package com.keanesf.popmovies.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Room;

import java.util.Date;

/**
 * Defines the schema of a table in {@link Room} for a single favorite.
 * The date is used as an {@link Index} so that its uniqueness can be ensured. Indexes
 * also allow for fast lookup for the column.
 */
@Entity(tableName = "favorite", indices = {@Index(value = {"id"}, unique = true)})
public class FavoriteEntry {

    @PrimaryKey
    private Long id;

    private String posterPath;

    private String title;

    private String overview;

    private String releaseDate;

    private String voteAverage;

    // Constructor used by Room to create FavoriteEntries
    public FavoriteEntry(Long id, String posterPath, String title, String overview, String releaseDate, String voteAverage) {
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
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

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }
}