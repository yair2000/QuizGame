package com.yairnet.quizgame.model;

// Displays the ranking of all the users
public class Ranking
{
    private String userName;
    private long score;

    public Ranking(){} // Constructor

    public Ranking(String userName, long score){
        this.userName = userName;
        this.score = score;
    }

    public String getUserName(){return userName;}

    public void setUserName(String userName){this.userName = userName;}

    public long getScore(){return score;}

    public void setScore(long score){this.score = score;}
}