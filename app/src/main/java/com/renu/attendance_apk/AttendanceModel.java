package com.renu.attendance_apk;

import java.io.Serializable;
import java.util.List;

public class AttendanceModel implements Serializable {

    private List<String> rollList;
    private List<String> attendanceList;


    public AttendanceModel(List<String> rollList, List<String> attendanceList) {
        this.rollList = rollList;
        this.attendanceList = attendanceList;
    }


    public List<String> getRollList() {
        return rollList;
    }

    public void setRollList(List<String> rollList) {
        this.rollList = rollList;
    }

    public List<String> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(List<String> attendanceList) {
        this.attendanceList = attendanceList;
    }
}

