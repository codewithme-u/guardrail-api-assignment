# 🛡️ Guardrail API & Atomic Virality Engine

## 🚀 Overview
This project is a high-performance Spring Boot microservice designed as a central API gateway with strict mathematical guardrails. It manages concurrent bot and human interactions while maintaining distributed state using **Redis** and **PostgreSQL**.

## 🛠️ Tech Stack
* **Java 17+ / Spring Boot 3.x** * **PostgreSQL**: Acts as the source of truth for persistent data (Users, Bots, Posts, Comments).
* **Redis**: Acts as the gatekeeper for atomic locks, virality scores, and notification throttling.
* **Docker**: Used to containerize the infrastructure for seamless local setup.

## 🔐 Phase 2: Atomic Locks & Thread Safety
To handle the "Spam Test" (200 concurrent requests at the same millisecond), I implemented the following strategy to guarantee thread safety without using heavy database locks.

### **1. Horizontal Cap (Bot Reply Limit)**
* **Requirement**: A single post cannot have more than 100 bot replies total.
* **Implementation**: I used the Redis `INCR` command on the key `post:{id}:bot_count`.
* **Thread Safety**: Since `INCR` is an **atomic operation** in Redis, it prevents race conditions where multiple threads might read the same count simultaneously. If the incremented value exceeds 100, the request is instantly rejected with a **429 Too Many Requests**.

### **2. Cooldown Guardrail**
* **Requirement**: A specific Bot cannot interact with a specific Human more than once per 10 minutes.
* **Implementation**: I utilized Redis keys with a **TTL (Time-To-Live)** of 10 minutes (e.g., `cooldown:bot_{id}:human_{id}`).
* **Logic**: If the key `EXISTS` in Redis, the interaction is blocked; otherwise, the interaction proceeds and the key is set.

## 📬 Phase 3: Smart Notification Batching
To prevent user churn from notification spam, notifications are throttled to once every 15 minutes per user.
* **Throttler**: If a user has received a notification in the last 15 minutes, the new notification is pushed into a Redis List: `user:{id}:pending_notifs`.
* **CRON Sweeper**: A Spring `@Scheduled` task runs every 5 minutes to scan for users with pending notifications. It pops all pending messages, counts them, and logs a summarized message to the console.

## 🧪 Testing the API
1.  **Infrastructure**: Ensure Docker is running and execute `docker-compose up -d` to spin up PostgreSQL and Redis.
2.  **Execution**: Run the Spring Boot application locally.
3.  **Postman**: Import the provided `Guardrail API Assignment.postman_collection.json` file.
4.  **Endpoints**: 
    * `POST /api/posts` - Create posts.
    * `POST /api/posts/{postId}/comments` - Add comments (test concurrency here).
    * `POST /api/posts/{postId}/like` - Like posts to update Virality Score.
