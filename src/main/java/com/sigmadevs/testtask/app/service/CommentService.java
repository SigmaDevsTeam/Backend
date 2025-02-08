package com.sigmadevs.testtask.app.service;

import com.sigmadevs.testtask.app.dto.CommentDTO;
import com.sigmadevs.testtask.app.entity.Comment;
import com.sigmadevs.testtask.app.exception.CommentNotFoundException;
import com.sigmadevs.testtask.app.mapper.CommentMapper;
import com.sigmadevs.testtask.app.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentDTO createComment(CommentDTO commentDTO) {
        log.info("Creating comment: {}", commentDTO);
        Comment comment = commentRepository.save(commentMapper.toEntity(commentDTO));
        log.info("Comment created with ID: {}", comment.getId());
        return commentMapper.toDTO(comment);
    }

    @Transactional
    public CommentDTO updateComment(CommentDTO commentDTO) {
        log.info("Updating comment with ID: {}", commentDTO.getId());

        Comment comment = commentRepository.findById(commentDTO.getId())
                .orElseThrow(() -> {
                    log.warn("Comment with ID {} not found!", commentDTO.getId());
                    return new CommentNotFoundException("Comment with Id " + commentDTO.getId() + " not found!");
                });

        comment.setTitle(commentDTO.getTitle());
        log.info("Comment with ID {} updated successfully", comment.getId());

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
