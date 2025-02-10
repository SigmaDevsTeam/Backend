package com.sigmadevs.testtask.app.service;

import com.sigmadevs.testtask.app.dto.CreateQuestDTO;
import com.sigmadevs.testtask.app.dto.QuestDTO;
import com.sigmadevs.testtask.app.dto.UpdateQuestDTO;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.User;
import com.sigmadevs.testtask.app.exception.QuestNotFoundException;
import com.sigmadevs.testtask.app.mapper.QuestMapper;
import com.sigmadevs.testtask.app.repository.QuestRepository;
import com.sigmadevs.testtask.security.exception.UserNotFoundException;
import com.sigmadevs.testtask.security.repository.UserRepository;
import com.sigmadevs.testtask.security.service.ImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;

    private final UserRepository userRepository;

    private final ImageService imageService;

    private final QuestMapper questMapper;

    public QuestDTO createQuest(CreateQuestDTO createQuestDTO, MultipartFile image) {
        log.info("Creating a new quest with title: {}", createQuestDTO.getTitle());

        User user = userRepository.findById(createQuestDTO.getUserId())
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", createQuestDTO.getUserId());
                    return new UserNotFoundException("User with " + createQuestDTO.getUserId() + " Id not found!");
                });
        String imageUrl = imageService.uploadImage("quest", image);
        createQuestDTO.setImage(imageUrl);
        Quest quest = questRepository.save(questMapper.toEntity(createQuestDTO, user));
        log.info("Quest created successfully with ID: {}", quest.getId());
        return questMapper.toDTO(quest);
    }

    @Transactional
    public QuestDTO updateQuest(UpdateQuestDTO updateQuestDTO,MultipartFile image) {
        log.info("Updating quest with ID: {}", updateQuestDTO.getId());
        Quest quest = questRepository.findById(updateQuestDTO.getId())
                .orElseThrow(() -> {
                    log.error("Quest with ID {} not found", updateQuestDTO.getId());
                    return new QuestNotFoundException("Quest with Id " + updateQuestDTO.getId() + " not found!");
                });

        quest.setTitle(updateQuestDTO.getTitle());
        quest.setDescription(updateQuestDTO.getDescription());
//        quest.setImage(updateQuestDTO.getImage());
        if (image!=null) {
            String url = imageService.updateImage(quest.getImage(), image);
            quest.setImage(url);
        }else quest.setImage(null);
        quest.setTimeLimit(updateQuestDTO.getTimeLimit());

        log.info("Quest with ID {} updated successfully", updateQuestDTO.getId());
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
