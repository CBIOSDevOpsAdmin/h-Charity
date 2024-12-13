package com.himanism.hcharityapi.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.himanism.hcharityapi.dto.request.LoginRequestDto;
import com.himanism.hcharityapi.dto.request.SignupRequestDto;
import com.himanism.hcharityapi.dto.response.MessageResponseDto;
import com.himanism.hcharityapi.dto.response.UserInfoResponse;
import com.himanism.hcharityapi.entities.RefreshToken;
import com.himanism.hcharityapi.entities.Role;
import com.himanism.hcharityapi.entities.User;
import com.himanism.hcharityapi.exception.TokenRefreshException;
import com.himanism.hcharityapi.models.Erole;
import com.himanism.hcharityapi.repo.RoleRepository;
import com.himanism.hcharityapi.repo.UserRepository;
import com.himanism.hcharityapi.security.jwt.JwtUtils;
import com.himanism.hcharityapi.security.services.RefreshTokenService;
import com.himanism.hcharityapi.security.services.UserDetailsImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials = "true")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  RefreshTokenService refreshTokenService;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
    try {
      log.info("Auth Controller: Authenticate user", loginRequest.getUsername());
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
      List<String> roles = userDetails.getAuthorities().stream()
          .map(item -> item.getAuthority())
          .collect(Collectors.toList());
      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
      ResponseCookie jwtRefreshCookie = jwtUtils.generateRefreshJwtCookie(refreshToken.getToken());

      log.info("User authenticated successfully", loginRequest.getUsername());
      return ResponseEntity.ok()
          .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
          .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
          .body(new UserInfoResponse(userDetails.getId(),
              userDetails.getUsername(),
              userDetails.getEmail(),
              userDetails.getMobile(),
              roles));
    } catch (Exception e) {
      log.error("Error during user authentication", e.getMessage(), e);
      return ResponseEntity.badRequest().body(new MessageResponseDto("Invalid credentials!"));
    }
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDto signUpRequest) {
    try {
      log.info("Auth Controller: Register user", signUpRequest.getUsername());
      if (Boolean.TRUE.equals(userRepository.existsByUsername(signUpRequest.getUsername()))) {
        return ResponseEntity.badRequest().body(new MessageResponseDto("Error: Username is already taken!"));
      }

      if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {
        return ResponseEntity.badRequest().body(new MessageResponseDto("Error: Email is already in use!"));
      }

      User user = new User(signUpRequest.getUsername(),
          signUpRequest.getFullname(),
          signUpRequest.getEmail(),
          encoder.encode(signUpRequest.getPassword()), signUpRequest.getMobile());

      Set<String> strRoles = signUpRequest.getRole();
      Set<Role> roles = new HashSet<>();

      if (strRoles == null) {
        Role userRole = roleRepository.findByName(Erole.NORMAL_USER)
            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
      } else {
        strRoles.forEach(role -> {
          switch (role) {
            case "admin":
              Role adminRole = roleRepository.findByName(Erole.ADMIN)
                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
              roles.add(adminRole);
              break;
            case "mod":
              Role modRole = roleRepository.findByName(Erole.ORGANISATION_VOLUNTEER)
                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
              roles.add(modRole);
              break;
            default:
              Role userRole = roleRepository.findByName(Erole.NORMAL_USER)
                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
              roles.add(userRole);
          }
        });
      }

      user.setRoles(roles);
      userRepository.save(user);

      log.info("User registered successfully", signUpRequest.getUsername());
      return ResponseEntity.ok(new MessageResponseDto("User registered successfully!"));
    } catch (Exception e) {
      log.error("Error during user registration", e.getMessage(), e);
      return ResponseEntity.internalServerError().body(new MessageResponseDto("Error: Registration failed!"));
    }
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser(HttpServletRequest request) {
    try {
      log.info("Auth Controller: log out user");
      Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (!principle.toString().equals("anonymousUser")) {
        Long userId = ((UserDetailsImpl) principle).getId();
        refreshTokenService.deleteByUserId(userId);
        log.info("Refresh tokens cleared for user ID", userId);
      }

      ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
      ResponseCookie jwtRefreshCookie = jwtUtils.getCleanJwtRefreshCookie();

      log.info("User logged out successfully");
      return ResponseEntity.ok()
          .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
          .header(HttpHeaders.SET_COOKIE, jwtRefreshCookie.toString())
          .body(new MessageResponseDto("You've been signed out!"));
    } catch (Exception e) {
      log.error("Error during user logout", e.getMessage(), e);
      return ResponseEntity.internalServerError().body(new MessageResponseDto("Error: Logout failed!"));
    }
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(HttpServletRequest request) {
    try {
      log.info("Auth Controller: Refresh token");
      String refreshToken = jwtUtils.getJwtRefreshFromCookies(request);

      if (refreshToken != null && !refreshToken.isEmpty()) {
        return refreshTokenService.findByToken(refreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUser)
            .map(user -> {
              ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(user);
              log.info("Token refreshed successfully");
              return ResponseEntity.ok()
                  .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                  .body(new MessageResponseDto("Token is refreshed successfully!"));
            })
            .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token is not in database!"));
      }

      return ResponseEntity.badRequest().body(new MessageResponseDto("Refresh Token is empty!"));
    } catch (Exception e) {
      log.error("Error during token refresh", e.getMessage(), e);
      return ResponseEntity.internalServerError().body(new MessageResponseDto("Error: Token refresh failed!"));
    }
  }
}