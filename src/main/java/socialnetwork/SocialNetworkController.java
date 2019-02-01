package socialnetwork;

import model.Message;
import model.User;
import model.Wall;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@RestController
public class SocialNetworkController {

    private final List<User> allUsers = new CopyOnWriteArrayList<>();

    @RequestMapping(value = "/post/user/{userId}", method = RequestMethod.POST)
    public Response post(@PathVariable String userId, @RequestBody String post) {
        if (post.length() > 140) {
            return Response.status(406).entity("Message exceeds 140 characters").build();
        }

        User user = addUserIfDoesNotExist(userId);

        Message message = new Message(user.getUserId(), post, LocalDateTime.now());

        user.getWall().postMessage(message);

        return Response.ok().build();
    }


    @RequestMapping(value = "/follow/user/{userId}/following/{followUserId}", method = RequestMethod.POST)
    public Response follow(@PathVariable String userId, @PathVariable String followUserId) {
        Optional<User> user = getUser(userId);

        Optional<User> follower = getUser(followUserId);

        if (user.isPresent() && follower.isPresent()) {
            user.get().followUser(follower.get());

            return Response.ok().build();
        }
        return Response.status(404).entity("User not found").build();
    }

    @RequestMapping(value = "/wall/user/{userId}", method = RequestMethod.GET, produces = "application/json")
    public Response getWall(@PathVariable String userId) {
        Optional<User> user = getUser(userId);

        if (user.isPresent()) {
            List<Message> wallMessages = user.get().getWall().getWallMessages();

            wallMessages.sort(Comparator.comparing(Message::getPostTime).reversed());

            return Response.ok(wallMessages).build();
        }

        return Response.status(404).entity("User not found").build();
    }

    @RequestMapping(value = "/timeline/user/{userId}", method = RequestMethod.GET, produces = "application/json")
    public Response getTimeline(@PathVariable String userId) {
        Optional<User> user = getUser(userId);

        if (user.isPresent()) {
            List<Message> timelinePosts = user.get().getFollowing().stream()
                    .flatMap(followers -> followers.getWall().getWallMessages().stream())
                    .sorted(Comparator.comparing(Message::getPostTime).reversed())
                    .collect(Collectors.toList());

            return Response.ok(timelinePosts).build();
        }

        return Response.status(404).entity("User not found").build();
    }

    public List<User> getAllUsers() {
        return allUsers;
    }

    private User addUserIfDoesNotExist(@PathVariable String userId) {
        Optional<User> user = getUser(userId);

        if (!user.isPresent()) {
            User newUser = new User(userId, new Wall(new ArrayList<>()), new ArrayList<>());

            getAllUsers().add(newUser);

            return newUser;
        }

        return user.get();
    }

    private Optional<User> getUser(String userId) {
        return getAllUsers().stream()
                .filter(user -> user.getUserId().equals(userId))
                .findFirst();
    }

}