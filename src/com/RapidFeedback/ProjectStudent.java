package com.RapidFeedback;

import java.util.ArrayList;


/**
 * create by: Xiaozhong Liu
 * description: a class that stores information from StudentInProject joint Student table in database
 * create time: 2019/9/22 2:06 PM
 */
public class ProjectStudent {

    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private int studentNumber;
    private int groupNumber;
    private double finalScore;      // given by the principal
    private String finalRemark;     // selected comment + personal remark, given by the principal
    private int audioId;
    private int ifEmailed;      // if the result has been emailed to the student
    private ArrayList<Remark> remarkList = new ArrayList<Remark>();

    public ProjectStudent(int id, String firstName, String middleName, String lastName, int studentNumber,
                          String email, int groupNumber, double finalScore, String finalRemark,
                          int ifEmailed, int audioId) {
        this.id = id;
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.groupNumber = groupNumber;
        this.finalScore = finalScore;
        this.finalRemark = finalRemark;
        this.ifEmailed = ifEmailed;
        this.audioId = audioId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }

    public String getFinalRemark() {
        return finalRemark;
    }

    public void setFinalRemark(String finalRemark) {
        this.finalRemark = finalRemark;
    }

    public int getAudioId() {
        return audioId;
    }

    public void setAudioId(int audioId) {
        this.audioId = audioId;
    }

    public int getIfEmailed() {
        return ifEmailed;
    }

    public void setIfEmailed(int ifEmailed) {
        this.ifEmailed = ifEmailed;
    }

    public ArrayList<Remark> getRemarkList() {
        return remarkList;
    }

    public void setRemarkList(ArrayList<Remark> remarkList) {
        this.remarkList = remarkList;
    }

}
