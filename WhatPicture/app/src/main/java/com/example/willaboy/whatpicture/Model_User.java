package com.example.willaboy.whatpicture;

import android.view.Display;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Model_User {

    private String fname;
    private String uname;
    private String password;
    private ArrayList<Model_Upload> pictureList = new ArrayList<>();
    private ArrayList<Model_Upload> playedList = new ArrayList<>();


    public Model_User(){

    }

    public Model_User(String fname, String uname, String password, ArrayList<Model_Upload> pictureList, ArrayList<Model_Upload> playedList) {
        this.fname = fname;
        this.uname = uname;
        this.password = password;
        this.pictureList = pictureList;
        this.playedList = playedList;
    }

    public Model_User(String fname, String uname, String password){
        this.fname = fname;
        this.uname = uname;
        this.password = password;

    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Model_Upload> getPictureList() {
        return pictureList;
    }

    public void setPictureList(ArrayList<Model_Upload> pictureList) {
        this.pictureList = pictureList;
    }

    public ArrayList<Model_Upload> getPlayedList() {
        return playedList;
    }

    public void setPlayedList(ArrayList<Model_Upload> playedList) {
        this.playedList = playedList;
    }
}
