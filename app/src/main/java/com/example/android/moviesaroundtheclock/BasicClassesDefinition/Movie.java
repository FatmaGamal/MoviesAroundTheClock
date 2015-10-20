package com.example.android.moviesaroundtheclock.BasicClassesDefinition;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by FATMA on 07-Sep-15.
 */
public class Movie {

    private Long mId;
    private String mTitle;
    private String mPoster;
    private Double mVoteAverage;
    private String mReleaseDate;
    private String mOverview;
    private ArrayList<String> mReviews;
    private ArrayList<String> mTrailers;
    private Boolean mIsFav;


    public Long getmId() {
        return mId;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public double getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(double mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public String getmPoster() {
        return mPoster;
    }

    public void setmPoster(String mPoster) {
        this.mPoster = mPoster;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public ArrayList<String> getmReviews() {
        return mReviews;
    }

    public void setmReviews(ArrayList<String> mReviews) {
        this.mReviews = mReviews;
    }

    public ArrayList<String> getmTrailers() {
        return mTrailers;
    }

    public void setmTrailers(ArrayList<String> mTrailers) {
        this.mTrailers = mTrailers;
    }

    public Boolean getmIsFave() {
        return mIsFav;
    }

    public void setmIsFav(Boolean mIsFav) {
        this.mIsFav = mIsFav;
    }

}
