package com.example.movietv;

import java.io.Serializable;

public class Results implements Serializable {

    public String poster_path;
    public String original_language;
    public  String original_title;
    public  String release_date;
    public String title;
    public String vote_average;
    public String overiew;
    public String id;

    public Results(String poster_path, String original_language, String original_title, String release_date, String title, String vote_average, String overiew, String id) {
        this.poster_path = poster_path;
        this.original_language = original_language;
        this.original_title = original_title;
        this.release_date = release_date;
        this.title = title;
        this.vote_average = vote_average;
        this.overiew = overiew;
        this.id = id;
    }

    public Results() {
    }

}