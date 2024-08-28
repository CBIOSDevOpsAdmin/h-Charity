package com.himanism.hcharityapi.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.himanism.hcharityapi.dto.request.AppealRequestDto;
import com.himanism.hcharityapi.dto.response.AppealResDto;
import com.himanism.hcharityapi.entities.Appeal;
import com.himanism.hcharityapi.mappers.AppealMapper;
import com.himanism.hcharityapi.repo.AppealRepository;
import com.himanism.hcharityapi.services.AppealService;

@Service
@Transactional
@RequiredArgsConstructor
public class AppealServiceImpl implements AppealService {

    private final AppealRepository appealRepository;

    @Override
    public List<AppealResDto> getAppeals(Authentication authentication) {
        List<Appeal> appeals = appealRepository.findAll();
        return appeals.stream().map(AppealMapper.INSTANCE::appealToAppealResponseDTO).collect(Collectors.toList());
    }

    @Override
    public Appeal addAppeal(AppealRequestDto appealDto) {
        Appeal appeal = AppealMapper.INSTANCE.appealRequestDTOtoAppeal(appealDto);
        return appealRepository.save(appeal);
    }

    @Override
    public Appeal updateAppeal(AppealRequestDto appealDto) {
        Optional<Appeal> existingAppeal = appealRepository.findById(appealDto.getId());
        if (existingAppeal.isEmpty())
            throw new IllegalArgumentException("Invalid Appeal ID");
        Appeal updatedAppeal = existingAppeal.get();
        updatedAppeal.setTitle(appealDto.getTitle());
        updatedAppeal.setDescription(appealDto.getDescription());
        updatedAppeal.setSelfOrBehalf(appealDto.getSelfOrBehalf());
        updatedAppeal.setOnBehalfName(appealDto.getOnBehalfName());
        updatedAppeal.setTotalFundsRequired(appealDto.getTotalFundsRequired());
        updatedAppeal.setFundsReceived(appealDto.getFundsReceived());
        updatedAppeal.setFundsNeeded(appealDto.getFundsNeeded());
        updatedAppeal.setIsZakatEligible(appealDto.getIsZakatEligible());
        updatedAppeal.setIsInterestEligible(appealDto.getIsInterestEligible());
        updatedAppeal.setIsAnonymous(appealDto.getIsAnonymous());
        return appealRepository.save(updatedAppeal);
    }

    @Override
    public void deleteAppeal(Long appealId) {
        appealRepository.deleteById(appealId);
    }

    @Override
    public AppealResDto getAppealById(Long appealId) {
        Optional<Appeal> optAppeal = appealRepository.findById(appealId);
        Appeal appeal = optAppeal.get();
        return AppealMapper.INSTANCE.appealToAppealResponseDTO(appeal);
    }
}
