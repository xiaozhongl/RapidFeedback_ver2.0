package com.RapidFeedback;

import java.util.ArrayList;

/**
 * create by: Xiaozhong Liu
 * description: a class used for RETURN PROJECTS responds from BACK END
 * create time: 2019/9/22 5:36 PM
 */
public class Project{

    private int id;
    private String name;
    private String subjectName;
    private String subjectCode;
    private int durationSec;
    private int warningSec;
    private int principalId;
    private String description;
    private int durationMin;
    private int warningMin;

    private ArrayList<Marker> markerList = new ArrayList<Marker>();
    private ArrayList<Criterion> criterionList = new ArrayList<Criterion>();
    private ArrayList<ProjectStudent> studentList = new ArrayList<ProjectStudent>();


    public Project (int id, String name, String subjectName, String subjectCode,
                    int durationSec, int warningSec, int principalId, String description){
        this.id = id;
        this.name = name;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.durationMin = durationSec / 60;
        this.durationSec = durationSec % 60;
        this.warningMin = warningSec / 60;
        this.warningSec = warningSec % 60;
        this.principalId = principalId;
        this.description = description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public int getDurationSec() {
        return durationSec;
    }

    public void setDurationSec(int durationSec) {
        this.durationSec = durationSec;
    }

    public int getWarningSec() {
        return warningSec;
    }

    public void setWarningSec(int warningSec) {
        this.warningSec = warningSec;
    }

    public int getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(int principalId) {
        this.principalId = principalId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }

    public int getWarningMin() {
        return warningMin;
    }

    public void setWarningMin(int warningMin) {
        this.warningMin = warningMin;
    }

    public ArrayList<Marker> getMarkerList() {
        return markerList;
    }

    public void setMarkerList(ArrayList<Marker> markerList) {
        this.markerList = markerList;
    }

    public ArrayList<Criterion> getCriterionList() {
        return criterionList;
    }

    public void setCriterionList(ArrayList<Criterion> criterionList) {
        this.criterionList = criterionList;
    }

    public ArrayList<ProjectStudent> getStudentList() {
        return studentList;
    }

    public void setStudentList(ArrayList<ProjectStudent> studentList) {
        this.studentList = studentList;
    }

}
