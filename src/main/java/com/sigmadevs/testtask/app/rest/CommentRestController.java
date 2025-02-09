package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.CommentDTO;
import com.sigmadevs.testtask.app.dto.CreateCommentDTO;
import com.sigmadevs.testtask.app.dto.UpdateCommentDTO;
import com.sigmadevs.testtask.app.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentRestController {

    private final CommentService commentService;
    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDTO createComment(@RequestBody @Valid CreateCommentDTO createCommentDTO) {
        return commentService.createComment(createCommentDTO);
    }
    @PutMapping("/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentDTO updateComment(@RequestBody @Valid UpdateCommentDTO updateCommentDTO) {
        return commentService.updateComment(updateCommentDTO);
    }
    @GetMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDTO getCommentById(@PathVariable("id") Long id) {
        return commentService.getCommentById(id);
    }
    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCommentById(@PathVariable("id") Long id) {
        commentService.removeCommentById(id);
    }

}
