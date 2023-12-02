package ru.aleksandrov.backendinternetnewspaper.controllers;

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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.aleksandrov.backendinternetnewspaper.dto.UserDto;
import ru.aleksandrov.backendinternetnewspaper.models.RefreshToken;
import ru.aleksandrov.backendinternetnewspaper.models.User;
import ru.aleksandrov.backendinternetnewspaper.payload.request.SigninRequest;
import ru.aleksandrov.backendinternetnewspaper.payload.request.RefreshTokenRequest;
import ru.aleksandrov.backendinternetnewspaper.payload.request.SignupRequest;
import ru.aleksandrov.backendinternetnewspaper.payload.response.SignupResponse;
import ru.aleksandrov.backendinternetnewspaper.payload.response.RefreshTokenResponse;
import ru.aleksandrov.backendinternetnewspaper.security.JWT.JwtUtils;
import ru.aleksandrov.backendinternetnewspaper.security.exception.TokenRefreshException;
import ru.aleksandrov.backendinternetnewspaper.security.services.RefreshTokenService;
import ru.aleksandrov.backendinternetnewspaper.security.services.UserDetailsImpl;
import ru.aleksandrov.backendinternetnewspaper.services.RegistrationService;
import ru.aleksandrov.backendinternetnewspaper.services.RoleService;
import ru.aleksandrov.backendinternetnewspaper.util.MappingUtil;
import ru.aleksandrov.backendinternetnewspaper.util.UserValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
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

    @PostMapping("/sign-in")
    public ResponseEntity<SignupResponse> login(@RequestBody @Valid SigninRequest signinRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),
                        signinRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String accessJwt = jwtUtils.generateJwtToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        SignupResponse response = new SignupResponse(accessJwt, refreshToken.getToken(), userDetails.getId(),
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

    @PostMapping("/sign-up")
    public ResponseEntity<?> perfectRegistration(@RequestBody
                                                 @Valid SignupRequest signupRequest,
                                                 BindingResult bindingResult) {
        userValidator.validate(signupRequest, bindingResult);
        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
        if (!fieldErrorList.isEmpty()) {
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
//
        User newUser = mappingUtil.convertToUser(signupRequest);
        roleService.setDefaultRole(newUser);
        registrationService.register(newUser);

        return new ResponseEntity<>("Created new user", HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
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

    @PostMapping("/sign-out")
    public ResponseEntity<?> logoutUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        int userId = userDetails.getId();
        refreshTokenService.deleteRefreshToken(userId);
        return ResponseEntity.ok("Logout successful!");
    }
}
