package com.sigmadevs.testtask.service;

import com.sigmadevs.testtask.app.dto.CommentDTO;
import com.sigmadevs.testtask.app.dto.CreateCommentDTO;
import com.sigmadevs.testtask.app.dto.UpdateCommentDTO;
import com.sigmadevs.testtask.app.entity.Comment;
import com.sigmadevs.testtask.app.entity.Quest;
import com.sigmadevs.testtask.app.entity.User;
import com.sigmadevs.testtask.app.exception.CommentNotFoundException;
import com.sigmadevs.testtask.app.exception.QuestNotFoundException;
import com.sigmadevs.testtask.app.mapper.CommentMapper;
import com.sigmadevs.testtask.app.repository.CommentRepository;
import com.sigmadevs.testtask.app.repository.QuestRepository;
import com.sigmadevs.testtask.app.service.CommentService;
import com.sigmadevs.testtask.app.exception.UserNotFoundException;
import com.sigmadevs.testtask.security.repository.UserRepository;
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
    CommentRepository commentRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    QuestRepository questRepository;
    @Mock
    CommentMapper commentMapper;
    @InjectMocks
    CommentService commentService;
    Comment comment;
    CommentDTO commentDTO;
    CreateCommentDTO createCommentDTO;
    UpdateCommentDTO updateCommentDTO;

    @BeforeEach
    void setUp() {

        comment = Comment.builder()
                .id(1L)
                .title("Test title")
                .build();

        commentDTO = CommentDTO.builder()
                .title("Test title")
                .build();

        createCommentDTO = CreateCommentDTO.builder()
                .title("Test title")
                .questId(1L)
                .userId(1L)
                .build();

        updateCommentDTO = UpdateCommentDTO.builder()
                .id(1L)
                .title("Test title")
                .build();

    }

    @Test
    void createComment_shouldReturnCommentDTO() {

        when(userRepository.findById(createCommentDTO.getUserId()))
                .thenReturn(Optional.of(new User()));

        when(questRepository.findById(createCommentDTO.getUserId()))
                .thenReturn(Optional.of(new Quest()));

        when(commentMapper.toEntity(any(CreateCommentDTO.class), any(User.class), any(Quest.class)))
                .thenReturn(comment);

        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);

        when(commentMapper.toDTO(any(Comment.class))).
                thenReturn(commentDTO);

        CommentDTO result = commentService.createComment(createCommentDTO);

        assertNotNull(result);
        assertEquals(commentDTO.getId(), result.getId());

        verify(userRepository).findById(createCommentDTO.getUserId());
        verify(questRepository).findById(createCommentDTO.getQuestId());
        verify(commentMapper).toEntity(any(CreateCommentDTO.class), any(User.class), any(Quest.class));
        verify(commentRepository).save(any(Comment.class));
        verify(commentMapper).toDTO(any(Comment.class));
    }

    @Test
    void createComment_shouldThrowException_whenUserNotFound() {

        when(userRepository.findById(createCommentDTO.getUserId()))
                .thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> commentService.createComment(createCommentDTO));
        assertEquals("User with " + createCommentDTO.getUserId() + " Id not found!", exception.getMessage());

        verify(userRepository).findById(createCommentDTO.getUserId());
        verify(commentMapper, never()).toEntity(any(CreateCommentDTO.class), any(User.class), any(Quest.class));
        verify(questRepository, never()).findById(createCommentDTO.getQuestId());
        verify(commentRepository, never()).save(any(Comment.class));
        verify(commentMapper,never()).toDTO(any(Comment.class));
    }
    @Test
    void createComment_shouldThrowException_whenQuestNotFound() {

        when(userRepository.findById(createCommentDTO.getUserId()))
                .thenReturn(Optional.of(new User()));

        when(questRepository.findById(createCommentDTO.getQuestId()))
                .thenReturn(Optional.empty());

        QuestNotFoundException exception = assertThrows(QuestNotFoundException.class, () -> commentService.createComment(createCommentDTO));

        assertEquals("Quest with Id " +createCommentDTO.getQuestId() + " not found!", exception.getMessage());

        verify(userRepository).findById(createCommentDTO.getUserId());
        verify(questRepository).findById(createCommentDTO.getQuestId());
        verify(commentMapper, never()).toEntity(any(CreateCommentDTO.class), any(User.class), any(Quest.class));
        verify(commentRepository, never()).save(any(Comment.class));
        verify(commentMapper,never()).toDTO(any(Comment.class));
    }

    @Test
    void updateComment_shouldReturnUpdatedCommentDTO() {

        when(commentRepository.findById(updateCommentDTO.getId()))
                .thenReturn(Optional.of(comment));

        when(commentMapper.toDTO(any(Comment.class)))
                .thenReturn(commentDTO);

        CommentDTO result = commentService.updateComment(updateCommentDTO);

        assertNotNull(result);
        assertEquals(commentDTO.getTitle(), result.getTitle());

        verify(commentRepository).findById(updateCommentDTO.getId());
        verify(commentMapper).toDTO(any(Comment.class));

    }

    @Test
    void updateComment_shouldThrowException_whenCommentDoesNotExist() {

        when(commentRepository.findById(updateCommentDTO.getId()))
                .thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.updateComment(updateCommentDTO));

        verify(commentRepository).findById(updateCommentDTO.getId());
        verify(commentMapper, never()).toDTO(any(Comment.class));

    }

    @Test
    void getCommentById_shouldReturnCommentDTO() {

        long id = 1L;

        when(commentRepository.findById(id))
                .thenReturn(Optional.of(comment));

        when(commentMapper.toDTO(comment))
                .thenReturn(commentDTO);

        CommentDTO actualComment = commentService.getCommentById(id);

        assertNotNull(actualComment);
        assertEquals(commentDTO, actualComment);

        verify(commentRepository).findById(id);
        verify(commentMapper).toDTO(any(Comment.class));
    }

    @Test
    void getCommentById_shouldThrowException_whenCommentNotFound() {

        long id = 100L;

        when(commentRepository.findById(id))
                .thenReturn(Optional.empty());

        CommentNotFoundException exception = assertThrows(CommentNotFoundException.class, () -> commentService.getCommentById(id));

        assertEquals("Comment with Id " + id + " not found!", exception.getMessage());

        verify(commentRepository).findById(id);
        verify(commentMapper, never()).toDTO(any(Comment.class));
    }

    @Test
    void removeCommentById_shouldRemoveComment_whenCommentExists() {

        Long id = 1L;

        when(commentRepository.existsById(id)).thenReturn(true);

        commentService.removeCommentById(id);

        verify(commentRepository).existsById(id);
        verify(commentRepository).deleteById(id);

    }

    @Test
    void removeCommentById_shouldThrowException_whenCommentDoesNotExist() {

        Long id = 1L;

        when(commentRepository.existsById(id))
                .thenReturn(false);

        assertThrows(CommentNotFoundException.class, () -> commentService.removeCommentById(id));

        verify(commentRepository).existsById(id);
        verify(commentRepository, never()).deleteById(id);

    }
}
