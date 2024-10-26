package com.himanism.hcharityapi.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import com.himanism.hcharityapi.dto.response.UserResDto;

public interface AdminService {
    List<UserResDto> getUsers(Authentication authentication);

    // Appeal addAppeal(AppealRequestDto appealDto, String username, Long userId);

    // Appeal updateAppeal(AppealRequestDto appealDto);

    // void deleteAppeal(Long appealId);

    // AppealResDto getAppealById(Long appealId);
}
