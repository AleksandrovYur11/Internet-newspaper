package ru.aleksandrov.backendinternetnewspaper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.aleksandrov.backendinternetnewspaper.model.RefreshToken;

import java.util.Optional;
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);

    @Transactional
    void deleteByUserId(int id);
}


