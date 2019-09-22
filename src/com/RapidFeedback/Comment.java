package com.RapidFeedback;

import java.util.ArrayList;

/**
 * create by: Xiaozhong Liu
 * description: a class that stores information from Comment table in database
 * create time: 2019/9/22 5:57 PM
 */
public class Comment {

    private int id;
    private String text;
    private String type;    // either be "POSITIVE" , "NEGATIVE" or "NEUTRAL"
    private ArrayList<ExpandedComment> expandedCommentList = new ArrayList<ExpandedComment>();

    public Comment(int id, String text, String type){
        this.id = id;
        this.text = text;
        this.type = type;
    }

    public int getId(){
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<ExpandedComment> getExCommentList() {
        return this.expandedCommentList;
    }

    public void setExCommentList(ArrayList<ExpandedComment> exCommentList) {
        this.expandedCommentList = exCommentList;
    }
}
