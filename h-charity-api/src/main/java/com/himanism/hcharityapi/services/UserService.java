package com.himanism.hcharityapi.services;

import java.util.List;

import com.himanism.hcharityapi.dto.response.UserResDto;
import com.himanism.hcharityapi.models.Erole;

public interface UserService {
    UserResDto getUserById(Long userId);

    List<UserResDto> getUserByRole(Erole role);
}
