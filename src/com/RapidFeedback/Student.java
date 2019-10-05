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

    // get only
    public int getId(){
        return this.id;
    }

    public int getStudentNumber() {
        return this.studentNumber;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

}
