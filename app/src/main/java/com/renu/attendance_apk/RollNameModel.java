package com.renu.attendance_apk;

import java.util.List;

public class RollNameModel {
    private List<String>rollNo;
    private List<String>name;
    private List<String>attFor;
    private List<String>dateTime;

    public RollNameModel() {
    }

    public RollNameModel(List<String> rollNo, List<String> name, List<String> attFor,List<String> dateTime) {

        this.rollNo = rollNo;
        this.name = name;
        this.attFor=attFor;
        this.dateTime=dateTime;
    }



    public List<String> getRollNo() {
        return rollNo;
    }

    public void setRollNo(List<String> rollNo) {
        this.rollNo = rollNo;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getAttFor() {
        return attFor;
    }

    public void setAttFor(List<String> attFor) {
        this.attFor = attFor;
    }

    public List<String> getDateTime() { return dateTime; }

    public void setDateTime(List<String> dateTime) {
        this.dateTime = dateTime;
    }
}
