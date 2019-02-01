package model;

import java.time.LocalDateTime;

public class Message {

    private final String userId;
    private final String message;
    private final LocalDateTime postTime;

    public Message(String userId, String message, LocalDateTime postTime) {
        this.userId = userId;
        this.message = message;
        this.postTime = postTime;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getPostTime() {
        return postTime;
    }

    public String getUserId() {
        return userId;
    }
}
