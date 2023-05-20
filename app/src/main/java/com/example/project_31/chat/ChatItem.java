package com.example.project_31.chat;

public class ChatItem {
    String name;
    String message;
    String time;
    String pofileUrl;

    public ChatItem(String name, String message, String time, String pofileUrl) {
        this.name = name;
        this.message = message;
        this.time = time;
        this.pofileUrl = pofileUrl;
    }

    //firebase DB에 객체로 값을 읽어올 때..
    //파라미터가 비어있는 생성자가 핑요함.
    public ChatItem() {
    }

    //Getter & Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPofileUrl() {
        return pofileUrl;
    }

    public void setPofileUrl(String pofileUrl) {
        this.pofileUrl = pofileUrl;
    }
}
