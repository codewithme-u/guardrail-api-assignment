package com.saksham.guardrail.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saksham.guardrail.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
