package com.example.mateusz.planszowki;

/**
 * Created by Mateusz on 26.10.2016.
 */

public class game {
    public String gamename;
    public String date;
    public String time;
    public String description;
    public String email;
    public String id;


    public game(){}

    public game(String gamename, String date, String time, String description, String email, String id ){
        this.gamename = gamename;
        this.date = date;
        this.time = time;
        this.description = description;
        this.email = email;
        this.id = id;

    }

    public String getGamename(){return gamename;}
    public String getDate(){return date;}
    public String getTime(){return time;}
    public String getDescription(){return description;}
    public String getEmail(){return email;}
    public String getId(){return id;}
}
