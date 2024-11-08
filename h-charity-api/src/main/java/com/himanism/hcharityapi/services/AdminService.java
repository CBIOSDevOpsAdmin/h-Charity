package com.himanism.hcharityapi.services;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.himanism.hcharityapi.dto.request.UserReqDto;
import com.himanism.hcharityapi.dto.response.UserResDto;
import com.himanism.hcharityapi.security.services.UserDetailsImpl;

import jakarta.validation.Valid;

public interface AdminService {
    List<UserResDto> getUsers(Authentication authentication);

    UserResDto updateUser(@Valid UserReqDto userReqDto, UserDetailsImpl principle);

    // Appeal addAppeal(AppealRequestDto appealDto, String username, Long userId);

    // Appeal updateAppeal(AppealRequestDto appealDto);

    // void deleteAppeal(Long appealId);

    // AppealResDto getAppealById(Long appealId);
}
