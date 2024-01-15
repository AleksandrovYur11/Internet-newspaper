package ru.aleksandrov.backendinternetnewspaper.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import ru.aleksandrov.backendinternetnewspaper.dto.payload.request.RefreshTokenRequestDto;
import ru.aleksandrov.backendinternetnewspaper.dto.payload.request.SigninRequestDto;
import ru.aleksandrov.backendinternetnewspaper.dto.payload.request.SignupRequestDto;
import ru.aleksandrov.backendinternetnewspaper.models.ERole;
import ru.aleksandrov.backendinternetnewspaper.models.RefreshToken;
import ru.aleksandrov.backendinternetnewspaper.models.Role;
import ru.aleksandrov.backendinternetnewspaper.models.User;
import ru.aleksandrov.backendinternetnewspaper.security.JWT.JwtUtils;
import ru.aleksandrov.backendinternetnewspaper.security.exception.TokenRefreshException;
import ru.aleksandrov.backendinternetnewspaper.security.services.RefreshTokenService;
import ru.aleksandrov.backendinternetnewspaper.security.services.UserDetailsImpl;
import ru.aleksandrov.backendinternetnewspaper.services.RegistrationService;
import ru.aleksandrov.backendinternetnewspaper.services.RoleService;
import ru.aleksandrov.backendinternetnewspaper.utils.MappingUtil;
import ru.aleksandrov.backendinternetnewspaper.utils.UserValidator;

