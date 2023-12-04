package ru.aleksandrov.backendinternetnewspaper.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.model.RefreshToken;
import ru.aleksandrov.backendinternetnewspaper.repositories.RefreshTokenRepository;
import ru.aleksandrov.backendinternetnewspaper.repositories.UserRepository;
import ru.aleksandrov.backendinternetnewspaper.security.exception.TokenRefreshException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${internet-newspaper.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationsMs;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }
    public RefreshToken createRefreshToken(Integer userId){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationsMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if (token.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(token);
            throw  new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return  token;
    }

    public void deleteRefreshToken(int id){
        refreshTokenRepository.deleteByUserId(id);
    }
}
