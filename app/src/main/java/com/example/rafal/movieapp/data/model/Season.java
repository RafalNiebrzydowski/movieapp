package com.example.rafal.movieapp.data.model;

import com.example.rafal.movieapp.data.model.Episode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rafal on 19.01.2017.
 */

public class Season implements Serializable {
    protected int numberSeason;
    protected List<Episode> episodes;

    public Season(int numberSeason, List<Episode> episodes) {
        this.numberSeason = numberSeason;
        this.episodes = episodes;
    }

    public int getNumberSeason() {
        return numberSeason;
    }

    public void setNumberSeason(int numberSeason) {
        this.numberSeason = numberSeason;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
}
