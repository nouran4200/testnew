package com.iti.chat.model;

import java.io.Serializable;

<<<<<<< HEAD
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
=======
public class Message implements Serializable{
>>>>>>> bbffb61967bebec2709d5d5d966043410e11014d
    private String content;
    private int messageType;
    private User sender;
    private ChatRoom chatRoom;

    public Message() {

    }

    public Message(String content, User sender) {
        this.content = content;
        this.sender = sender;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public int getMessageType() {

        return messageType;
    }

    public void setMessageType(int messageType) {

        this.messageType = messageType;
    }

    public User getSender() {

        return sender;
    }

    public void setSender(User sender) {

        this.sender = sender;
    }

    public ChatRoom getChatRoom() {

        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {

        this.chatRoom = chatRoom;
    }
}
