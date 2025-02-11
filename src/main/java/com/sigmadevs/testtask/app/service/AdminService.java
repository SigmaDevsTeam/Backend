package com.sigmadevs.testtask.app.service;

import com.sigmadevs.testtask.app.dto.QuestDTO;
import com.sigmadevs.testtask.app.dto.UserDTO;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.User;
import com.sigmadevs.testtask.app.exception.QuestNotFoundException;
import com.sigmadevs.testtask.app.mapper.MainUserMapper;
import com.sigmadevs.testtask.app.mapper.QuestMapper;
import com.sigmadevs.testtask.app.repository.QuestRepository;
import com.sigmadevs.testtask.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final QuestRepository questRepository;

    private final QuestMapper questMapper;

    private final MainUserMapper mainUserMapper;


    public Page<UserDTO> getAllUsers(int page, int size) {
        log.info("Fetching users with pagination: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(mainUserMapper::toDTO);
    }

    public Page<QuestDTO> getAllQuests(int page, int size) {
        log.info("Fetching quests with pagination: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Quest> questPage = questRepository.findAll(pageable);
        return questPage.map(questMapper::toDTO);
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