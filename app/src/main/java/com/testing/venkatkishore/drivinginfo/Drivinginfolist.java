package com.testing.venkatkishore.drivinginfo;

/**
 * Created by Akula on 26-04-2017.
 */

public class Drivinginfolist {

    private String starttime, endtime, hours, drivingstate;

    public Drivinginfolist() {
    }

    public Drivinginfolist(String starttime, String endtime, String hours, String drivingstate) {
        this.starttime = starttime;
        this.endtime = endtime;
        this.hours = hours;
        this.drivingstate = drivingstate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getDrivingstate() {
        return drivingstate;
    }

    public void setDrivingstate(String drivingstate) {
        this.drivingstate = drivingstate;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}