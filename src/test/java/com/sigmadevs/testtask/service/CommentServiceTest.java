package com.sigmadevs.testtask.service;

import com.sigmadevs.testtask.app.dto.CommentDTO;
import com.sigmadevs.testtask.app.entity.Comment;
import com.sigmadevs.testtask.app.exception.CommentNotFoundException;
import com.sigmadevs.testtask.app.mapper.CommentMapper;
import com.sigmadevs.testtask.app.repository.CommentRepository;
import com.sigmadevs.testtask.app.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    private CommentDTO commentDTO;
    private Comment comment;

    @BeforeEach
    void setUp() {
        commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setTitle("Test Comment");

        comment = new Comment();
        comment.setId(1L);
        comment.setTitle("Test Comment");
    }

    @Test
    void createComment_shouldReturnCommentDTO_whenValidDataProvided() {
        when(commentMapper.toEntity(any(CommentDTO.class))).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(commentMapper.toDTO(any(Comment.class))).thenReturn(commentDTO);

        CommentDTO result = commentService.createComment(commentDTO);

        assertNotNull(result);
        assertEquals(commentDTO.getId(), result.getId());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void updateComment_shouldReturnUpdatedCommentDTO_whenCommentExists() {
        when(commentRepository.findById(commentDTO.getId())).thenReturn(Optional.of(comment));
        when(commentMapper.toDTO(any(Comment.class))).thenReturn(commentDTO);

        CommentDTO result = commentService.updateComment(commentDTO);

        assertNotNull(result);
        assertEquals(commentDTO.getTitle(), result.getTitle());
        verify(commentRepository, times(1)).findById(commentDTO.getId());
    }

    @Test
    void updateComment_shouldThrowException_whenCommentDoesNotExist() {
        when(commentRepository.findById(commentDTO.getId())).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.updateComment(commentDTO));
        verify(commentRepository, times(1)).findById(commentDTO.getId());
    }

    @Test
    void removeCommentById_shouldRemoveComment_whenCommentExists() {
        when(commentRepository.existsById(commentDTO.getId())).thenReturn(true);

        commentService.removeCommentById(commentDTO.getId());

        verify(commentRepository, times(1)).deleteById(commentDTO.getId());
    }

    @Test
    void removeCommentById_shouldThrowException_whenCommentDoesNotExist() {
        when(commentRepository.existsById(commentDTO.getId())).thenReturn(false);

        assertThrows(CommentNotFoundException.class, () -> commentService.removeCommentById(commentDTO.getId()));
        verify(commentRepository, times(1)).existsById(commentDTO.getId());
    }
}
