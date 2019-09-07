package com.RapidFeedback;

/**
 * @ClassName StudentInfo
 * @Description This class stores all the information of a student.
*/
public class StudentInfo {

	private String number;
	private String firstName;
	private String middleName;
	private String surname;
	private String email;
	/** @Field @totalMark : given by primary assessor. */
	private Double totalMark = -999.0;
	private Mark mark;
	private int group = -999;
	/**
	 * @Field @sendEmail : the flag to show whether the report of this student
	 *        is sent or not. If email sent, it equals true.
	 */
	private boolean sendEmail;

	public StudentInfo() {
	}

	public StudentInfo(String number, String firstName, String middleName,
			String surname, String email) {

		this.number = number;
		this.firstName = firstName;
		this.middleName = middleName;
		this.surname = surname;
		this.email = email;

	}

	public void setStudentInfo(String number, String firstName,
			String middleName, String surname, String email) {

		this.number = number;
		this.firstName = firstName;
		this.middleName = middleName;
		this.surname = surname;
		this.email = email;

	}

	public Mark getMark() {

		return mark;

	}

	public void setMark(Mark mark) {

		this.mark = mark;

	}

	public void setFirstName(String firstName) {

		this.firstName = firstName;

	}

	public void setMiddleName(String middleName) {

		this.middleName = middleName;

	}

	public void setEmail(String email) {

		this.email = email;

	}

	public void setSurname(String surname) {

		this.surname = surname;

	}

	public void setTotalMark(Double totalMark) {

		this.totalMark = totalMark;

	}

	public void setGroup(int group) {

		this.group = group;

	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {

		return number;

	}

	public String getFirstName() {

		return firstName;

	}

	public String getMiddleName() {

		return middleName;

	}

	public String getSurname() {

		return surname;

	}

	public String getEmail() {

		return email;

	}

	public Double getTotalMark() {

		return totalMark;

	}

	public int getGroup() {

		return group;

	}

	public boolean getSendEmail() {

		return sendEmail;

	}

	public void setSendEmail(boolean sendEmail) {

		this.sendEmail = sendEmail;

	}

}