package com.sigmadevs.testtask.app.service;

import com.sigmadevs.testtask.app.dto.CommentDTO;
import com.sigmadevs.testtask.app.dto.WriteCommentDTO;
import com.sigmadevs.testtask.app.dto.EditCommentDTO;
import com.sigmadevs.testtask.app.entity.Comment;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.User;
import com.sigmadevs.testtask.app.exception.CommentNotFoundException;
import com.sigmadevs.testtask.app.exception.QuestNotFoundException;
import com.sigmadevs.testtask.app.exception.UserNotFoundException;
import com.sigmadevs.testtask.app.mapper.CommentMapper;
import com.sigmadevs.testtask.app.repository.CommentRepository;
import com.sigmadevs.testtask.app.repository.QuestRepository;
import com.sigmadevs.testtask.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final QuestRepository questRepository;
    private final CommentMapper commentMapper;

    public CommentDTO writeComment(WriteCommentDTO writeCommentDTO, Principal principal) {
        log.info("Creating comment with title: {}", writeCommentDTO);

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> {
                    log.error("User with login {} not found", principal.getName());
                    return new UserNotFoundException("User with " +  principal.getName() + " Id not found!");
                });

        Quest quest = questRepository.findById(writeCommentDTO.getQuestId())
                .orElseThrow(() -> {
                    log.error("Quest with ID {} not found", writeCommentDTO.getQuestId());
                    return new QuestNotFoundException("Quest with Id " + writeCommentDTO.getQuestId() + " not found!");
                });
        

        Comment comment = commentRepository.save(commentMapper.toEntity(writeCommentDTO, user, quest));
        log.info("Comment created with ID: {}", comment.getId());
        return commentMapper.toDTO(comment);
    }

    @Transactional
    public CommentDTO editComment(EditCommentDTO editCommentDTO, Principal principal) {
        log.info("Updating comment with ID: {}", editCommentDTO.getId());

        Comment comment = commentRepository.findById(editCommentDTO.getId())
                .orElseThrow(() -> {
                    log.warn("Comment with ID {} not found!", editCommentDTO.getId());
                    return new CommentNotFoundException("Comment with Id " + editCommentDTO.getId() + " not found!");
                });

        String currentUsername = principal.getName();

        if (!comment.getUser().getUsername().equals(currentUsername)) {
            log.warn("User {} is not the owner of comment ID {}", currentUsername, comment.getId());
            throw new AccessDeniedException("You are not allowed to edit this comment");
        }

        comment.setTitle(editCommentDTO.getTitle());
        log.info("Comment with ID {} updated successfully", comment.getId());

        return commentMapper.toDTO(comment);
    }

    public List<CommentDTO> getCommentsByQuestId(Long questId) {
        log.info("Fetching comments for quest ID: {}", questId);

        List<CommentDTO> comments = commentRepository.findAll().stream()
                .filter(comment -> comment.getQuest().getId().equals(questId))
                .map(commentMapper::toDTO)
                .collect(Collectors.toList());

        log.info("Found {} comments for quest ID: {}", comments.size(), questId);

        return comments;
    }

    public void removeCommentById(Long id, Principal principal) {
        log.info("Removing comment with ID: {}", id);

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Comment with ID {} not found!", id);
                    return new CommentNotFoundException("Comment with Id " + id + " not found!");
                });

        String currentUsername = principal.getName();

        if (!comment.getUser().getUsername().equals(currentUsername)) {
            log.warn("User {} is not the owner of comment ID {}", currentUsername, id);
            throw new AccessDeniedException("You are not allowed to delete this comment");
        }

        commentRepository.deleteById(id);
        log.info("Comment with ID {} removed successfully by {}", id, currentUsername);
    }

}
