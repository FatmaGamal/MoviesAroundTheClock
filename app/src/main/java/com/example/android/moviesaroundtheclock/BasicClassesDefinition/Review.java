package com.example.android.moviesaroundtheclock.BasicClassesDefinition;

/**
 * Created by FATMA on 20-Oct-15.
 */
public class Review {
    private long rId;
    private String rAuthor;
    private String rBody;

    public long getrId() {
        return rId;
    }

    public void setrId(long rId) {
        this.rId = rId;
    }

    public String getrAuthor() {
        return rAuthor;
    }

    public void setrAuthor(String rName) {
        this.rAuthor = rName;
    }

    public String getrBody() {
        return rBody;
    }

    public void setrBody(String rReview) {
        this.rBody = rReview;
    }
}
