package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.CommentDTO;
import com.sigmadevs.testtask.app.dto.EditCommentDTO;
import com.sigmadevs.testtask.app.dto.WriteCommentDTO;
import com.sigmadevs.testtask.app.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentRestController {

    private final CommentService commentService;
    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDTO writeComment(@RequestBody @Valid WriteCommentDTO writeCommentDTO, Principal principal) {
        return commentService.writeComment(writeCommentDTO, principal);
    }
    @PutMapping("/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentDTO editComment(@RequestBody @Valid EditCommentDTO editCommentDTO, Principal principal) {
        return commentService.editComment(editCommentDTO, principal);
    }
    @GetMapping("/comments/{questId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDTO> getCommentsByQuestId(@PathVariable("questId") Long questId) {
        return  commentService.getCommentsByQuestId(questId);
    }
    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCommentById(@PathVariable("id") Long id, Principal principal) {
        commentService.removeCommentById(id, principal);
    }

}