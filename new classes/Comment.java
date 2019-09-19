package com.RapidFeedback;

import java.util.ArrayList;

/**
 * @ClassName Comment    called ShortText in old version
 * @Description Comment is a collection of comments which has the similar
 *              meaning. The name of the shortText is the meaning of these
 *              comments.
 */
public class Comment {

	private String cmtext;
	/**
	 * @Field @type : the scale of the positivity of these comments.
	 */
	private String type;
	/**
	 * @Field @epcomment : a collection of comments whose meaning is the
	 *        ShortText.
	 */
	private ArrayList<String> epcomment  = new ArrayList<String>();

	public String getText() {

		return cmtext;

	}

	public void setText(String cmtext) {

		this.cmtext = cmtext;

	}

	public String getType() {

		return type;

	}

	public void setType(String type) {

		this.type = type;

	}

	public ArrayList<String> getEpComment() {

		return epcomment;

	}

	public void setEpComment(ArrayList<String> epcomment) {

		this.epcomment = epcomment;

	}
}

