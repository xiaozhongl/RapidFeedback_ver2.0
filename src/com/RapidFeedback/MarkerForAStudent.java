package com.RapidFeedback;

import java.util.ArrayList;

/**
 * create by: Xiaozhong Liu
 * description: a class that stores information from Remark joint Marker table in database
 * create time: 2019/9/22 1:26 PM
 */
public class MarkerForAStudent {
    private int markerId;       // one of the primary key, user cannot change it here
    private String email;       // consistent with markerId, user cannot change it here
    private String fullName;    // consistent with markerId, user cannot change it here
    private String remark;      // marker-made remark which is different from selected comments
    private ArrayList<Assessment> assessmentList = new java.util.ArrayList<Assessment>();

    public MarkerForAStudent (int markerId, String email, String fullName, String remark){
        this.markerId = markerId;
        this.email = email;
        this.fullName = fullName;
        this.remark = remark;
    }

    // get only
    public int getMarkerId(){
        return this.markerId;
    }

    public String getEmail(){
        return this.email;
    }

    public String getFullName(){
        return this.fullName;
    }


    //get and set
    public String getRemark(){
        return this.remark;
    }

    public void setRemark(String remark){
        this.remark = remark;
    }

    public ArrayList<Assessment> getAssessmentList(){
        return  this.assessmentList;
    }

    public void setAssessmentList(ArrayList<Assessment> assessmentList){
        this.assessmentList = assessmentList;
    }
}


