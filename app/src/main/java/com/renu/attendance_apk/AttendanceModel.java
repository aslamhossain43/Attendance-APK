package com.renu.attendance_apk;

import java.io.Serializable;
import java.util.List;

public class AttendanceModel implements Serializable {

    private List<String> rollList;
    private List<String> nameList;
    private List<String> attendanceList;
    private List<String> dateTimeList;

    public AttendanceModel() {
    }

    public AttendanceModel(List<String> rollList, List<String> nameList, List<String> attendanceList, List<String> dateTimeList) {
        this.rollList = rollList;
        this.nameList = nameList;
        this.attendanceList = attendanceList;
        this.dateTimeList = dateTimeList;
    }


    public List<String> getRollList() {
        return rollList;
    }

    public void setRollList(List<String> rollList) {
        this.rollList = rollList;
    }

    public List<String> getNameList() {
        return nameList;
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    public List<String> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<String> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public List<String> getDateTimeList() {
        return dateTimeList;
    }

    public void setDateTimeList(List<String> dateTimeList) {
        this.dateTimeList = dateTimeList;
    }
}

