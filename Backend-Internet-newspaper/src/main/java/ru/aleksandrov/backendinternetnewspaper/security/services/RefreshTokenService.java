package ru.aleksandrov.backendinternetnewspaper.security.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.aleksandrov.backendinternetnewspaper.model.RefreshToken;
import ru.aleksandrov.backendinternetnewspaper.repositories.RefreshTokenRepository;
import ru.aleksandrov.backendinternetnewspaper.repositories.UserRepository;
import ru.aleksandrov.backendinternetnewspaper.security.exception.TokenRefreshException;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
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

    public Optional<RefreshToken> findByToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken);
    }

    public RefreshToken createRefreshToken(Integer userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationsMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        log.info("Create new refresh token: Success");
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    public void deleteRefreshToken(Integer refreshTokenId) {
        if (refreshTokenRepository.existsById(refreshTokenId)) {
            refreshTokenRepository.deleteById(refreshTokenId);
            log.info("Delete refresh token with id = " + refreshTokenId + ": Success");
        } else {
            log.error("Refresh token with id = " + refreshTokenId + ": Not Found");
            throw new EntityNotFoundException("Refresh token with id = " + refreshTokenId + ": Not Found");
        }
    }
}
