package com.saksham.guardrail.service;

import com.saksham.guardrail.entity.Post;
import com.saksham.guardrail.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private StringRedisTemplate redis;

	public Post createPost(Post post) {
		Post p = postRepository.save(post);
		String scoreKey = "post:" + p.getId() + ":score";
		redis.opsForValue().set(scoreKey, "0");

		return p;
	}

	public void addViralityPoints(Long postId, int points) {
		String scoreKey = "post:" + postId + ":score";
		redis.opsForValue().increment(scoreKey, points);
	}
}