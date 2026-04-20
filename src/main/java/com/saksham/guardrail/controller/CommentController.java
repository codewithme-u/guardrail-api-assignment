package com.saksham.guardrail.controller;

import com.saksham.guardrail.entity.Comment;
import com.saksham.guardrail.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@PostMapping("/{postId}/comments")
	public ResponseEntity<?> addComment(@PathVariable Long postId, @RequestBody Comment comment,
			@RequestParam(defaultValue = "false") boolean isBot, @RequestParam(required = false) Long humanId) {

		try {
			comment.setPostId(postId);

			Comment savedComment = commentService.addComment(comment, isBot, humanId);

			return ResponseEntity.ok(savedComment);
		} catch (RuntimeException e) {
			if (e.getMessage().contains("Horizontal Cap")) {
				return ResponseEntity.status(429).body(e.getMessage());
			}
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}