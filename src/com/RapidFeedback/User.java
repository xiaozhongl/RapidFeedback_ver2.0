package com.RapidFeedback;

/**
 * @ClassName User
 * @Description Stores all the info of a user (assessor).
 */
public class User {
	private int id;
//	private String email;
//	private String password;
	private String firstName;
//	private String middleName;
//	private String lastName;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

//	public String getUserEmail() {
//		return email;
//	}
//
//	public void setUserEmail(String userEmail) {
//		this.email = userEmail;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}

	public String getFName() {
		return this.firstName;
	}

	public void setFName(String firstName) {
		this.firstName = firstName;
	}

//	public String getMName() {
//		return middleName;
//	}
//
//	public void setMName(String mname) {
//		this.middleName = mname;
//	}
//
//	public String getLName() {
//		return lastName;
//	}
//
//	public void setLName(String lname) {
//		this.lastName = lname;
//	}

}
