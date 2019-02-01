import model.Message;
import model.User;
import org.junit.Before;
import org.junit.Test;
import socialnetwork.SocialNetworkController;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class SocialNetworkControllerTest {

    final String user1 = "konrad";
    final String user2 = "roger";
    final String user3 = "adam";
    final String  message1 = "First Message";
    final String  message2 = "Second Message";
    final String  message3 = "Third Message";
    final String  message4 = "Fourth Message";

    private SocialNetworkController controller;

    @Before
    public void setUp() {
        controller = new SocialNetworkController();
        controller.post(user1, message1);
        controller.post(user2, message1);
        controller.post(user1, message2);

        controller.follow(user1, user2);
    }

    @Test
    public void postMessageTest() {
        List<User> users = controller.getAllUsers();
        assertEquals(2, users.size());
        assertEquals(user1, users.get(0).getUserId());
        assertEquals(user2, users.get(1).getUserId());

        assertTrue(users.get(0).getWall().getWallMessages().get(0).getMessage().equals(message1));
        assertEquals(2, users.get(0).getWall().getWallMessages().size());
    }

    @Test
    public void followTest() {
        List<User> users = controller.getAllUsers();

        Optional<User> konrad = users.stream().filter(user -> user.getUserId().equals(user1)).findFirst();
        assertTrue(konrad.isPresent());
        assertTrue(konrad.get().getFollowing().stream().anyMatch(user -> user.getUserId().equals(user2)));

        Optional<User> roger = users.stream().filter(user -> user.getUserId().equals(user2)).findFirst();
        assertTrue(roger.isPresent());
        assertFalse(roger.get().getFollowing().stream().anyMatch(user -> user.getUserId().equals(user1)));
    }

    @Test
    public void getWallTest() throws InterruptedException {
        // Ensure message 3 is sent after message 2 -> message 3 should be latest message and first to display on wall
        Thread.sleep(100);
        controller.post(user1, message3);

        Response response = controller.getWall(user1);
        assertTrue(response.getStatus() == 200);

        List<Message> messageList = (List<Message>) response.getEntity();
        assertTrue(messageList.get(0).getMessage().equals(message3));
    }

    @Test
    public void getTimeline() throws InterruptedException {
        Thread.sleep(100);
        controller.post(user3, message1);
        Thread.sleep(100);
        controller.post(user3, message2);
        Thread.sleep(100);
        controller.post(user3, message3);
        Thread.sleep(100);
        controller.post(user2, message2);
        Thread.sleep(100);
        controller.post(user2, message3);
        Thread.sleep(100);
        controller.post(user3, message4);

        controller.follow(user1, user3);

        Response response = controller.getTimeline(user1);
        List<Message> messageList = (List<Message>) response.getEntity();

        assertEquals( 7, messageList.size());

        assertEquals(user2, messageList.get(6).getUserId());
        assertEquals(message1, messageList.get(6).getMessage());

        // Reverse chronological order, last post is seen first
        assertEquals(user3, messageList.get(0).getUserId());
        assertEquals(message4, messageList.get(0).getMessage());
    }

}
