package com.yairnet.quizgame.model;

public class User
{
    private String email;
    private String username;
    private String password;

    public User(){

    }

    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail(){return email;}
}