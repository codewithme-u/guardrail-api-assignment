package com.saksham.guardrail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class NotificationSweeper {

	@Autowired
	private StringRedisTemplate redis;

	@Scheduled(fixedRate = 300000)
	public void sweepNotifications() {
		Set<String> keys = redis.keys("user:*:pending_notifications");

		if (keys != null) {
			for (String key : keys) {
				Long count = redis.opsForList().size(key);
				if (count != null && count > 0) {
					System.out.println("Summarized Push Notification: Bot activity detected. " + count
							+ " new interactions for user.");

					redis.delete(key);
				}
			}
		}
	}
}