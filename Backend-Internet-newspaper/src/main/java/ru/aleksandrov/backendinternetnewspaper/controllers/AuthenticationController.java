package ru.aleksandrov.backendinternetnewspaper.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    private final UserValidator userValidator;
    private final RegistrationService registrationService;

    private final MappingUtil mappingUtil;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;
    private final RoleService roleService;

    @Autowired
    public AuthenticationController(UserValidator userValidator, RegistrationService registrationService,
                                    MappingUtil mappingUtil, AuthenticationManager authenticationManager,
                                    JwtUtils jwtUtils, RefreshTokenService refreshTokenService,
                                    RoleService roleService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.mappingUtil = mappingUtil;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.roleService = roleService;
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String accessJwt = jwtUtils.generateJwtToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        LoginResponse response = new LoginResponse(accessJwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getName(), roles);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @PostMapping("/signup")
//    public ResponseEntity<?> perfectRegistration(@RequestBody @Valid UserDto userDTO,
//                                                 BindingResult bindingResult) {
//        userValidator.validate(userDTO, bindingResult);
//        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
//        if (!fieldErrorList.isEmpty()) {
//            return new ResponseEntity<>(fieldErrorList.stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
//                    .collect(Collectors.toList()), HttpStatus.BAD_REQUEST);
//        }
//        try {
//            User newUser = mappingUtil.convertToUser(userDTO);
//            System.out.println(newUser.toString());
//            registrationService.register(newUser);
//            return new ResponseEntity<>("Created new user", HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }

    @PostMapping("/signup")
    public ResponseEntity<?> perfectRegistration(@RequestBody
                                                 @Valid UserDto userDTO,
                                                 BindingResult bindingResult) {
        userValidator.validate(userDTO, bindingResult);
//        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
//        if (!fieldErrorList.isEmpty()) {
//            throw new MethodArgumentNotValidException(new MethodParameter(null, -1), bindingResult);
//        }
//
        User newUser = mappingUtil.convertToUser(userDTO);
        roleService.setDefaultRole(newUser);
        User savedUser = registrationService.register(newUser);

        return new ResponseEntity<>("Created new user", HttpStatus.CREATED);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
//                    Collection<? extends GrantedAuthority> authorities = new ArrayList<>(user.getRoles());
                    String newAccessToken = jwtUtils.generateJwtToken(UserDetailsImpl.build(user));
                    return ResponseEntity.ok(new RefreshTokenResponse(newAccessToken, refreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken,
                        "Refresh token is not in database"));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        int userId = userDetails.getId();
        refreshTokenService.deleteRefreshToken(userId);
        return ResponseEntity.ok("Logout successful!");
    }
}
