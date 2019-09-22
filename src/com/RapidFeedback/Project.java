package com.RapidFeedback;

import java.util.ArrayList;

/**
 * create by: Xiaozhong Liu
 * description: a class that stores information from Project table in database
 * create time: 2019/9/22 5:36 PM
 */
public class Project{

    private int id;
    private String name;
    private String subjectName;
    private String subjectCode;
    private int durationSec;
    private int warningSec;
    private int principalId;        // this is the current user's id
    private String description;
    private int durationMin;
    private int warningMin;

    private ArrayList<Marker> markerList = new ArrayList<Marker>();
    private ArrayList<Criterion> criterionList = new ArrayList<Criterion>();
    private ArrayList<Student> studentList = new ArrayList<Student>();


    public Project (int id, String name, String subjectName, String subjectCode,
                    int duration, int warning, int principalId, String description){
        this.id = id;
        this.name = name;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.durationMin = duration / 60;
        this.durationSec = duration % 60;
        this.warningMin = warning / 60;
        this.warningSec = warning % 60;
        this.principalId = principalId;
        this.description = description;
    }

    // get only
    public int getId(){
        return this.id;
    }

    public int getPrincipalId(){
        return this.principalId;
    }


    // get and set
    public String getName() {
        return this.name;
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
        return this.warningSec;
    }

    public void setWarningSec(int warningSec) {
        this.warningSec = warningSec;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationMin(){
        return this.durationMin;
    }

    public void setDurationMin(int durationMin){
        this.durationMin = durationMin;
    }

    public int getWarningMin(){
        return this.warningMin;
    }

    public void setWarningMin(int warningMin){
        this.warningMin = warningMin;
    }

    public ArrayList<Marker> getMarkerList(){
        return this.markerList;
    }

    public void setMarkerList(ArrayList<Marker> markerList){
        this.markerList = markerList;
    }

    public ArrayList<Criterion> getCriterionList(){
        return this.criterionList;
    }

    public void setCriterionList (ArrayList<Criterion> criterionList){
        this.criterionList = criterionList;
    }

    public ArrayList<Student> getStudentList(){
        return this.studentList;
    }

    public void setStudentList(ArrayList<Student> studentList){
        this.studentList = studentList;
    }

}
