package com.ayandev.meymey;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "SavedMemes")
public class MemeModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int memeId;

    @ColumnInfo(name = "postLink")
    @NonNull
    private final String postLink;

    @ColumnInfo(name = "subreddit")
    @NonNull
    private final String subreddit;

    @ColumnInfo(name = "title")
    @NonNull
    private final String title;

    @ColumnInfo(name = "url")
    @NonNull
    private final String url;

    @ColumnInfo(name = "nsfw")
    @NonNull
    private final String nsfw;

    @ColumnInfo(name = "spoiler")
    @NonNull
    private final String spoiler;

    @ColumnInfo(name = "author")
    @NonNull
    private final String author;

    @ColumnInfo(name = "ups")
    @NonNull
    private final int ups;

    @ColumnInfo(name = "isSaved")
    private boolean isSaved;

    public MemeModel(@NonNull String postLink, @NonNull String subreddit, @NonNull String title, @NonNull String url, @NonNull String nsfw, @NonNull String spoiler, @NonNull String author, int ups) {
        this.postLink = postLink;
        this.subreddit = subreddit;
        this.title = title;
        this.url = url;
        this.nsfw = nsfw;
        this.spoiler = spoiler;
        this.author = author;
        this.ups = ups;
        this.isSaved = false;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public int getMemeId() {
        return memeId;
    }

    public void setMemeId(int memeId) {
        this.memeId = memeId;
    }

    @NonNull
    public String getPostLink() {
        return postLink;
    }

    @NonNull
    public String getSubreddit() {
        return subreddit;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @NonNull
    public String getAuthor() {
        return author;
    }

    @NonNull
    @Override
    public String toString() {
        String printedToBe = "MemeModel: " + getUrl() + "\n" +
                getSubreddit() + "\n" +
                getTitle() + "\n" +
                getAuthor();

        return printedToBe;
    }

    @NonNull
    public String getNsfw() {
        return nsfw;
    }

    @NonNull
    public String getSpoiler() {
        return spoiler;
    }

    public int getUps() {
        return ups;
    }
}
