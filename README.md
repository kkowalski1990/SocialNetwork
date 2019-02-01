# SocialNetwork

- To start my SocialNetwork run the main method in ApplicationStarter

- To post a message on a wall you will need to hit the rest endpoint: http://localhost:8080/post/user/{userId} and POST a message.
If a user does not exist they will be created automatically

- To follow endpoint is used to follow a user: http://localhost:8080/follow/user/{userId}/following/{followUserId} where {userId} is the
id of the current user and {followerUserId} is the id of the user they wish to follow.  If either of these users do not exist, i.e. have
not posted a message yet to create an account, then an error code will be returned

- To view a users wall messages use the following endpoint: http://localhost:8080/wall/user/{userId} which will return their messages in
reverse chronological order

- To view the timeline use: http://localhost:8080/timeline/user/{userId} which will return all of the posts the user is following and not their own posts
