package com.testing.venkatkishore.drivinginfo;

/**
 * Created by Akula on 05-05-2017.
 */

public class Drivingimagelistdetails {

    String nowinfo;
    String imagefileinfo;

    public Drivingimagelistdetails() {
    }

    public Drivingimagelistdetails(String nowinfo, String imagefileinfo) {
        this.nowinfo = nowinfo;
        this.imagefileinfo = imagefileinfo;
    }

    public String getNowinfo() {
        return nowinfo;
    }

    public void setNowinfo(String nowinfo) {
        this.nowinfo = nowinfo;
    }

    public String getImagefileinfo() {
        return imagefileinfo;
    }

    public void setImagefileinfo(String imagefileinfo) {
        this.imagefileinfo = imagefileinfo;
    }
}
