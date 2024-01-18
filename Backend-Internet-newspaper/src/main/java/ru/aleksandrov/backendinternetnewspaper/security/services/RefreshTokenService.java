package ru.aleksandrov.backendinternetnewspaper.security.services;

import ru.aleksandrov.backendinternetnewspaper.models.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String refreshToken);
    RefreshToken createRefreshToken(Integer userId);
    RefreshToken verifyExpiration(RefreshToken token);
    void deleteRefreshToken(String refreshToken);
}

