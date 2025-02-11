package com.sigmadevs.testtask.app.service;

import com.sigmadevs.testtask.app.dto.CreateQuestDTO;
import com.sigmadevs.testtask.app.dto.QuestDTO;
import com.sigmadevs.testtask.app.dto.UpdateQuestDTO;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.User;
import com.sigmadevs.testtask.app.exception.QuestNotFoundException;
import com.sigmadevs.testtask.app.mapper.QuestMapper;
import com.sigmadevs.testtask.app.repository.QuestRepository;
import com.sigmadevs.testtask.app.exception.UserNotFoundException;
import com.sigmadevs.testtask.security.repository.UserRepository;
import com.sigmadevs.testtask.security.service.ImageService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;

    private final UserRepository userRepository;

    private final ImageService imageService;

    private final QuestMapper questMapper;

    public QuestDTO createQuest(CreateQuestDTO createQuestDTO, MultipartFile image,Principal principal) {
        log.info("Creating a new quest with title: {}", createQuestDTO.getTitle());
        User user1 = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new UserNotFoundException(principal.getName()));

        String imageUrl = imageService.uploadImage("quest", image);
        createQuestDTO.setImage(imageUrl);
        Quest quest = questRepository.save(questMapper.toEntity(createQuestDTO, user1));
        log.info("Quest created successfully with ID: {}", quest.getId());
        return questMapper.toDTO(quest);
    }

    @Transactional
    public QuestDTO updateQuest(UpdateQuestDTO updateQuestDTO,MultipartFile image,Principal principal) {
        log.info("Updating quest with ID: {}", updateQuestDTO.getId());
        Quest quest = questRepository.findById(updateQuestDTO.getId())
                .orElseThrow(() -> {
                    log.error("Quest with ID {} not found", updateQuestDTO.getId());
                    return new QuestNotFoundException("Quest with Id " + updateQuestDTO.getId() + " not found!");
                });
        if (quest.getUser().getUsername().equals(principal.getName())) {

            quest.setTitle(updateQuestDTO.getTitle()!=null?updateQuestDTO.getTitle():quest.getTitle());
            quest.setDescription(updateQuestDTO.getDescription());
            if (image!=null) {
                String url = imageService.updateImage(quest.getImage(), image);
                quest.setImage(url);
            }
            quest.setTimeLimit(updateQuestDTO.getTimeLimit());

            log.info("Quest with ID {} updated successfully", updateQuestDTO.getId());
            return questMapper.toDTO(quest);
        }else {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes())
                    .getResponse();
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            try {
                response.getWriter().write("Forbidden");
                return null;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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

    public void removeQuestById(long id, Principal principal) {
        log.info("Attempting to delete quest with ID: {}", id);
        Quest quest = questRepository.findById(id).orElseThrow(() -> new QuestNotFoundException("Quest with Id " + id + " not found!"));
        if (quest.getUser().getUsername().equals(principal.getName())) {
            questRepository.deleteById(id);
            log.info("Quest with ID {} deleted successfully", id);
        }else {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes())
                .getResponse();
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            try {
                response.getWriter().write("Forbidden");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
