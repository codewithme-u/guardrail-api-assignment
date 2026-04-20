package com.saksham.guardrail.controller;

import com.saksham.guardrail.entity.Post;
import com.saksham.guardrail.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	@Autowired
	private PostService postService;

	@PostMapping
	public ResponseEntity<Post> createPost(@RequestBody Post post) {
		Post savedPost = postService.createPost(post);
		return ResponseEntity.ok(savedPost);
	}

	@PostMapping("/{postId}/like")
	public ResponseEntity<String> likePost(@PathVariable Long postId) {
		postService.addViralityPoints(postId, 20);
		return ResponseEntity.ok("Post liked! Virality score updated.");
	}
}