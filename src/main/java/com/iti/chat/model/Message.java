package com.iti.chat.model;

public class Message {
    private String content;
    private int messageType;
    private User sender;
    private ChatRoom chatRoom;

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
