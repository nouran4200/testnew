package com.iti.chat.service;

import com.iti.chat.model.ChatRoom;
import com.iti.chat.model.Message;
import com.iti.chat.model.User;

import java.util.List;

public interface ChatRoomService {
    public ChatRoom createNewChatRoom(List<User> users);
    public void sendMessage(Message message, ChatRoom room);
}
