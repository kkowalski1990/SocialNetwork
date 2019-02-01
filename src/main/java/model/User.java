package model;

import java.util.List;

public class User {

    private final String userId;
    private final Wall wall;
    private final List<User> following;

    public User(String userId, Wall wall, List<User> following) {
        this.userId = userId;
        this.wall = wall;
        this.following = following;
    }

    public String getUserId() {
        return userId;
    }

    public Wall getWall() {
        return wall;
    }

    public List<User> getFollowing() {
        return following;
    }

    public void followUser(User user) {
        following.add(user);
    }
}
