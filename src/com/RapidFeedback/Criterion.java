package com.RapidFeedback;

import java.util.ArrayList;

/**
 * create by: Xiaozhong Liu
 * description: a class that stores information from ProjectCriterion joint Criterion table in database
 * create time: 2019/9/22 5:57 PM
 */
public class Criterion {

    private int id;
    private String name;
    private int weight;
    private int maxMark;
    private String markIncrement; // "full" means 1; "half" means 1/2; "quarter" means 1/4;
    private ArrayList<Field> fieldList = new ArrayList<Field>();

    public Criterion(int id, String name, int weight, int maxMark, String markIncrement){
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.maxMark = maxMark;
        this.markIncrement = markIncrement;
    }

    public int getId(){
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getMaxMark() {
        return this.maxMark;
    }

    public void setMaxMark(int maxMark) {
        this.maxMark = maxMark;
    }

    public String getMarkIncrement() {
        return this.markIncrement;
    }

    public void setMarkIncrement(String markIncrement) {
        this.markIncrement = markIncrement;
    }

    public ArrayList<Field> getFieldList() {
        return this.fieldList;
    }

    public void setFieldList(ArrayList<Field> fieldList) {
        this.fieldList = fieldList;
    }
}
