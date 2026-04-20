package com.saksham.guardrail.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saksham.guardrail.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
