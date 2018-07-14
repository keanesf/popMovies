package com.keanesf.popmovies.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trailer {


    @SerializedName("id")
    private String id;

    @SerializedName("youtube")
    private List<Youtube> youtubeVideos;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Youtube> getYoutubeVideos() {
        return youtubeVideos;
    }

    public void setYoutubeVideos(List<Youtube> youtubeVideos) {
        this.youtubeVideos = youtubeVideos;
    }

    public class Youtube {

        private String name;

        private String source;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }



}

