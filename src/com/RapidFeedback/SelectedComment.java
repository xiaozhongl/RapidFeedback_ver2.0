package com.RapidFeedback;

/**
 * create by: Xiaozhong Liu
 * description: a class that stores information from SelectedComment table in database
 * create time: 2019/9/22 11:21 AM
 */
public class SelectedComment {


    private int fieldId;            // one of the primary key, user cannot change it here
    private int exCommentId;        // user can select expanded comment here


    /**
     * description: if expanded comment has not been set, exCommentId will be zero
     * and exCommentName will be null
     */
    public SelectedComment (int fieldId, int exCommentId){
        this.fieldId = fieldId;
        this.exCommentId = exCommentId;
    }


    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public int getExCommentId() {
        return exCommentId;
    }

    public void setExCommentId(int exCommentId) {
        this.exCommentId = exCommentId;
    }

}
