package com.himanism.hcharityapi.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.himanism.hcharityapi.dto.request.AppealRequestDto;
import com.himanism.hcharityapi.dto.response.AppealResDto;
import com.himanism.hcharityapi.entities.Appeal;
import com.himanism.hcharityapi.mappers.AppealMapper;
import com.himanism.hcharityapi.repo.AppealRepository;
import com.himanism.hcharityapi.repo.EntityRepository;
import com.himanism.hcharityapi.repo.UserRepository;
import com.himanism.hcharityapi.services.AppealService;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AppealServiceImpl implements AppealService {

    private final AppealRepository appealRepository;
    private final UserRepository userRepository;
    private final EntityRepository entityRepository;

    @Override
    public List<AppealResDto> getAppeals(Authentication authentication) {
        try {
            log.info("Appeals Service : Fetching all appeals");
            List<Appeal> appeals = appealRepository.findAll();
            List<AppealResDto> appealResDtos = appeals.stream()
                    .map(AppealMapper.INSTANCE::appealToAppealResponseDTO)
                    .collect(Collectors.toList());
            log.info("Successfully fetched {} appeals", appeals.size());
            return appealResDtos;
        } catch (Exception e) {
            log.error("Error while fetching appeals", e);
            throw new RuntimeException("Error fetching appeals", e);
        }
    }

    @Override
    public Appeal addAppeal(AppealRequestDto appealDto, String username, Long userId, String userGroup) {
        Logger log = LoggerFactory.getLogger(getClass());

        try {
            log.info("Appeals Service : Addind a new appeal by user with ID", userId);
            Appeal appeal = AppealMapper.INSTANCE.appealRequestDTOtoAppeal(appealDto);
            appeal.setCreatedBy(username);
            appeal.setCreatedDate(new Date());

            if ("INSTITUTE_OWNER".equalsIgnoreCase(userGroup) || entityRepository.findByUserId(userId).isPresent()) {
                appeal.setEntity(entityRepository.findByUserId(userId).get());
            } else {
                appeal.setUser(userRepository.findById(userId).get());
            }

            Appeal savedAppeal = appealRepository.save(appeal);
            log.info("Successfully added new appeal with ID", savedAppeal.getId());
            return savedAppeal;
        } catch (Exception e) {
            log.error("Error while adding appeal for user with ID", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to add appeal", e);
        }
    }

    @Override
    public Appeal updateAppeal(AppealRequestDto appealDto) {
        Logger log = LoggerFactory.getLogger(getClass());

        try {
            log.info("Appeals Service : Update appeal with ID", appealDto.getId());
            Optional<Appeal> existingAppeal = appealRepository.findById(appealDto.getId());
            if (existingAppeal.isEmpty()) {
                log.error("Appeal with ID not found", appealDto.getId());
                throw new IllegalArgumentException("Invalid Appeal ID");
            }

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

            Appeal savedAppeal = appealRepository.save(updatedAppeal);
            log.info("Successfully updated appeal with ID", savedAppeal.getId());
            return savedAppeal;
        } catch (Exception e) {
            log.error("Error while updating appeal with ID", appealDto.getId(), e.getMessage(), e);
            throw new RuntimeException("Failed to update appeal", e);
        }
    }

    @Override
    public void deleteAppeal(Long appealId) {
        Logger log = LoggerFactory.getLogger(getClass());

        try {
            log.info("Appeals Service : Delete appeal with ID", appealId);
            appealRepository.deleteById(appealId);
            log.info("Successfully deleted appeal with ID", appealId);
        } catch (Exception e) {
            log.error("Error while deleting appeal with ID", appealId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete appeal", e);
        }
    }

    @Override
    public AppealResDto getAppealById(Long appealId) {
        Logger log = LoggerFactory.getLogger(getClass());

        try {
            log.info("Appeals Service : Fetch appeal with ID", appealId);
            Optional<Appeal> optAppeal = appealRepository.findById(appealId);
            if (optAppeal.isEmpty()) {
                log.error("Appeal with ID not found", appealId);
                throw new IllegalArgumentException("Invalid Appeal ID");
            }

            Appeal appeal = optAppeal.get();
            AppealResDto appealResDto = AppealMapper.INSTANCE.appealToAppealResponseDTO(appeal);
            log.info("Successfully fetched appeal with ID", appealId);
            return appealResDto;
        } catch (Exception e) {
            log.error("Error while fetching appeal with ID", appealId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch appeal", e);
        }
    }
}
