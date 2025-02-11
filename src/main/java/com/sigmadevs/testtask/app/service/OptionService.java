package com.sigmadevs.testtask.app.service;

import com.sigmadevs.testtask.app.dto.CreateOptionDTO;
import com.sigmadevs.testtask.app.dto.OptionDTO;
import com.sigmadevs.testtask.app.dto.UpdateOptionDTO;
import com.sigmadevs.testtask.app.entity.Option;
import com.sigmadevs.testtask.app.entity.Task;
import com.sigmadevs.testtask.app.exception.OptionNotFoundException;
import com.sigmadevs.testtask.app.mapper.OptionMapper;
import com.sigmadevs.testtask.app.repository.OptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptionService {

    private final OptionRepository optionRepository;

    private final OptionMapper optionMapper;

    public void createOptions(List<CreateOptionDTO> options, Task task) {
        try{

            List<Option> list = options.stream().map(e -> optionMapper.toEntity(e, task)).toList();
            optionRepository.saveAll(list);
        }catch (Exception e){
            log.error("Error while creating all options", e);
            throw new RuntimeException(e); //TODO
        }
    }
    @Transactional
    public OptionDTO updateOption(UpdateOptionDTO updateOptionDTO, Principal principal) {
        log.info("Updating option with ID: {}", updateOptionDTO.getId());

        Option option = optionRepository.findById(updateOptionDTO.getId())
                .orElseThrow(() -> {
                    log.warn("Option with ID {} not found!", updateOptionDTO.getId());
                    return new OptionNotFoundException("Option with Id " + updateOptionDTO.getId() + " not found!");
                });

        String currentUsername = principal.getName();

        if (!option.getTask().getQuest().getUser().getUsername().equals(currentUsername)) {
            log.warn("User {} is not the owner of option ID {}", currentUsername ,updateOptionDTO.getId());
            throw new AccessDeniedException("You are not allowed to update this option");
        }

        option.setTitle(updateOptionDTO.getTitle());
        option.setIsTrue(updateOptionDTO.getIsTrue());

        log.info("Option with ID {} updated successfully", option.getId());
        return optionMapper.toDTO(option);
    }

    public List<OptionDTO> getOptionsByTaskId(long taskId) {
        return optionRepository.findAll().stream()
                .filter(option -> option.getTask().getId() == taskId)
                .map(option -> optionMapper.toDTO(option))
                .collect(Collectors.toList());
    }

    public void removeOptionById(Long id, Principal principal) {
        log.info("Removing option with ID: {}", id);

        Option option = optionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Option with ID {} not found!", id);
                    return new OptionNotFoundException("Option with Id " + id + " not found!");
                });

        String currentUsername = principal.getName();

        if (!option.getTask().getQuest().getUser().getUsername().equals(currentUsername)) {
            log.warn("User {} is not the owner of option ID {}", currentUsername, id);
            throw new AccessDeniedException("You are not allowed to delete this option");
        }

        optionRepository.deleteById(id);
        log.info("Option with ID {} removed successfully", id);
    }

}