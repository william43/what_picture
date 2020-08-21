package com.example.willaboy.whatpicture;

public class Model_Upload {

    private String mName;
    private String mImageUrl;
    private String Category;
    private Boolean beingPlayed;
    private String[] played = {"No", "No", "No"};

    public Model_Upload() {
        //empty constructor needed
    }

    public Model_Upload(String mName, String mImageUrl){
        this.mName = mName;
        this.mImageUrl = mImageUrl;
    }

    public Model_Upload(String name, String imageUrl, String Category) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
        this.Category = Category;

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

}
