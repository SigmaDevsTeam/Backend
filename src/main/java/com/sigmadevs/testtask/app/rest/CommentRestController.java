package com.sigmadevs.testtask.app.rest;

import com.sigmadevs.testtask.app.dto.CommentDTO;
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
    public CommentDTO createComment(@RequestBody @Valid CommentDTO commentDTO) {
        return commentService.createComment(commentDTO);
    }
    @PutMapping("/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentDTO updateComment(@RequestBody @Valid CommentDTO commentDTO) {
        return commentService.updateComment(commentDTO);
    }
    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCommentById(@PathVariable("id") Long id) {
        commentService.removeCommentById(id);
    }

}