@ContextConfiguration(classes = {AuthenticationController.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class AuthenticationControllerTest {
    @Autowired
    private AuthenticationController authenticationController;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private RoleService roleService;

    @MockBean
    private MappingUtil mappingUtil;

    @MockBean
    private UserValidator userValidator;

    /**
     * Method under test: {@link AuthenticationController#createNewAccessToken(RefreshTokenRequestDto)}
     */
    @Test
    public void createNewAccessToken_WithValidRefreshToken() throws Exception {
        when(jwtUtils.generateJwtToken(any(UserDetailsImpl.class))).thenReturn("testAccessToken");

        User user = User.builder()
                .id(1)
                .name("John")
                .surname("Cena")
                .email("cena@yandex.ru")
                .password("Password123")
                .role(new Role(ERole.ROLE_USER))
                .build();

        RefreshToken testRefreshToken = RefreshToken.builder()
                .token("testRefreshToken")
                .user(user)
                .build();

        user.setRefreshToken(Collections.singletonList(testRefreshToken));

        Optional<RefreshToken> ofResult = Optional.of(testRefreshToken);
        when(refreshTokenService.verifyExpiration(any(RefreshToken.class))).thenReturn(testRefreshToken);
        when(refreshTokenService.findByToken(anyString())).thenReturn(ofResult);

        RefreshTokenRequestDto refreshTokenRequestDto = new RefreshTokenRequestDto();
        refreshTokenRequestDto.setRefreshToken(testRefreshToken.getToken());
        String content = (new ObjectMapper()).writeValueAsString(refreshTokenRequestDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(authenticationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.accessToken").value("testAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("testRefreshToken"));
    }

    /**
     * Method under test: {@link AuthenticationController#createNewAccessToken(RefreshTokenRequestDto)}
     */
    @Test
    public void createNewAccessToken_WithInvalidRefreshToken() throws Exception {
        when(jwtUtils.generateJwtToken(any(UserDetailsImpl.class)))
                .thenThrow(new TokenRefreshException("testRefreshToken", "Refresh token was expired. Please make a new signin request"));

        User user = User.builder()
                .id(1)
                .name("John")
                .surname("Cena")
                .email("cena@yandex.ru")
                .password("Password123")
                .role(new Role(ERole.ROLE_USER))
                .build();

        RefreshToken testRefreshToken = RefreshToken.builder()
                .token("testRefreshToken")
                .user(user)
                .build();

        user.setRefreshToken(Collections.singletonList(testRefreshToken));

        Optional<RefreshToken> ofResult = Optional.of(testRefreshToken);
        when(refreshTokenService.verifyExpiration(any(RefreshToken.class))).thenReturn(testRefreshToken);
        when(refreshTokenService.findByToken(anyString())).thenReturn(ofResult);

        RefreshTokenRequestDto refreshTokenRequestDto = new RefreshTokenRequestDto();
        refreshTokenRequestDto.setRefreshToken(testRefreshToken.getToken());
        String content = (new ObjectMapper()).writeValueAsString(refreshTokenRequestDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authenticationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult
                .andExpect(status().isForbidden());
    }

    /**
     * Method under test: {@link AuthenticationController#logout(RefreshTokenRequestDto)}
     */
    @Test
    public void testLogout() throws Exception {
        doNothing().when(refreshTokenService).deleteRefreshToken(any());

        RefreshTokenRequestDto refreshTokenRequestDto = RefreshTokenRequestDto.builder()
                .refreshToken("ABC123")
                .build();

        String content = (new ObjectMapper()).writeValueAsString(refreshTokenRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/sign-out")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(authenticationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Method under test: {@link AuthenticationController#registration(SignupRequestDto, BindingResult)}
     */
    @Test
    public void registration_WithValidUserData() throws Exception {

        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .name("John")
                .surname("Cena")
                .email("cena@yandex.ru")
                .password("Password123")
                .build();

        String userJson = new ObjectMapper().writeValueAsString(signupRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/sign-up")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authenticationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(status().isCreated());
    }

    /**
     * Method under test: {@link AuthenticationController#registration(SignupRequestDto, BindingResult)}
     */
    @Test
    public void registration_WithoutSurname() throws Exception {

        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .name("John")
                .surname("")
                .email("cena@yandex.ru")
                .password("Password123")
                .build();

        String userJson = new ObjectMapper().writeValueAsString(signupRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/sign-up")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authenticationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(status().isConflict())
                .andExpect(jsonPath("$.surname")
                        .value("The surname should have a minimum length of 2 characters"));
    }

    /**
     * Method under test: {@link AuthenticationController#registration(SignupRequestDto, BindingResult)}
     */
    @Test
    public void registration_WithoutName() throws Exception {

        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .name("")
                .surname("Cena")
                .email("cena@yandex.ru")
                .password("Password123")
                .build();

        String userJson = new ObjectMapper().writeValueAsString(signupRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/sign-up")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authenticationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(status().isConflict())
                .andExpect(jsonPath("$.name")
                        .value("The name should have a minimum length of 2 characters"));
    }

    /**
     * Method under test: {@link AuthenticationController#registration(SignupRequestDto, BindingResult)}
     */
    @Test
    public void registration_WithInvalidPassword() throws Exception {

        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .name("John")
                .surname("Cena")
                .email("cena@yandex.ru")
                .password("password123")
                .build();

        String userJson = new ObjectMapper().writeValueAsString(signupRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/sign-up")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authenticationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(status().isConflict())
                .andExpect(jsonPath("$.password")
                        .value("The password must contain at least 8 characters, including both uppercase and lowercase letters, as well as at least one numeric digit from 0 to 9"));
    }

    /**
     * Method under test: {@link AuthenticationController#registration(SignupRequestDto, BindingResult)}
     */
    @Test
    public void registration_WithInvalidEmail() throws Exception {

        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .name("John")
                .surname("Cena")
                .email("cenayandex.ru")
                .password("Password123")
                .build();

        String userJson = new ObjectMapper().writeValueAsString(signupRequestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/sign-up")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authenticationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(status().isConflict())
                .andExpect(jsonPath("$.email")
                        .value("Please enter a valid email address"));
    }

    /**
     * Method under test: {@link AuthenticationController#registration(SignupRequestDto, BindingResult)}
     */
    @Test
    public void login_WithValidUserData() throws Exception {
        SigninRequestDto requestDto = SigninRequestDto.builder()
                .email("test@yandex.ru")
                .password("TestPassword123")
                .build();

        Authentication mockAuthentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth/sign-in")
                .content(new ObjectMapper().writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authenticationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(status().isCreated());
    }
}

