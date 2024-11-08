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

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResDto getUserById(Long userId) {
        Optional<User> optUser = userRepository.findById(userId);
        return UserMapper.INSTANCE.userToUserResponseDTO(optUser.get());
    }

    @Override
    public List<UserResDto> getUserByRole(Erole role) {
        List<User> users = userRepository.findAllByRoleName(role);

        return users.stream()
                .map(user -> UserMapper.INSTANCE.userToUserResponseDTO(user))
                .collect(Collectors.toList());
    }
}
