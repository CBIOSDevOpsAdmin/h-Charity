package com.himanism.hcharityapi.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.himanism.hcharityapi.dto.response.UserResDto;
import com.himanism.hcharityapi.entities.Role;
import com.himanism.hcharityapi.entities.User;
import com.himanism.hcharityapi.mappers.UserMapper;
import com.himanism.hcharityapi.models.Erole;
import com.himanism.hcharityapi.repo.RoleRepository;
import com.himanism.hcharityapi.repo.UserRepository;
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

}
