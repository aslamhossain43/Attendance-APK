package com.renu.attendance_apk;

import java.util.List;

public class PercentageModel {

    private List<String>attList,rollList;
    private List<Integer>dayList,pList,aList,percentList;

    public PercentageModel() {
    }

    public PercentageModel(List<String> attList, List<String> rollList, List<Integer> dayList, List<Integer> pList, List<Integer> aList, List<Integer> percentList) {
        this.attList = attList;
        this.rollList = rollList;
        this.dayList = dayList;
        this.pList = pList;
        this.aList = aList;
        this.percentList = percentList;
    }


    public List<String> getAttList() {
        return attList;
    }

    public void setAttList(List<String> attList) {
        this.attList = attList;
    }

    public List<String> getRollList() {
        return rollList;
    }

    public void setRollList(List<String> rollList) {
        this.rollList = rollList;
    }

    public List<Integer> getDayList() {
        return dayList;
    }

    public void setDayList(List<Integer> dayList) {
        this.dayList = dayList;
    }

    public List<Integer> getpList() {
        return pList;
    }

    public void setpList(List<Integer> pList) {
        this.pList = pList;
    }

    public List<Integer> getaList() {
        return aList;
    }

    public void setaList(List<Integer> aList) {
        this.aList = aList;
    }

    public List<Integer> getPercentList() {
        return percentList;
    }

    public void setPercentList(List<Integer> percentList) {
        this.percentList = percentList;
    }
}
