package com.example.rafal.movieapp.data.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rafal on 19.01.2017.
 */

public class Movie extends Video implements Serializable {

    public int duration;

    public ArrayList<String> video;

    public Movie(int id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public Movie(int id, String title, String overview, String dateRelease, int voteCount, double voteAverage, int favorite, String image, int duration, ArrayList<String> video) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.dateRelease = dateRelease;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.favorite = favorite;
        this.image = image;
        this.duration = duration;
        this.video = video;
    }



    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ArrayList<String> getVideo() {
        return video;
    }

    public void setVideo(ArrayList<String> video) {
        this.video = video;
    }

}
