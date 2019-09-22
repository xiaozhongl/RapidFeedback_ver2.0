package com.RapidFeedback;

import java.util.ArrayList;

/**
 * create by: Xiaozhong Liu
 * description: a class that stores information from StudentInProject joint Student table in database
 * create time: 2019/9/22 2:06 PM
 */
public class Student {

    private int id;                 // one of the primary key, user cannot change it here
    private String firstName;       // consistent with studentId, user cannot change it here
    private String middleName;      // consistent with studentId, user cannot change it here
    private String lastName;        // consistent with studentId, user cannot change it here
    private String email;           // consistent with studentId, user cannot change it here
    private int studentNumber;      // consistent with studentId, user cannot change it here
    private int groupNumber;
    private double finalScore;      // given by the principal
    private String finalRemark;     // selected comment + personal remark, given by the principal
    private String audioRemark;     // uri of the audio remark file
    private boolean ifEmailed;      // if the result has been emailed to the student
    private ArrayList<MarkerForAStudent> markerList = new java.util.ArrayList<MarkerForAStudent>();

    public Student(int id, String firstName, String middleName, String lastName, String email,
                   int studentNumber, int groupNumber, double finalScore, String finalRemark,
                   String audioRemark, boolean ifEmailed) {
        this.id = id;
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.groupNumber = groupNumber;
        this.finalScore = finalScore;
        this.finalRemark = finalRemark;
        this.audioRemark = audioRemark;
        this.ifEmailed = ifEmailed;
    }

    // get only
    public int getId(){
        return this.id;
    }

    public int getStudentNumber() {
        return this.studentNumber;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }


    // get and set
    public int getGroupNumber(){
        return this.groupNumber;
    }

    public void setGroupNumber(int groupNumber){
        this.groupNumber = groupNumber;
    }

    public double getFinalScore(){
        return  this.finalScore;
    }

    public void setFinalScore(double finalScore){
        this.finalScore = finalScore;
    }

    public String getFinalRemark(){
        return this.finalRemark;
    }

    public void setFinalRemark(String finalRemark){
        this.finalRemark = finalRemark;
    }

    public String getAudioRemark(){
        return this.audioRemark;
    }

    public void setAudioRemark(String audioRemark){
        this.audioRemark = audioRemark;
    }

    public boolean isIfEmailed(){
        return this.ifEmailed;
    }

    public void setIfEmailed(boolean ifEmailed){
        this.ifEmailed = ifEmailed;
    }

    public ArrayList<MarkerForAStudent> getMarkerList(){
        return this.markerList;
    }

    public void setMarkerList(ArrayList<MarkerForAStudent> markerList){
        this.markerList = markerList;
    }
}
