package com.sigmadevs.testtask.service;

import com.sigmadevs.testtask.app.dto.CreateQuestDTO;
import com.sigmadevs.testtask.app.dto.QuestDTO;
import com.sigmadevs.testtask.app.dto.UpdateQuestDTO;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.User;
import com.sigmadevs.testtask.app.exception.QuestNotFoundException;
import com.sigmadevs.testtask.app.mapper.QuestMapper;
import com.sigmadevs.testtask.app.repository.QuestRepository;
import com.sigmadevs.testtask.app.service.QuestService;
import com.sigmadevs.testtask.app.exception.UserNotFoundException;
import com.sigmadevs.testtask.security.repository.UserRepository;
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
    UserRepository userRepository;
    @Mock
    QuestMapper questMapper;
    Quest quest;
    QuestDTO questDTO;
    CreateQuestDTO createQuestDTO;
    UpdateQuestDTO updateQuestDTO;

    @BeforeEach
    void setUp() {

        quest = Quest.builder()
                .title("Test title")
                .build();

        questDTO = QuestDTO.builder()
                .title("Test title")
                .build();

        createQuestDTO = CreateQuestDTO.builder()
                .title("Test title")
                .userId(1L)
                .build();

        updateQuestDTO = UpdateQuestDTO.builder()
                .id(1L)
                .title("Test title 2")
                .build();

    }

    @Test
    void createQuest_shouldReturnQuestDTO() {

        when(userRepository.findById(createQuestDTO.getUserId()))
                .thenReturn(Optional.of(new User()));

        when(questMapper.toEntity(any(CreateQuestDTO.class), any(User.class)))
                .thenReturn(quest);

        when(questRepository.save(any(Quest.class)))
                .thenReturn(quest);

        when(questMapper.toDTO(any(Quest.class)))
                .thenReturn(questDTO);

        QuestDTO actualQuestDTO = questService.createQuest(createQuestDTO);

        assertNotNull(actualQuestDTO);
        assertEquals(actualQuestDTO, questDTO);

        verify(userRepository).findById(createQuestDTO.getUserId());
        verify(questMapper).toEntity(any(CreateQuestDTO.class), any(User.class));
        verify(questRepository).save(any(Quest.class));
        verify(questMapper).toDTO(any(Quest.class));
    }


    @Test
    void createQuest_shouldThrowException_whenUserNotFound() {

        when(userRepository.findById(createQuestDTO.getUserId()))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> questService.createQuest(createQuestDTO));
        assertEquals("User with " + createQuestDTO.getUserId() + " Id not found!", exception.getMessage());

        verify(userRepository).findById(createQuestDTO.getUserId());
        verify(questMapper, never()).toEntity(any(CreateQuestDTO.class), any(User.class));
        verify(questRepository,never()).save(any(Quest.class));
        verify(questMapper, never()).toDTO(any(Quest.class));
    }

    @Test
    void updateQuest_shouldReturnQuestDTO() {

        when(questRepository.findById(updateQuestDTO.getId()))
                .thenReturn(Optional.of(quest));

        when(questMapper.toDTO(any(Quest.class)))
                .thenReturn(questDTO);

        QuestDTO actualQuestDTO = questService.updateQuest(updateQuestDTO);

        assertNotNull(actualQuestDTO);
        assertEquals(actualQuestDTO, questDTO);

        verify(questRepository).findById(updateQuestDTO.getId());
        verify(questMapper).toDTO(any(Quest.class));
    }

    @Test
    void updateQuest_shouldThrowException_whenQuestNotFound() {

        when(questRepository.findById(updateQuestDTO.getId()))
                .thenReturn(Optional.empty());

        QuestNotFoundException exception = assertThrows(QuestNotFoundException.class, () -> questService.updateQuest(updateQuestDTO));

        assertEquals("Quest with Id " + updateQuestDTO.getId() + " not found!", exception.getMessage());

        verify(questRepository).findById(updateQuestDTO.getId());
        verify(questMapper,never()).toDTO(any(Quest.class));
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
    void getQuestById_shouldReturnQuestDTO() {

        long id = 1L;

        when(questRepository.findById(id))
                .thenReturn(Optional.of(quest));

        when(questMapper.toDTO(quest))
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
