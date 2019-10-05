package com.RapidFeedback;

import java.util.ArrayList;

/**
 * create by: Xiaozhong Liu
 * description: a class that stores information from Remark joint Marker table in database
 * create time: 2019/9/22 1:26 PM
 */
public class Remark {
    private int id;             // one of the primary key, user cannot change it here
    private String text;      // marker-made remark which is different from selected comments
    private ArrayList<Assessment> assessmentList = new java.util.ArrayList<Assessment>();

    public Remark (int markerId, String text){
        this.id = markerId;
        this.text  = text;
    }

    // get only
    public int getMarkerId(){
        return this.id;
    }


    //get and set
    public String getText(){
        return this.text;
    }

    public void setText(String text){
        this.text = text;
    }

    public ArrayList<Assessment> getAssessmentList(){
        return  this.assessmentList;
    }

    public void setAssessmentList(ArrayList<Assessment> assessmentList){
        this.assessmentList = assessmentList;
    }
}


