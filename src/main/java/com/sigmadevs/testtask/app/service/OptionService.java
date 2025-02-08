package com.sigmadevs.testtask.app.service;

import com.sigmadevs.testtask.app.dto.OptionDTO;
import com.sigmadevs.testtask.app.entity.Option;
import com.sigmadevs.testtask.app.exception.OptionNotFoundException;
import com.sigmadevs.testtask.app.mapper.OptionMapper;
import com.sigmadevs.testtask.app.repository.OptionRepository;
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
    public OptionDTO createOption(OptionDTO optionDTO) {
        log.info("Creating new option: {}", optionDTO);
        Option option = optionRepository.save(optionMapper.toEntity(optionDTO));
        log.info("Option created successfully with ID: {}", option);
        return optionMapper.toDTO(option);
    }

    public List<OptionDTO> getAllOptions() {
        log.info("Fetching all options...");
        List<Option> options = optionRepository.findAll();
        log.info("Retrieved {} options", options.size());
        return optionMapper.toDTOList(options);
    }

    @Transactional
    public OptionDTO updateOption(OptionDTO optionDTO) {
        log.info("Updating option with ID: {}", optionDTO.getId());

        Option option = optionRepository.findById(optionDTO.getId())
                .orElseThrow(() -> {
                    log.warn("Option with ID {} not found!", optionDTO.getId());
                    return new OptionNotFoundException("Option with Id " + optionDTO.getId() + " not found!");
                });

        option.setTitle(optionDTO.getTitle());
        option.setIsTrue(optionDTO.getIsTrue());

        log.info("Option with ID {} updated successfully", option.getId());
        return optionMapper.toDTO(option);
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

}