package com.sigmadevs.testtask.service;

import com.sigmadevs.testtask.app.dto.QuestDTO;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.exception.QuestNotFoundException;
import com.sigmadevs.testtask.app.mapper.QuestMapper;
import com.sigmadevs.testtask.app.repository.QuestRepository;
import com.sigmadevs.testtask.app.service.QuestService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestServiceTest {
    @InjectMocks
    QuestService questService;
    @Mock
    QuestRepository questRepository;
    @Mock
    QuestMapper questMapper;
    Quest quest;
    QuestDTO questDTO;
    @BeforeEach
    void setUp() {

        quest = Quest.builder()
                .title("Test title")
                .build();

        questDTO = QuestDTO.builder()
                .title("Test title")
                .build();

    }

    @Test
    void createQuest_shouldReturnQuestDTO() {

        when(questMapper.toEntity(any(QuestDTO.class)))
                .thenReturn(quest);

        when(questRepository.save(any(Quest.class)))
                .thenReturn(quest);

        when(questMapper.toDTO(any(Quest.class)))
                .thenReturn(questDTO);

        QuestDTO actualQuestDTO = questService.createQuest(questDTO);

        assertNotNull(actualQuestDTO);
        assertEquals(actualQuestDTO, questDTO);

        verify(questMapper).toEntity(any(QuestDTO.class));
        verify(questRepository).save(any(Quest.class));
        verify(questMapper).toDTO(any(Quest.class));
    }

    @Test
    void getAllQuests_shouldReturnQuestDTOList() {

        when(questRepository.findAll())
                .thenReturn(List.of(quest));

        when(questMapper.toDTOList(any(List.class)))
                .thenReturn(List.of(questDTO));

        List<QuestDTO> questDTOS = questService.getAllQuests();

        assertNotNull(questDTOS);
        assertFalse(questDTOS.isEmpty());
        assertEquals(1 , questDTOS.size());

        verify(questRepository).findAll();
        verify(questMapper).toDTOList(any(List.class));
    }

    @Test
    void updateQuest_shouldReturnQuestDTO() {

        questDTO.setTitle("Test title 2");

        when(questRepository.findById(questDTO.getId()))
                .thenReturn(Optional.of(quest));

        when(questMapper.toDTO(any(Quest.class)))
                .thenReturn(questDTO);

        QuestDTO actualQuestDTO = questService.updateQuest(questDTO);

        assertNotNull(actualQuestDTO);
        assertEquals(actualQuestDTO, questDTO);

        verify(questRepository).findById(questDTO.getId());
        verify(questMapper).toDTO(any(Quest.class));
    }

    @Test
    void updateQuest_shouldThrowException_whenQuestNotFound() {

        when(questRepository.findById(questDTO.getId()))
                .thenReturn(Optional.empty());

        QuestNotFoundException exception = assertThrows(QuestNotFoundException.class, () -> questService.updateQuest(questDTO));

        assertEquals("Quest with Id " + questDTO.getId() + " not found!", exception.getMessage());

        verify(questRepository).findById(questDTO.getId());
        verify(questMapper,never()).toDTO(any(Quest.class));
    }

    @Test
    void getQuestById_shouldReturnQuestDTO() {

        long id = 1L;

        when(questRepository.findById(id))
                .thenReturn(Optional.of(quest));

        when(questMapper.toDTO(any(Quest.class)))
                .thenReturn(questDTO);

        QuestDTO actualQuest = questService.getQuestById(id);

        assertNotNull(actualQuest);
        assertEquals(questDTO, actualQuest);

        verify(questRepository).findById(id);
        verify(questMapper).toDTO(any(Quest.class));
    }

    @Test
    void getQuestById_shouldThrowException_whenQuestNotFound() {

        long id = 100L;

        when(questRepository.findById(id))
                .thenReturn(Optional.empty());

        QuestNotFoundException exception = assertThrows(QuestNotFoundException.class, () -> questService.getQuestById(id));

        assertEquals("Quest with Id " + id + " not found!", exception.getMessage());

        verify(questRepository).findById(id);
        verify(questMapper, never()).toDTO(any(Quest.class));
    }

    @Test
    void removeQuestById_shouldDeleteQuest() {

        long id = 1L;

        when(questRepository.existsById(id))
                .thenReturn(true);

        doNothing().when(questRepository).deleteById(id);

        questService.removeQuestById(id);

        verify(questRepository).existsById(id);
        verify(questRepository).deleteById(id);
    }

    @Test
    void removeQuestById_shouldThrowException_whenQuestNotFound() {

        long id = 100L;

        when(questRepository.existsById(id))
                .thenReturn(false);

        QuestNotFoundException exception = assertThrows(QuestNotFoundException.class, () -> questService.removeQuestById(id));

        assertEquals("Quest with Id " + id + " not found!", exception.getMessage());

        verify(questRepository).existsById(id);
        verify(questRepository, never()).deleteById(id);
    }


}
