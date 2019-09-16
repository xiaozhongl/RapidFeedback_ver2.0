package com.RapidFeedback;

import java.util.ArrayList;

/**
 * @ClassName Field  called Subsection in old version
 * @Description Stores all the info of one field of a criteria.
*/
public class Field {

	private String name;
	private ArrayList<Comment> commentList = new ArrayList<Comment>();

	public String getName() {

		return name;

	}

	public void setName(String name) {

		this.name = name;

	}

	public ArrayList<Comment> getCommentList() {

		return commentList;

	}

	public void setCommentList(ArrayList<Comment> commentList) {

		this.commentList = commentList;

	}
}
