package com.iti.chat.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User implements Comparable<User>, Serializable {

    private String firstName;
    private String lastName;
    private int id;
    private String email;
    private String phone;
    private int status;
    private int gender;
    private String country;
    private String bio;
    private String password;
    private List<com.iti.chat.model.User> friends;
    private List<ChatRoom> chatRooms;
    private boolean chatBotEnabled;
    private String remoteImagePath;
    private int isAddedFromServer;
    private LocalDate birthDate;

    {
        friends = new ArrayList<>();
        chatRooms = new ArrayList<>();
        country = "Egypt";
        bio = "Biooo.......";
        status = UserStatus.OFFLINE;
    }

    public User(String firstName, String lastName, String phone, String email, int gender, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.status = UserStatus.ONLINE;
        this.gender = gender;
        this.country = country;
    }

    public User(String firstName, String lastName, int id, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public User() {

    }

    public String getRemoteImagePath() {
        return remoteImagePath;
    }

    public void setRemoteImagePath(String remoteImagePath) {
        this.remoteImagePath = remoteImagePath;
    }

    public boolean isChatBotEnabled() {
        return chatBotEnabled;
    }

    public void setChatBotEnabled(boolean chatBotEnabled) {
        this.chatBotEnabled = chatBotEnabled;
    }

    public int getGender() {

        return gender;
    }

    public void setGender(int gender) {

        this.gender = gender;
    }


    public String getStatusMessage() {
        String state = "";
        switch (status) {
            case UserStatus.AWAY:
                state = "Away";
                break;
            case UserStatus.BUSY:
                state = "Busy";
                break;
            case UserStatus.ONLINE:
                state = "Online";
                break;
            default:
                state = "Offline";
        }
        return state;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getPhone() {

        return phone;
    }

    public void setPhone(String phone) {

        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<com.iti.chat.model.User> getFriends() {
        return friends;
    }

    public void setFriends(List<com.iti.chat.model.User> friends) {
        this.friends = friends;
    }

    public List<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    public void setChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof com.iti.chat.model.User) {
            com.iti.chat.model.User user = (com.iti.chat.model.User) object;
            return id == user.getId();
        }
        return false;
    }

    public ChatRoom getSharedChatRoom(List<com.iti.chat.model.User> users) {
        for (ChatRoom chatRoom : chatRooms) {
            if (chatRoom.getUsers().containsAll(users) && users.containsAll(chatRoom.getUsers())) {
                return chatRoom;
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", status='" + getStatus() + '\'' +
                ", image='" + getRemoteImagePath() +
                '}';
    }

    @Override
    public int compareTo(com.iti.chat.model.User o) {
        return firstName.compareTo(o.firstName);
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public int getIsAddedFromServer() {
        return isAddedFromServer;
    }

    public void setIsAddedFromServer(int isAddedFromServer) {
        this.isAddedFromServer = isAddedFromServer;
    }

}
