package com.saksham.guardrail.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saksham.guardrail.entity.Bot;

public interface BotRepository extends JpaRepository<Bot, Long> {

}
