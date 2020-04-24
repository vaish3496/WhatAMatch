package com.example.tinder;

public class MatchesItem {
    private String mImageURL;
    private String mName;

    public MatchesItem(String ImageURL, String Name) {
        mImageURL = ImageURL;
        mName = Name;
    }

    public String getmImageURL() {
        return mImageURL;
    }

    public String getmName() {
        return mName;
    }
}
