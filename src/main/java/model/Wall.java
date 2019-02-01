package model;

import java.util.List;

public class Wall {

    private final List<Message> wallMessages;

    public Wall(List<Message> wallMessages) {
        this.wallMessages = wallMessages;
    }

    public void postMessage(Message message) {
        wallMessages.add(message);
    }

    public List<Message> getWallMessages() {
        return wallMessages;
    }
}
