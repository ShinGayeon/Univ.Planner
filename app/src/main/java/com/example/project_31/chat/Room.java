package com.example.project_31.chat;

// 채팅 - 채팅 목록 DTO
public class Room {
    String chatName;
    String message;
    String time;
    String readCount;

    public Room(String chatName, String message, String time, String readCount) {
        this.chatName = chatName;
        this.message = message;
        this.time = time;
        this.readCount = readCount;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReadCount() {
        return readCount;
    }

    public void setReadCount(String readCount) {
        this.readCount = readCount;
    }
}
