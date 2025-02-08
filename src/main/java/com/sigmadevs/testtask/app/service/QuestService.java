package com.sigmadevs.testtask.app.service;

import com.sigmadevs.testtask.app.dto.QuestDTO;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.exception.QuestNotFoundException;
import com.sigmadevs.testtask.app.mapper.QuestMapper;
import com.sigmadevs.testtask.app.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;
    private final QuestMapper questMapper;

    public QuestDTO createQuest(QuestDTO questDTO) {
        log.info("Creating a new quest: {}", questDTO);
        Quest quest = questRepository.save(questMapper.toEntity(questDTO));
        log.info("Quest created successfully with ID: {}", quest.getId());
        return questMapper.toDTO(quest);
    }

    public QuestDTO updateQuest(QuestDTO questDTO) {
        log.info("Updating quest with ID: {}", questDTO.getId());
        Quest quest = questRepository.findById(questDTO.getId())
                .orElseThrow(() -> {
                    log.error("Quest with ID {} not found", questDTO.getId());
                    return new QuestNotFoundException("Quest with Id " + questDTO.getId() + " not found!");
                });

        quest.setTitle(questDTO.getTitle());
        quest.setDescription(questDTO.getDescription());
        quest.setTimeLimit(questDTO.getTimeLimit());

        log.info("Quest with ID {} updated successfully", questDTO.getId());
        return questMapper.toDTO(quest);
    }

    public List<QuestDTO> getAllQuests() {
        log.info("Fetching all quests");
        List<Quest> quests = questRepository.findAll();
        log.info("Retrieved {} quests", quests.size());
        return questMapper.toDTOList(quests);
    }

    public QuestDTO getQuestById(Long id) {
        log.info("Fetching quest with ID: {}", id);
        Quest quest = questRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Quest with ID {} not found", id);
                    return new QuestNotFoundException("Quest with Id " + id + " not found!");
                });
        log.info("Quest with ID {} retrieved successfully", id);
        return questMapper.toDTO(quest);
    }

    public void removeQuestById(long id) {
        log.info("Attempting to delete quest with ID: {}", id);
        if (!questRepository.existsById(id)) {
            log.error("Quest with ID {} not found", id);
            throw new QuestNotFoundException("Quest with Id " + id + " not found!");
        }
        questRepository.deleteById(id);
        log.info("Quest with ID {} deleted successfully", id);
    }
}
