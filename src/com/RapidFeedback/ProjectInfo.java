package com.RapidFeedback;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ProjectInfo
 * @Description This class stores all the information of a project.
 */
public class ProjectInfo {

	private String projectName;
	private String username;
	private String subjectName;
	private String subjectCode;
	private String description;

	private int durationMin;
	private int durationSec;
	private int warningMin;
	private int warningSec;

	private ArrayList<String> assistantList = new ArrayList<String>();
	private ArrayList<Criteria> criteriaList = new ArrayList<Criteria>();
	private ArrayList<Criteria> commentList = new ArrayList<Criteria>();
	private ArrayList<StudentInfo> studentInfoList = new ArrayList<StudentInfo>();

	public ArrayList<Criteria> getCommentList() {

		return commentList;

	}

	public void setCommentList(ArrayList<Criteria> commentList) {

		this.commentList = commentList;

	}

	public void setProjectName(String projectName) {

		this.projectName = projectName;

	}

	public void setUsername(String username) {

		this.username = username;

	}

	public void setSubjectName(String subjectName) {

		this.subjectName = subjectName;

	}

	public void setSubjectCode(String subjectCode) {

		this.subjectCode = subjectCode;

	}

	public void setDescription(String description) {

		this.description = description;

	}

	public void setAssistant(ArrayList<String> assistantList) {

		this.assistantList = assistantList;
	}

	public void setCriteria(ArrayList<Criteria> criteriaList) {

		this.criteriaList = criteriaList;

	}

	public void setStudentInfo(ArrayList<StudentInfo> studentList) {

		this.studentInfoList = studentList;

	}

	public void addStudentList(ArrayList<StudentInfo> studentInfoList) {

		this.studentInfoList.addAll(studentInfoList);
	}

	public void addSingleStudent(StudentInfo studentInfo) {

		this.studentInfoList.add(studentInfo);

	}

	public void addSingleCriteria(Criteria criteria) {

		this.criteriaList.add(criteria);
	}

	public void setDurationMin(int durationMin) {
		this.durationMin = durationMin;
	}

	public void setDurationSec(int durationSec) {
		this.durationSec = durationSec;
	}

	public void setWarningSec(int warningSec) {
		this.warningSec = warningSec;
	}

	public void setWarningMin(int warningMin) {
		this.warningMin = warningMin;
	}

	public void setStudentInfoList(ArrayList<StudentInfo> studentInfoList) {
		this.studentInfoList = studentInfoList;
	}

	public void setCriteriaList(ArrayList<Criteria> criteriaList) {
		this.criteriaList = criteriaList;
	}

	public void setAssistantList(ArrayList<String> assistantList) {
		this.assistantList = assistantList;
	}

	public String getProjectName() {

		return projectName;

	}

	public String getUsername() {

		return username;

	}

	public String getSubjectName() {

		return subjectName;

	}

	public String getSubjectCode() {

		return subjectCode;

	}

	public String getDescription() {

		return description;

	}

	public ArrayList<String> getAssistant() {

		return assistantList;

	}

	public ArrayList<Criteria> getCriteria() {

		return criteriaList;

	}

	public ArrayList<StudentInfo> getStudentInfo() {

		return studentInfoList;

	}

	public int getDurationMin() {

		return durationMin;

	}

	public int getDurationSec() {

		return durationSec;

	}

	public int getWarningMin() {

		return warningMin;

	}

	public int getWarningSec() {

		return warningSec;

	}

}
