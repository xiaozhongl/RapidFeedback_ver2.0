package com.RapidFeedback;

import java.util.ArrayList;

/**
 * @ClassName SubSection
 * @Description Stores all the info of one subsection of a criteria.
*/
public class SubSection {

	private String name;
	private ArrayList<ShortText> shortTextList = new ArrayList<ShortText>();

	public String getName() {

		return name;

	}

	public void setName(String name) {

		this.name = name;

	}

	public ArrayList<ShortText> getShortTextList() {

		return shortTextList;

	}

	public void setShortTextList(ArrayList<ShortText> shortTextList) {

		this.shortTextList = shortTextList;

	}
}