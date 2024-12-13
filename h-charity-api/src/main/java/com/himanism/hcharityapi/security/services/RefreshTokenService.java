package com.himanism.hcharityapi.security.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.himanism.hcharityapi.entities.RefreshToken;
import com.himanism.hcharityapi.exception.TokenRefreshException;
import com.himanism.hcharityapi.repo.RefreshTokenRepository;
import com.himanism.hcharityapi.repo.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RefreshTokenService {
  @Value("${jwt.refreshExpirationMs}")
  private Long refreshTokenDurationMs;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private UserRepository userRepository;

  public Optional<RefreshToken> findByToken(String token) {
    try {
      log.info("RefreshTokenService: Finding refresh token", token);
      return refreshTokenRepository.findByToken(token);
    } catch (Exception e) {
      log.error(" Error finding refresh token", e.getMessage(), e);
      throw e;
    }
  }

  public RefreshToken createRefreshToken(Long userId) {
    try {
      log.info("RefreshTokenService: Creating refresh token for user ID", userId);
      RefreshToken refreshToken = new RefreshToken();

      refreshToken.setUser(userRepository.findById(userId).orElseThrow(() -> {
        log.error("User ID not found: ", userId);
        return new RuntimeException("User not found");
      }));
      refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
      refreshToken.setToken(UUID.randomUUID().toString());

      refreshToken = refreshTokenRepository.save(refreshToken);
      log.info("Refresh token created successfully for user ID", userId);
      return refreshToken;
    } catch (Exception e) {
      log.error("Error creating refresh token for user ID:", userId, e.getMessage(), e);
      throw e;
    }
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    try {
      log.info("RefreshTokenService: Verifying expiration for refresh token", token.getToken());
      if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
        log.error("Refresh token expired", token.getToken());
        refreshTokenRepository.delete(token);
        throw new TokenRefreshException(token.getToken(),
            "Refresh token was expired. Please make a new signin request");
      }
      log.info("Refresh token is valid", token.getToken());
      return token;
    } catch (TokenRefreshException e) {
      throw e;
    } catch (Exception e) {
      log.error("Error verifying refresh token expiration", e.getMessage(), e);
      throw e;
    }
  }

  @Transactional
  public int deleteByUserId(Long userId) {
    try {
      log.info("RefreshTokenService: Deleting refresh tokens for user ID", userId);
      int deletedCount = refreshTokenRepository.deleteByUser(userRepository.findById(userId).orElseThrow(() -> {
        log.error("User ID not found", userId);
        return new RuntimeException("User not found");
      }));
      log.info("Deleted {} refresh tokens for user ID", deletedCount, userId);
      return deletedCount;
    } catch (Exception e) {
      log.error("Error deleting refresh tokens for user ID:", userId, e.getMessage(), e);
      throw e;
    }
  }
}
