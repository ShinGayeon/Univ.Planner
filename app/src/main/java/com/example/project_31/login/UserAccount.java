package com.example.project_31.login;

public class UserAccount {



    public UserAccount() {
    } //생성자

    public String getIdToken() { return idToken; }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
    private String idToken;

    public String getEmailId() { return emailId; }

    public void setEmailId(String emailId) { this.emailId = emailId; }
    private String emailId;

    public  String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    private String password;

}
