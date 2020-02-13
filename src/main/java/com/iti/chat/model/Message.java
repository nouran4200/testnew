package com.iti.chat.model;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private int messageType;
    private User sender;
    private String remotePath;
    private ChatRoom chatRoom;

    public Message() {
        messageType = MessageType.TEXT_MESSAGE;
    }

    public Message(String content, User sender, int messageType) {
        this.content = content;
        this.sender = sender;
        this.messageType = messageType;
    }

    public Message(String content, User sender) {
        this.content = content;
        this.sender = sender;
        this.messageType = MessageType.TEXT_MESSAGE;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
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
