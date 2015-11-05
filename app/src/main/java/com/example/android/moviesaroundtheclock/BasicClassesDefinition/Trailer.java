package com.example.android.moviesaroundtheclock.BasicClassesDefinition;

/**
 * Created by FATMA on 20-Oct-15.
 */
public class Trailer extends Extra{
    Long tId;
    String tKey;
    String tName;

    public Long gettId() {
        return tId;
    }

    public void settId(Long tId) {
        this.tId = tId;
    }

    public String gettKey() {
        return tKey;
    }

    public void settKey(String tKey) {
        this.tKey = tKey;
    }

    @Override
    public void setrAuthor(String author) {

    }

    @Override
    public void setrBody(String body) {

    }

    @Override
    public void setrId(String id) {

    }

    @Override
    public void setrUrl(String url) {

    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }
}
