package com.RapidFeedback;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        MysqlFunction db = new MysqlFunction();
        int id = -5;
        boolean bool = false;
        String name;
        ArrayList<Project> projects;
//        id = db.addMarker("garbage", "", "", "", "");
//        id = db.addMarker("6@123.com", "123", "xz", "", "l");
//        id = db.addMarker("5@123.com", "123", "x", "x", "l");
//        id = db.addMarker("3@123.com", "123", "3", "", "l");
//        id = db.addMarker("4@123.com", "123", "3", "", "l");
//        id = db.logIn("lxz123@.com", "12");

//        id = db.createProject("garbage", "", "",0
//                , 0, "", 1);
//        id = db.createProject("Assigment1", "AI ", "COMP90018", 600, 60, "", 3);
//        id = db.createProject("Assigment1", "Crypto", "COMP90018",
//                600, 60, "", 3);
//        bool = db.updateProject(3,"Assigment1", "Distributed Com", "COMP90018",
//                600, 60, "This is");

//        bool = db.deleteProject(3);
//        id = db.addStudent("Xiao", "", "l", 77770,
//                "lxz0960@gmail.com", 2);

//
//        bool = db.updateStudent(2, 111111, "1", "2", "3", "");

//        db.updateGroupNumber(2,2,1);
//        db.updateGroupNumber(2,3,1);
//        db.updateGroupNumber(2,4,1);


//        bool = db.updateFinalResult(2,3, 99, "good", 1);

//        name = db.getMarkerName(6);
//        bool = db.getMarkerName();

        projects = db.getProjectList(2);

//        bool = db.deleteProjectMarker(4,2);


        System.out.println(JSON.toJSONString(projects));
//        System.out.println(bool);
    }


}
