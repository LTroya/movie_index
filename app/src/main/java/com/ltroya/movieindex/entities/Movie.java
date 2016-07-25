package com.ltroya.movieindex.entities;

import com.google.gson.annotations.SerializedName;
import com.ltroya.movieindex.MovieIndexApp;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Movie extends RealmObject {
    @Ignore
    private final String IMAGE_ROOT_URL = "http://image.tmdb.org/t/p/" + MovieIndexApp.IMAGE_API_SIZE;

    @PrimaryKey
    private String id;

    @SerializedName("poster_path")
    private String posterPath;

    private String title;

    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    private float popularity;

    @SerializedName("vote_average")
    private float voteAverage;

    @SerializedName("genres")
    RealmList<Genre> genreList;

    @Ignore
    public boolean keep;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosterPath() {
        return IMAGE_ROOT_URL + posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public RealmList<Genre> getGenreList() {
        return genreList;
    }

    public void setGenreList(RealmList<Genre> genreList) {
        this.genreList = genreList;
    }

    public boolean isKeep() {
        return keep;
    }

    public void setKeep(boolean keep) {
        this.keep = keep;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", popularity=" + popularity +
                ", voteAverage=" + voteAverage +
                ", genreList=" + genreList +
                ", keep=" + keep +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;

        Movie movie = (Movie) o;

        if (isKeep() != movie.isKeep()) return false;
        if (getId() != null ? !getId().equals(movie.getId()) : movie.getId() != null) return false;
        if (getPosterPath() != null ? !getPosterPath().equals(movie.getPosterPath()) : movie.getPosterPath() != null)
            return false;
        if (getTitle() != null ? !getTitle().equals(movie.getTitle()) : movie.getTitle() != null)
            return false;
        return getOverview() != null ? getOverview().equals(movie.getOverview()) : movie.getOverview() == null;

    }
}
