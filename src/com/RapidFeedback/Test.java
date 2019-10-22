package com.RapidFeedback;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        MysqlFunction db = new MysqlFunction();
        int id;
        boolean bool;
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
//        id = db.addStudent("Xiao", "", "l", 789,
//                "lxz0960@gmail.com", 7);
//        System.out.println(db.addStudent("Xiao", "", "l", 789,
//                "lxz0960@gmail.com", 7));

//
//        bool = db.updateStudent(2, 111111, "1", "2", "3", "");

//        db.updateGroupNumber(2,2,1);
//        db.updateGroupNumber(2,3,1);
//        db.updateGroupNumber(2,4,1);


//        bool = db.updateFinalResult(2,3, 99, "good", 1);

//        name = db.getMarkerName(6);
//        bool = db.getMarkerName();

//        bool = db.deleteProjectMarker(4,2);

//        projects = db.getProjectList(2);
//        System.out.println(JSON.toJSONString(projects));



//        List<Project> projectList = new Gson().fromJson(JSON.toJSONString(projects),
//                new TypeToken<List<Project>>(){}.getType());
//        ArrayList<Project> arrayList ;
//        arrayList = new ArrayList();
//        arrayList.addAll(projectList);
//        Project project1 = arrayList.get(0);
////        Criterion criterion = project1.getCriterionList().get(0);
////        System.out.println(JSON.toJSONString(criterion));
//
//        System.out.println(db.addCriteria(project1.getCriterionList(), 6));

//        System.out.println(db.deleteCriteria(7));
//        System.out.println(db.addStudent("xz2",
//                "", "l", 456, "",7));

//        projects = db.getProjectList(4);
//        System.out.println(JSON.toJSONString(projects));

//        List<Project> projectList = new Gson().fromJson(JSON.toJSONString(projects),
//                new TypeToken<List<Project>>(){}.getType());
//        ArrayList<Project> arrayList ;
//        arrayList = new ArrayList();
//        arrayList.addAll(projectList);
//        Project project1 = arrayList.get(1);
//        ProjectStudent student = project1.getStudentList().get(0);
//        Remark remark = student.getRemarkList().get(0);
////        System.out.println(JSON.toJSONString(remark));
//        System.out.println(db.addResult(remark, 7, 17));


//        System.out.println(db.addProjectMarker(6,7));
//        System.out.println(db.deleteProjectMarker(2,2));
//        System.out.println(db.sentMail(7, 15));

//        ArrayList<Student> slist = new ArrayList<>();
//        slist.add(new Student(1,"firstName", "", "lastName", "email", 111));
//        slist.add(new Student(2,"firstName", "", "lastName", "email", 222));
//        System.out.println(JSON.toJSONString(slist));
//        System.out.println(JSON.toJSONString(db.getProjectStudent(15, 7)));
        PDFUtil pdf = new PDFUtil();
        pdf.create(15, 7, "/Users/sindorei/audio/", "test.pdf");
    }
}
