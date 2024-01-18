package ru.aleksandrov.backendinternetnewspaper.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
import ru.aleksandrov.backendinternetnewspaper.security.services.impl.RefreshTokenServiceImpl;
import ru.aleksandrov.backendinternetnewspaper.security.services.impl.UserDetailsImpl;
import ru.aleksandrov.backendinternetnewspaper.services.impl.RegistrationServiceImpl;
import ru.aleksandrov.backendinternetnewspaper.services.impl.RoleServiceImpl;
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
    private final RegistrationServiceImpl registrationServiceImpl;
    private final MappingUtil mappingUtil;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenServiceImpl refreshTokenServiceImpl;
    private final RoleServiceImpl roleServiceImpl;

    @Autowired
    public AuthenticationController(UserValidator userValidator, RegistrationServiceImpl registrationServiceImpl,
                                    MappingUtil mappingUtil, AuthenticationManager authenticationManager,
                                    JwtUtils jwtUtils, RefreshTokenServiceImpl refreshTokenServiceImpl,
                                    RoleServiceImpl roleServiceImpl) {
        this.userValidator = userValidator;
        this.registrationServiceImpl = registrationServiceImpl;
        this.mappingUtil = mappingUtil;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenServiceImpl = refreshTokenServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SigninResponseDto> login(@RequestBody @Valid SigninRequestDto signinRequestDto) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signinRequestDto.getEmail(),
                        signinRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String accessJwt = jwtUtils.generateJwtToken(userDetails);
        RefreshToken refreshToken = refreshTokenServiceImpl.createRefreshToken(userDetails.getId());
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        SigninResponseDto signinResponseDto = SigninResponseDto.builder()
                .accessToken(accessJwt)
                .refreshToken(refreshToken.getToken())
                .id(userDetails.getId())
                .name(userDetails.getName())
                .surname(userDetails.getSurname())
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
        roleServiceImpl.setDefaultRole(newUser);
        registrationServiceImpl.register(newUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> createNewAccessToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        String refreshToken = refreshTokenRequestDto.getRefreshToken();
        return refreshTokenServiceImpl.findByToken(refreshToken)
                .map(refreshTokenServiceImpl::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String newAccessToken = jwtUtils.generateJwtToken(UserDetailsImpl.build(user));
                    return ResponseEntity.ok(new RefreshTokenResponseDto(newAccessToken, refreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in database"));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> logout(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        String refreshToken = refreshTokenRequestDto.getRefreshToken();
        refreshTokenServiceImpl.deleteRefreshToken(refreshToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
