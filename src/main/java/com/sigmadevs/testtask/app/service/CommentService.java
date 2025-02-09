package com.sigmadevs.testtask.app.service;

import com.sigmadevs.testtask.app.dto.CommentDTO;
import com.sigmadevs.testtask.app.dto.CreateCommentDTO;
import com.sigmadevs.testtask.app.dto.QuestDTO;
import com.sigmadevs.testtask.app.dto.UpdateCommentDTO;
import com.sigmadevs.testtask.app.entity.Comment;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.User;
import com.sigmadevs.testtask.app.exception.CommentNotFoundException;
import com.sigmadevs.testtask.app.exception.QuestNotFoundException;
import com.sigmadevs.testtask.app.mapper.CommentMapper;
import com.sigmadevs.testtask.app.repository.CommentRepository;
import com.sigmadevs.testtask.app.repository.QuestRepository;
import com.sigmadevs.testtask.security.exception.UserNotFoundException;
import com.sigmadevs.testtask.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final CommentMapper commentMapper;

    public CommentDTO createComment(CreateCommentDTO createCommentDTO) {
        log.info("Creating comment with title: {}", createCommentDTO);

        User user = userRepository.findById(createCommentDTO.getUserId())
                .orElseThrow(() -> {
                    log.error("User with ID {} not found", createCommentDTO.getUserId());
                    return new UserNotFoundException("User with " + createCommentDTO.getUserId() + " Id not found!");
                });

        Quest quest = questRepository.findById(createCommentDTO.getQuestId())
                .orElseThrow(() -> {
                    log.error("Quest with ID {} not found",createCommentDTO.getQuestId());
                    return new QuestNotFoundException("Quest with Id " + createCommentDTO.getQuestId() + " not found!");
                });

        Comment comment = commentRepository.save(commentMapper.toEntity(createCommentDTO, user, quest));
        log.info("Comment created with ID: {}", comment.getId());
        return commentMapper.toDTO(comment);
    }

    @Transactional
    public CommentDTO updateComment(UpdateCommentDTO updateCommentDTO) {
        log.info("Updating comment with ID: {}", updateCommentDTO.getId());

        Comment comment = commentRepository.findById(updateCommentDTO.getId())
                .orElseThrow(() -> {
                    log.warn("Comment with ID {} not found!", updateCommentDTO.getId());
                    return new CommentNotFoundException("Comment with Id " + updateCommentDTO.getId() + " not found!");
                });

        comment.setTitle(updateCommentDTO.getTitle());
        log.info("Comment with ID {} updated successfully", comment.getId());

        return commentMapper.toDTO(comment);
    }

    public CommentDTO getCommentById(Long id) {
        log.info("Fetching comment with ID: {}", id);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Comment with ID {} not found", id);
                    return new CommentNotFoundException("Comment with Id " + id + " not found!");
                });
        log.info("Comment with ID {} retrieved successfully", id);
        return commentMapper.toDTO(comment);
    }

    public void removeCommentById(long id) {
        log.info("Removing comment with ID: {}", id);

        if (!commentRepository.existsById(id)) {
            log.warn("Comment with ID {} not found!", id);
            throw new CommentNotFoundException("Comment with Id " + id + " not found!");
        }

        commentRepository.deleteById(id);
        log.info("Comment with ID {} removed successfully", id);
    }
}
