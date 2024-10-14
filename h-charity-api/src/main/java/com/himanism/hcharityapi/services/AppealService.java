package com.himanism.hcharityapi.services;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.himanism.hcharityapi.dto.request.AppealRequestDto;
import com.himanism.hcharityapi.dto.response.AppealResDto;
import com.himanism.hcharityapi.entities.Appeal;

public interface AppealService {

    List<AppealResDto> getAppeals(Authentication authentication);

    Appeal addAppeal(AppealRequestDto appealDto, String username, Long userId);

    Appeal updateAppeal(AppealRequestDto appealDto);

    void deleteAppeal(Long appealId);

    AppealResDto getAppealById(Long appealId);
}
