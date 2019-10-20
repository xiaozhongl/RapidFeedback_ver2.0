package com.RapidFeedback;

import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * create by: Xiaozhong Liu
 * description: a class that stores information from Assessment table in database
 * create time: 2019/9/22 12:57 PM
 */
public class Assessment {
    private int criterionId;    // one of the primary key, user cannot change it here
    private double score;       // score for a criterion
    private ArrayList<SelectedComment> selectedCommentList = new ArrayList<SelectedComment>();

    public Assessment (int criterionId, double score){
        this.criterionId = criterionId;
        this.score = score;
    }

    public int getCriterionId() {
        return criterionId;
    }

    public void setCriterionId(int criterionId) {
        this.criterionId = criterionId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public ArrayList<SelectedComment> getSelectedCommentList() {
        return selectedCommentList;
    }

    public void setSelectedCommentList(ArrayList<SelectedComment> selectedCommentList) {
        this.selectedCommentList = selectedCommentList;
    }

}

