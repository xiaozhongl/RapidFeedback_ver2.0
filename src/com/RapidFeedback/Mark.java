package com.RapidFeedback;

import java.util.ArrayList;

/**
 * @ClassName Mark
 * @Description Mark class stores all the information of a presentation result.
*/
public class Mark {

	/** @Field @criteriaList : The list of criteria with grades and comments. */
	private ArrayList<Criteria> criteriaList = new ArrayList<Criteria>();
	
	/** @Field @markList : The list of marks for criteriaList */
	private ArrayList<Double> markList = new ArrayList<Double>();
	
	/** @Field @commentList : The list of criteria with only comments. */
	private ArrayList<Criteria> commentList = new ArrayList<Criteria>();
	
	/** @Field @comment : Additional comment. */
	private String comment;
	private double totalMark = -999.00;
	private String lecturerName;
	private String lecturerEmail;

	public String getLecturerEmail() {
		return lecturerEmail;
	}

	public void setLecturerEmail(String lecturerEmail) {
		this.lecturerEmail = lecturerEmail;
	}

	public String getLecturerName() {
		return lecturerName;
	}

	public void setLecturerName(String lecturerName) {
		this.lecturerName = lecturerName;
	}

	public double getTotalMark() {
		return totalMark;
	}

	public void setTotalMark(double totalMark) {
		this.totalMark = totalMark;
	}

	public ArrayList<Criteria> getCommentList() {

		return commentList;

	}

	public void setCommentList(ArrayList<Criteria> commentList) {

		this.commentList = commentList;

	}

	public String getComment() {

		return comment;

	}

	public void setComment(String comment) {

		this.comment = comment;

	}

	public void setCriteriaList(ArrayList<Criteria> criteriaList) {

		this.criteriaList = criteriaList;

	}

	public void setMarkList(ArrayList<Double> markList) {

		this.markList = markList;

	}

	public ArrayList<Criteria> getCriteriaList() {

		return criteriaList;

	}

	public ArrayList<Double> getMarkList() {

		return markList;

	}
}