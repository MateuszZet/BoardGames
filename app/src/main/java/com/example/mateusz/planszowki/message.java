package com.example.mateusz.planszowki;

/**
 * Created by Mateusz on 30.10.2016.
 */

public class message {
    public String username;
    public String ms;


    public message(){}

    public message(String username, String ms ){
        this.username = username;
        this.ms = ms;

    }

    public String getUsername(){return username;}
    public String getMs(){return ms;}
}
