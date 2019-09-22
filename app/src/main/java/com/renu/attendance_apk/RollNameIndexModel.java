package com.renu.attendance_apk;

public class RollNameIndexModel {
    private String attendanceFor;
    private String dateTime;


    public RollNameIndexModel() {
    }


    public RollNameIndexModel(String dateTime,String attendanceFor) {
        this.dateTime = dateTime;
        this.attendanceFor = attendanceFor;
    }


    public String getAttendanceFor() {
        return attendanceFor;
    }

    public void setAttendanceFor(String attendanceFor) {
        this.attendanceFor = attendanceFor;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
