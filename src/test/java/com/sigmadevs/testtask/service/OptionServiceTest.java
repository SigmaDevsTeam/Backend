package com.sigmadevs.testtask.service;

import com.sigmadevs.testtask.app.dto.OptionDTO;
import com.sigmadevs.testtask.app.dto.TaskDTO;
import com.sigmadevs.testtask.app.entity.Option;
import com.sigmadevs.testtask.app.entity.Task;
import com.sigmadevs.testtask.app.exception.OptionNotFoundException;
import com.sigmadevs.testtask.app.mapper.OptionMapper;
import com.sigmadevs.testtask.app.repository.OptionRepository;
import com.sigmadevs.testtask.app.service.OptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OptionServiceTest {
    @InjectMocks
    OptionService optionService;
    @Mock
    OptionRepository optionRepository;
    @Mock
    OptionMapper optionMapper;
    Option option;
    OptionDTO optionDTO;
    @BeforeEach
    void setUp() {

        TaskDTO taskDTO = TaskDTO.builder()
                .title("Test title")
                .build();

        Task task = Task.builder()
                .title("Test title")
                .build();

        optionDTO = OptionDTO.builder()
                .title("Test title")
                .isTrue(true)
                .taskDTO(taskDTO)
                .build();

        option = Option.builder()
                .title("Test title")
                .isTrue(true)
                .task(task)
                .build();

    }

    @Test
    void createOption_shouldReturnOptionDTO() {

        when(optionMapper.toEntity(any(OptionDTO.class)))
                .thenReturn(option);

        when(optionRepository.save(any(Option.class)))
                .thenReturn(option);

        when(optionMapper.toDTO(any(Option.class)))
                .thenReturn(optionDTO);

        OptionDTO actualOptionDTO = optionService.createOption(optionDTO);

        assertNotNull(actualOptionDTO);
        assertEquals(actualOptionDTO, optionDTO);

        verify(optionMapper).toEntity(any(OptionDTO.class));
        verify(optionRepository).save(any(Option.class));
        verify(optionMapper).toDTO(any(Option.class));
    }

    @Test
    void getAllOptions_shouldReturnOptionDTOList() {

        when(optionRepository.findAll())
                .thenReturn(List.of(option));

        when(optionMapper.toDTOList(any(List.class)))
                .thenReturn(List.of(option));

        List<OptionDTO> optionDTOS = optionService.getAllOptions();

        assertNotNull(optionDTOS);
        assertFalse(optionDTOS.isEmpty());
        assertEquals(1 , optionDTOS.size());

        verify(optionRepository).findAll();
        verify(optionMapper).toDTOList(any(List.class));

    }

    @Test
    void updateOption_shouldReturnOptionDTO() {

        optionDTO.setTitle("Test title 2");

        when(optionRepository.findById(optionDTO.getId()))
                .thenReturn(Optional.of(option));

        when(optionMapper.toDTO(any(Option.class)))
                .thenReturn(optionDTO);

        OptionDTO actualOptionDTO = optionService.updateOption(optionDTO);

        assertNotNull(actualOptionDTO);
        assertEquals(actualOptionDTO, optionDTO);

        verify(optionRepository).findById(optionDTO.getId());
        verify(optionMapper).toDTO(any(Option.class));
    }

    @Test
    void updateOption_shouldThrowException_whenOptionNotFound() {

        when(optionRepository.findById(optionDTO.getId()))
                .thenReturn(Optional.empty());

        OptionNotFoundException exception = assertThrows(OptionNotFoundException.class, () -> optionService.updateOption(optionDTO));

        assertEquals("Option with Id " + optionDTO.getId() + " not found!", exception.getMessage());

        verify(optionRepository).findById(optionDTO.getId());
        verify(optionMapper,never()).toDTO(any(Option.class));
    }

    @Test
    void getOptionById_shouldReturnOptionDTO() {

        long id = 1L;

        when(optionRepository.findById(id))
                .thenReturn(Optional.of(option));

        when(optionMapper.toDTO(any(Option.class)))
                .thenReturn(optionDTO);

        OptionDTO actualOption = optionService.getOptionById(id);

        assertNotNull(actualOption);
        assertEquals(optionDTO,actualOption);

        verify(optionRepository).findById(id);
        verify(optionMapper).toDTO(any(Option.class));

    }
    @Test
    void getOptionById_shouldThrowException_whenOptionNotFound() {

        long id = 100L;

        when(optionRepository.findById(id))
                .thenReturn(Optional.empty());

        OptionNotFoundException exception = assertThrows(OptionNotFoundException.class, () -> optionService.getOptionById(id));

        assertEquals("Option with Id " + id + " not found!", exception.getMessage());

        verify(optionRepository).findById(id);
        verify(optionMapper,never()).toDTO(any(Option.class));
    }
    @Test
    void removeOptionById_shouldDeleteOption() {

        long id = 1L;

        when(optionRepository.existsById(id))
                .thenReturn(true);

        doNothing().when(optionRepository).deleteById(id);

        optionService.removeOptionById(id);

        verify(optionRepository).existsById(id);
        verify(optionRepository).deleteById(id);
    }
    @Test
    void removeOptionById_shouldThrowException_whenOptionNotFound() {

        long id = 100L;

        when(optionRepository.existsById(id))
                .thenReturn(false);

        OptionNotFoundException exception = assertThrows(OptionNotFoundException.class, () -> optionService.removeOptionById(id));

        assertEquals("Option with Id " + id + " not found!", exception.getMessage());

        verify(optionRepository).existsById(id);
        verify(optionRepository,never()).deleteById(id);
    }

}
