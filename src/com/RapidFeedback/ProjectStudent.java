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

    // get only
    public int getId(){
        return this.id;
    }


    // get and set
    public int getStudentNumber() {
        return this.studentNumber;
    }

    public void setStudentNumber(int studentNumber){
        this.studentNumber = studentNumber;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public void setMiddleName(String middleName){
        this.middleName = middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

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

    public int getAudioId(){
        return this.audioId;
    }

    public void setAudioId(int audioId){
        this.audioId = audioId;
    }

    public int isIfEmailed(){
        return this.ifEmailed;
    }

    public void setIfEmailed(int ifEmailed){
        this.ifEmailed = ifEmailed;
    }

    public ArrayList<Remark> getRemarkList(){
        return this.remarkList;
    }

    public void setRemarkList(ArrayList<Remark> remarkList){
        this.remarkList = remarkList;
    }

}
