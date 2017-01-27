package com.example.rafal.movieapp.data.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rafal on 19.01.2017.
 */

public class Serial extends Video implements Serializable {



    public List<Season> seasons;

    public Serial(int id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public Serial(int id, String title, String overview, String dateRelease, int voteCount, double voteAverage, int favorite,String image, List<Season> seasons) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.dateRelease = dateRelease;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.favorite = favorite;
        this.image = image;
        this.seasons = seasons;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

}
