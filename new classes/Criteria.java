package com.RapidFeedback;

import java.util.ArrayList;

/**
 * @ClassName: Criteria   new version Criteria
 * @Description: This class stores all the information of a marking criteria.
*/
public class Criteria {

	private String name;
	private int weighting;
	private int maximunMark;
	private String markIncrement; // "full" means 1; "half" means 1/2; "quarter"
									// means 1/4;
	private ArrayList<Field> fieldList = new ArrayList<Field>();

	public String getName() {

		return name;

	}

	public void setName(String name) {

		this.name = name;

	}

	public int getWeighting() {

		return weighting;

	}

	public void setWeighting(int weighting) {

		this.weighting = weighting;

	}

	public int getMaximunMark() {

		return maximunMark;

	}

	public void setMaximunMark(int maximunMark) {

		this.maximunMark = maximunMark;

	}

	public String getMarkIncrement() {

		return markIncrement;

	}

	public void setMarkIncrement(String markIncrement) {

		this.markIncrement = markIncrement;

	}

	public ArrayList<Field> getFieldList() {

		return fieldList;

	}

	public void setFieldList(ArrayList<Field> fieldList) {

		this.fieldList = fieldList;

	}

}