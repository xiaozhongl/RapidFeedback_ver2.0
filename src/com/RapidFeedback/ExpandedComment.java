package com.RapidFeedback;

/**
 * create by: Xiaozhong Liu
 * description: a class that stores information from ExpandedComment table in database
 * create time: 2019/9/22 5:59 PM
 */
public class ExpandedComment {
    private int id;
    private String text;

    public ExpandedComment(int id, String text){
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
