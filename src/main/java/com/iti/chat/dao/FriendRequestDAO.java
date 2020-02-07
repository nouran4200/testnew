package com.iti.chat.dao;

import com.iti.chat.model.User;

import java.util.List;

public interface FriendRequestDAO {
    public void sendFriendRequest(User sender, User receiver);
    public void acceptFriendRequest(User receiver, User sender);
    public void rejectFriendRequest(User receiver, User sender);
    public List<User> pendingFriendRequests(User user);
    public List<User> getFriends(User user);
}

