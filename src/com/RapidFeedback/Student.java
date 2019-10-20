package com.RapidFeedback;

/**
 * create by: Xiaozhong Liu
 * description: a class for ADD STUDENT and UPDATE STUDENT requests from FRONT END
 * create time: 2019/9/22 2:06 PM
 */
public class Student {
    private int id;     // set to 0 when add student, include when update student
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private int studentNumber;

    public Student(int id, String firstName, String middleName, String lastName, String email,
                   int studentNumber) {
        this.id = id;
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

}
