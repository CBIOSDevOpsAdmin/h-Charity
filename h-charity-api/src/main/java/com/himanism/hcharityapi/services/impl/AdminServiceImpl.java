package com.himanism.hcharityapi.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.himanism.hcharityapi.dto.request.UserReqDto;
import com.himanism.hcharityapi.dto.response.UserResDto;
import com.himanism.hcharityapi.entities.Appeal;
import com.himanism.hcharityapi.entities.Role;
import com.himanism.hcharityapi.entities.User;
import com.himanism.hcharityapi.mappers.AppealMapper;
import com.himanism.hcharityapi.mappers.UserMapper;
import com.himanism.hcharityapi.models.Erole;
import com.himanism.hcharityapi.repo.RoleRepository;
import com.himanism.hcharityapi.repo.UserRepository;
import com.himanism.hcharityapi.security.services.UserDetailsImpl;
import com.himanism.hcharityapi.services.AdminService;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<UserResDto> getUsers(Authentication authentication) {
        Role adminRole = roleRepository.findByName(Erole.ADMIN).get();
        List<User> users = userRepository.findByRolesNotContaining(adminRole);

        return users.stream().map(UserMapper.INSTANCE::userToUserResponseDTO).collect(Collectors.toList());
    }

    @Override
    public UserResDto updateUser(@Valid UserReqDto userReqDto, UserDetailsImpl principle) {
        try {
            User existingUser = userRepository.findById(userReqDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));

            User user = UserMapper.INSTANCE.userRequestDTOToUser(userReqDto);
            user.setPassword(existingUser.getPassword());
            user.setRoles(existingUser.getRoles());

            // Fetch the old and new roles by their names
            Optional<Role> oldRole = roleRepository
                    .findByName(existingUser.getRoles().stream().findFirst().get().getName());
            Optional<Role> newRole = roleRepository.findByName(Erole.valueOf(userReqDto.getRole()));

            Set<Role> roles = user.getRoles();
            if (roles.contains(oldRole.get())) {
                roles.remove(oldRole.get()); // Remove the old role
            }
            roles.add(newRole.get()); // Add the new role

            user.setRoles(roles); // Update the user's roles
            userRepository.save(user);
            return UserMapper.INSTANCE.userToUserResponseDTO(user);
        } finally {

        }
    }

}
