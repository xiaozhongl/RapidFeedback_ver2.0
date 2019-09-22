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

    // get only
    public int getId(){
        return this.id;
    }

    public String getEmail(){
        return this.email;
    }

    public String getFirstName(){
        return  this.firstName;
    }

    public String getMiddleName(){
        return this.middleName;
    }

    public String getLastName(){
        return this.lastName;
    }
}
