package com.RapidFeedback;

import java.util.ArrayList;

/**
 * @ClassName ShortText
 * @Description ShortText is a collection of comments which has the similar
 *              meaning. The name of the shortText is the meaning of these
 *              comments.
 */
public class ShortText {

	private String name;
	/**
	 * @Field @grade : the scale of the positivity of these comments.
	 *        1-negative, 2-middle, 3-positive
	 */
	private int grade;
	/**
	 * @Field @longtext : a collection of comments whose meaning is the
	 *        ShortText.
	 */
	private ArrayList<String> longtext = new ArrayList<String>();

	public String getName() {

		return name;

	}

	public void setName(String name) {

		this.name = name;

	}

	public int getGrade() {

		return grade;

	}

	public void setGrade(int grade) {

		this.grade = grade;

	}

	public ArrayList<String> getLongtext() {

		return longtext;

	}

	public void setLongtext(ArrayList<String> longtext) {

		this.longtext = longtext;

	}

}
