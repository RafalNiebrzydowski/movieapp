package com.example.rafal.movieapp.data.model;

import java.io.Serializable;

/**
 * Created by rafal on 19.01.2017.
 */

public class Episode implements Serializable {
    protected String title;
    protected String date;

    public Episode(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
