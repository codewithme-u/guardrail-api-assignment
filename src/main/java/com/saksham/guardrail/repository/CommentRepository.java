package com.saksham.guardrail.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saksham.guardrail.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
