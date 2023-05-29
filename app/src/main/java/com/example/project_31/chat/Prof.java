package com.example.project_31.chat;

// 채팅 - 교수 DTO
public class Prof {
    String name;
    String major;

    public Prof(String name, String major) {
        this.name = name;
        this.major = major;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
