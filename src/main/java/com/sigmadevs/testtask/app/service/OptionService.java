package com.sigmadevs.testtask.app.service;

import com.sigmadevs.testtask.app.dto.CreateOptionDTO;
import com.sigmadevs.testtask.app.dto.OptionDTO;
import com.sigmadevs.testtask.app.dto.UpdateOptionDTO;
import com.sigmadevs.testtask.app.entity.Option;
import com.sigmadevs.testtask.app.entity.Task;
import com.sigmadevs.testtask.app.exception.OptionNotFoundException;
import com.sigmadevs.testtask.app.exception.TaskNotFoundException;
import com.sigmadevs.testtask.app.mapper.OptionMapper;
import com.sigmadevs.testtask.app.repository.OptionRepository;
import com.sigmadevs.testtask.app.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptionService {

    private final OptionRepository optionRepository;

    private final OptionMapper optionMapper;

    private final TaskRepository taskRepository;

    public OptionDTO createOption(CreateOptionDTO createOptionDTO,Long taskId) {
        log.info("Creating new option with title: {}", createOptionDTO.getTitle());

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.warn("Task with ID {} not found!", taskId);
                    return new TaskNotFoundException("Task with Id " + taskId + " not found!");
                });

        Option option = optionRepository.save(optionMapper.toEntity(createOptionDTO, task));
        log.info("Option created successfully with ID: {}", option);
        return optionMapper.toDTO(option);
    }
    @Transactional
    public OptionDTO updateOption(UpdateOptionDTO updateOptionDTO) {
        log.info("Updating option with ID: {}", updateOptionDTO.getId());

        Option option = optionRepository.findById(updateOptionDTO.getId())
                .orElseThrow(() -> {
                    log.warn("Option with ID {} not found!", updateOptionDTO.getId());
                    return new OptionNotFoundException("Option with Id " + updateOptionDTO.getId() + " not found!");
                });

        option.setTitle(updateOptionDTO.getTitle());
        option.setIsTrue(updateOptionDTO.getIsTrue());

        log.info("Option with ID {} updated successfully", option.getId());
        return optionMapper.toDTO(option);
    }
    public List<OptionDTO> getAllOptions() {
        log.info("Fetching all options...");
        List<Option> options = optionRepository.findAll();
        log.info("Retrieved {} options", options.size());
        return optionMapper.toDTOList(options);
    }

    public OptionDTO getOptionById(Long id) {
        log.info("Fetching option with ID: {}", id);

        Option option = optionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Option with ID {} not found!", id);
                    return new OptionNotFoundException("Option with Id " + id + " not found!");
                });

        log.info("Retrieved option: {}", option);
        return optionMapper.toDTO(option);
    }

    public void removeOptionById(Long id) {
        log.info("Removing option with ID: {}", id);

        if (!optionRepository.existsById(id)) {
            log.warn("Option with ID {} not found!", id);
            throw new OptionNotFoundException("Option with Id " + id + " not found!");
        }

        optionRepository.deleteById(id);
        log.info("Option with ID {} removed successfully", id);
    }

    public void createAllOptions(List<CreateOptionDTO> options, Task task) {
        try{

            List<Option> list = options.stream().map(e -> optionMapper.toEntity(e, task)).toList();
            optionRepository.saveAll(list);
        }catch (Exception e){
            log.error("Error while creating all options", e);
            throw new RuntimeException(e); //TODO
        }
    }
}