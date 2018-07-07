package com.keanesf.popmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Review implements Parcelable {

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @SerializedName("id")
    private String id;

    @SerializedName("author")
    private String author;

    @SerializedName("url")
    private String url;

    @SerializedName("content")
    private String content;

    public Review(String reviewId) {
        this.id = reviewId;
    }

    protected Review(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.url = in.readString();
        this.content = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String reviewUrl) {
        this.url = reviewUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //CHECKSTYLE:OFF
    @Override
    @SuppressWarnings("PMD")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (id != null ? !id.equals(review.id) : review.id != null) return false;
        if (author != null ? !author.equals(review.author) : review.author != null) return false;
        if (url != null ? !url.equals(review.url) : review.url != null) return false;
        return content != null ? content.equals(review.content) : review.content == null;

    }

    @Override
    @SuppressWarnings("PMD")
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
    //CHECKSTYLE:ON

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.author);
        dest.writeString(this.url);
        dest.writeString(this.content);
    }
}
