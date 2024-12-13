package com.himanism.hcharityapi.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.himanism.hcharityapi.dto.response.UserResDto;
import com.himanism.hcharityapi.entities.User;
import com.himanism.hcharityapi.mappers.UserMapper;
import com.himanism.hcharityapi.models.Erole;
import com.himanism.hcharityapi.repo.UserRepository;
import com.himanism.hcharityapi.services.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResDto getUserById(Long userId) {
        try {
            log.info("User Service: Fetching user with ID", userId);

            Optional<User> optUser = userRepository.findById(userId);

            if (optUser.isPresent()) {
                UserResDto userResDto = UserMapper.INSTANCE.userToUserResponseDTO(optUser.get());
                log.info("Successfully fetched user with ID", userId);
                return userResDto;
            } else {
                log.warn("User with ID not found", userId);
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching user with ID", userId, e);
            throw new RuntimeException("Error fetching user with ID " + userId, e);
        }
    }

    @Override
    public List<UserResDto> getUserByRole(Erole role) {
        try {
            log.info("User Service: Fetching users with role", role);

            List<User> users = userRepository.findAllByRoleName(role);

            if (users.isEmpty()) {
                log.warn("No users found with role", role);
            } else {
                log.info("Successfully fetched users with role:", users.size(), role);
            }

            return users.stream()
                    .map(user -> UserMapper.INSTANCE.userToUserResponseDTO(user))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error occurred while fetching users with role:", role, e);
            throw new RuntimeException("Error fetching users with role: " + role, e);
        }
    }

}
