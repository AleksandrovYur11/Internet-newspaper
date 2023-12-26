package ru.aleksandrov.backendinternetnewspaper.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.aleksandrov.backendinternetnewspaper.models.RefreshToken;
import ru.aleksandrov.backendinternetnewspaper.models.User;
import ru.aleksandrov.backendinternetnewspaper.dto.payload.request.SigninRequestDto;
import ru.aleksandrov.backendinternetnewspaper.dto.payload.request.RefreshTokenRequestDto;
import ru.aleksandrov.backendinternetnewspaper.dto.payload.request.SignupRequestDto;
import ru.aleksandrov.backendinternetnewspaper.dto.payload.response.SigninResponseDto;
import ru.aleksandrov.backendinternetnewspaper.dto.payload.response.RefreshTokenResponseDto;
import ru.aleksandrov.backendinternetnewspaper.security.JWT.JwtUtils;
import ru.aleksandrov.backendinternetnewspaper.security.exception.TokenRefreshException;
import ru.aleksandrov.backendinternetnewspaper.security.services.RefreshTokenService;
import ru.aleksandrov.backendinternetnewspaper.security.services.UserDetailsImpl;
import ru.aleksandrov.backendinternetnewspaper.services.RegistrationService;
import ru.aleksandrov.backendinternetnewspaper.services.RoleService;
import ru.aleksandrov.backendinternetnewspaper.utils.MappingUtil;
import ru.aleksandrov.backendinternetnewspaper.utils.UserValidator;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
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
                                    JwtUtils jwtUtils, RefreshTokenService refreshTokenService, RoleService roleService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.mappingUtil = mappingUtil;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.roleService = roleService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SigninResponseDto> login(@RequestBody @Valid SigninRequestDto signinRequestDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signinRequestDto.getEmail(),
                        signinRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String accessJwt = jwtUtils.generateJwtToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        SigninResponseDto signinResponseDto = SigninResponseDto.builder()
                .accessToken(accessJwt)
                .refreshToken(refreshToken.getToken())
                .id(userDetails.getId())
                .name(userDetails.getName())
                .roles(roles)
                .build();
        return new ResponseEntity<>(signinResponseDto, HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registration(@RequestBody @Valid SignupRequestDto signupRequestDto,
                                          BindingResult bindingResult) {
        userValidator.validate(signupRequestDto, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
        }
        User newUser = mappingUtil.convertToUser(signupRequestDto);
        roleService.setDefaultRole(newUser);
        registrationService.register(newUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> createNewAccessToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        String refreshToken = refreshTokenRequestDto.getRefreshToken();
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
//                    Collection<? extends GrantedAuthority> authorities = new ArrayList<>(user.getRoles());
                    String newAccessToken = jwtUtils.generateJwtToken(UserDetailsImpl.build(user));
                    return ResponseEntity.ok(new RefreshTokenResponseDto(newAccessToken, refreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in database"));
    }

    @PostMapping("/sign-out")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> logout(String refreshToken) {
        refreshTokenService.deleteRefreshToken(refreshToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
