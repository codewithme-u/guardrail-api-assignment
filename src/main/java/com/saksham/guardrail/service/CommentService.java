package com.saksham.guardrail.service;

import com.saksham.guardrail.entity.Comment;
import com.saksham.guardrail.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepo;

	@Autowired
	private StringRedisTemplate redis;

	@Autowired
	private PostService postService;

	public Comment addComment(Comment comment, boolean isBot, Long humanId) {
		if (comment.getDepthLevel() > 20) {
			throw new RuntimeException("Thread too deep! Max 20 levels.");
		}

		if (isBot) {
			String cooldownKey = "cooldown:bot" + comment.getAuthorId() + ":human" + humanId;
			Boolean canInteract = redis.opsForValue().setIfAbsent(cooldownKey, "active", Duration.ofMinutes(10));

			if (Boolean.FALSE.equals(canInteract)) {
				throw new RuntimeException("Bot is on cooldown for this user!");
			}

			String botCountKey = "post:" + comment.getPostId() + ":bot_count";
			Long currentCount = redis.opsForValue().increment(botCountKey);

			if (currentCount != null && currentCount > 100) {
				throw new RuntimeException("Horizontal Cap Reached: Max 100 bot replies.");
			}
		}

		Comment savedComment = commentRepo.save(comment);

		int points = isBot ? 1 : 50;
		postService.addViralityPoints(comment.getPostId(), points);

		return savedComment;
	}
}