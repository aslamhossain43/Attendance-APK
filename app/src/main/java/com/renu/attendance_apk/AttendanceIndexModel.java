package com.renu.attendance_apk;

public class AttendanceIndexModel {
    private String dateTime;
    private String attendanceFor;


    public AttendanceIndexModel() {
    }

    public AttendanceIndexModel(String dateTime, String attendanceFor) {
        this.dateTime = dateTime;
        this.attendanceFor = attendanceFor;
    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getAttendanceFor() {
        return attendanceFor;
    }

    public void setAttendanceFor(String attendanceFor) {
        this.attendanceFor = attendanceFor;
    }
}
