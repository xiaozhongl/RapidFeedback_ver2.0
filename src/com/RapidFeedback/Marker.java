package com.RapidFeedback;

/**
 * create by: Xiaozhong Liu
 * description: a class that stores information from Marker joint MarkerInProject table in database
 * create time: 2019/9/22 5:16 PM
 */
public class Marker {

    private int id;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;

    public Marker(int id, String email, String firstName, String middleName, String lastName){
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
