package com.iti.chat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatRoom implements Serializable{
    private int id;
    private List<User> users;
    private List<Message> messages;
    private String name;
    static int counter = 0;

    public ChatRoom() {
        id = counter++;
        users = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public List<Message> getMessages() {

        return messages;
    }

    public void setMessages(List<Message> messages) {

        this.messages = messages;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public List<User> getUsers() {

        return users;
    }

    public void addUser(User user) {
        users.add(user);
        user.getChatRooms().add(this);
    }

    public void setUsers(List<User> users) {

        this.users = users;
    }
}
