package com.himanism.hcharityapi.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.himanism.hcharityapi.dto.request.UserReqDto;
import com.himanism.hcharityapi.dto.response.UserResDto;
import com.himanism.hcharityapi.entities.Role;
import com.himanism.hcharityapi.entities.User;
import com.himanism.hcharityapi.mappers.UserMapper;
import com.himanism.hcharityapi.models.Erole;
import com.himanism.hcharityapi.repo.RoleRepository;
import com.himanism.hcharityapi.repo.UserRepository;
import com.himanism.hcharityapi.security.services.UserDetailsImpl;
import com.himanism.hcharityapi.services.AdminService;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<UserResDto> getUsers(Authentication authentication) {
        Logger log = LoggerFactory.getLogger(getClass());

        try {
            log.info("Admin Service: Fetching Usrs");
            Role adminRole = roleRepository.findByName(Erole.ADMIN)
                    .orElseThrow(() -> new IllegalArgumentException("Admin role not found"));
            List<User> users = userRepository.findByRolesNotContaining(adminRole);
            return users.stream()
                    .map(UserMapper.INSTANCE::userToUserResponseDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("An error occurred while fetching users", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch users", e);
        }
    }

    @Override
    public UserResDto updateUser(@Valid UserReqDto userReqDto, UserDetailsImpl principle) {
        Logger log = LoggerFactory.getLogger(getClass());

        try {
            log.info("Admin Service : Fetching existing user with ID", userReqDto.getId());
            User existingUser = userRepository.findById(userReqDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));

            User user = UserMapper.INSTANCE.userRequestDTOToUser(userReqDto);
            user.setPassword(existingUser.getPassword());
            user.setRoles(existingUser.getRoles());
            Optional<Role> oldRole = roleRepository
                    .findByName(existingUser.getRoles().stream().findFirst().get().getName());
            Optional<Role> newRole = roleRepository.findByName(Erole.valueOf(userReqDto.getRole()));

            Set<Role> roles = user.getRoles();
            if (roles.contains(oldRole.orElse(null))) {
                roles.remove(oldRole.get());
            }

            roles.add(newRole.orElseThrow(() -> new IllegalArgumentException("Invalid new role")));

            user.setRoles(roles);
            log.info("Saving updated user.");
            userRepository.save(user);

            log.info("User with ID : successfully updated.", userReqDto.getId());
            return UserMapper.INSTANCE.userToUserResponseDTO(user);
        } catch (Exception e) {
            log.error("An error occurred while updating user with ID", userReqDto.getId(), e.getMessage(), e);
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        try {
            log.info("Admin Service : delete user with ID:", userId);
            userRepository.deleteById(userId);
            log.info("Successfully deleted user with ID", userId);
        } catch (Exception e) {
            log.error("Error while deleting user with ID", userId, e);
        }
    }

    @Override
    public void deleteUsersByIds(List<Long> userIds) {
        try {
            log.info("Admin Service : delete multiple users with IDs", userIds);
            userRepository.deleteUsersByIds(userIds);
            log.info("Successfully deleted users with IDs", userIds);
        } catch (Exception e) {
            log.error("Error while deleting users with IDs", userIds, e);
        }
    }

}
