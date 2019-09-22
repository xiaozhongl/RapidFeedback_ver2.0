package com.RapidFeedback;

/**
 * create by: Xiaozhong Liu
 * description: a class that stores information from SelectedComment table in database
 * create time: 2019/9/22 11:21 AM
 */
public class SelectedComment {
    private int fieldId;            // one of the primary key, user cannot change it here
    private String fieldName;       // consistent with fieldId, user cannot change it here
    private int exCommentId;        // user can select expanded comment here
    private String exCommentName;   // front end should update this name when updating exCommentId

    /**
     * description: if expanded comment has not been set, exCommentId will be zero
     * and exCommentName will be null
     */
    public SelectedComment (int fieldId, String fieldName, int exCommentId, String exCommentName){
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.exCommentId = exCommentId;
        this.exCommentName = exCommentName;
    }

    // get only
    public int getFieldId(){
        return this.fieldId;
    }

    public String getFieldName(){
        return this.fieldName;
    }


    // get and set
    public int getExCommentId(){
        return this.exCommentId;
    }

    public void setExCommentId(int exCommentId){
        this.exCommentId = exCommentId;
    }

    public String getExCommentName(){
        return  this.exCommentName;
    }

    public void setExCommentName(String exCommentName){
        this.exCommentName = exCommentName;
    }

}
