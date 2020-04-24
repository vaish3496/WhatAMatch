package com.example.tinder;

public class ProfileItem {
    private String mUserID;
    private String mName;
    private String mImageURL;

    public ProfileItem(String UserID, String Name, String ImageURL) {
        mUserID = UserID;
        mName = Name;
        mImageURL = ImageURL;
    }

    public String getmName() {
        return mName;
    }

    public String getmUserID() {
        return mUserID;
    }

    public String getmImageURL() {
        return mImageURL;
    }

}
