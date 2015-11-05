package com.example.android.moviesaroundtheclock.BasicClassesDefinition;

/**
 * Created by FATMA on 20-Oct-15.
 */
public class Review extends Extra{
    private String rId;
    private String rAuthor;
    private String rBody;
    private String rUrl;

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getrAuthor() {
        return rAuthor;
    }

    @Override
    public void settName(String name) {

    }

    @Override
    public void settKey(String key) {

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

    public String getrUrl() {
        return rUrl;
    }

    public void setrUrl(String url) {
        this.rUrl = url;
    }

}
