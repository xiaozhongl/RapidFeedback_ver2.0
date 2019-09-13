# API

### /LoginServlet
* For login
* POST 
```
{
    username: // this key is named as email in RegisterServlet
    password:
}
```
* return
```
{
    login_ACK: // if > 0 then login succeed/ -2 then fail
    projectList:
    firstName:
    token:
}
```
### /RegisterServlet
* For register
* POST
```
{
    email:
    password:
    firstName:
    middleName:
    lastName:
}
```
* return
``` 
{
    register_ACK: //true: success/ false: fail
}
```
### /AddStudentServlet
* To add student into a project
* POST
```
{
    token: String
    projectName: String
    studentID: String
    firstName: String
    middleName: String
    lastName:
    email: // student's email
}
```
* return
``` 
{
    updateStudent_ACK: boolean
}
```
### /CriteriaListServlet
* inserts or updates two criteriaList (markedCriteriaList and commentCriteriaList)
* POST
```
{
    token: String
    projectName: String
    markedCriteriaListString: String
    commentCriteriaListString: String
}
```
* return
```
{
    updateProject_ACK: boolean
}
```
### /DeleteProjectServlet
* to delete project 
* POST   // better to use DELETE method
```
{
    token: String
    projectName: String
}
```
* return
```
{
    updateProject_ACK: boolean
}
```
### /DeleteStudentServlet
* to delete a student of a specific project
* POST
```
{
    token: String
    projectName: String
    studentID: String
}
```
* return
```
{
    updateStudent_ACK: boolean
}
```
### /EditStudentServlet
* edit a student information in a specific project
* POST
``` 
{
    token: String
    projectName: String
    studentID: String
    firstName: String
    middleName: String
    lastName:
    email: // student's email
}
```
* return
```
{
    updateStudent_ACK: boolean
}
```
### /GetMarkServlet
* POST
```
{
    token: String
    projectName: String
    studentNumberList: String
    primaryEmail: String
}
```
* return
```
{
    getMark_ACK: boolean
    markList: String // if getMark_ACK == true
    otherComments: String // if studentNumberList.size > 1
}
```
### /GroupStudentServlet
* to change the group number of a student
* POST
```
{
    token: String
    projectName: String
    studentID: String
    group: integer
}
```
* return
```
{
    updateStudent_ACK: boolean
}
```
### /ImportStudentsServlet
* to store a list of students of a project
* POST
```
{
    token: String
    projectName: String
    studentList: String
}
```
* return
```
{
    updateStudent_ACK: boolean
}
```
### /InviteAssessorServle
* to invite (POST) or delete (DELETE) the assessor in a project assessment
* POST
```
{
    token: String
    projectName: String
    assessorEmail: String
}
```
* return
```
{
    invite_ACK: boolean
    assessorEmail: String
    sendMail_ACK: boolean
}
```

* DELETE
```
{
    token: String
    projectName: String
    assessorEmail: String
}
```
* return
```
{
    invite_ACK: boolean
    assessorEmail: String
    sendMail_ACK: boolean
}
```
### /MarkServlet
* to store the student's mark
* POST
```
{
    token: String
    projectName: String
    studentID: String
    mark: String     // actual type: Mark
    primaryEmail: String
}
```
* return
```
{
    mark_AC: boolean
}
```
### /SendEmailServlet
* Generate pdf result report for a student, and email the report, and delete the local pdf file
* POST
```
{
    token: String
    projectName: String
    studentID: String
    sendBoth: int
}
```
* return
```
{
    sendMail_ACK: boolean
}
```
### /UpdateProject_About_Servlet
* update project about info
* POST
```
{
    token: String
    projectName: String
    subjectName: String
    subjectCode: String
    description: String
}
```
* return
```
{
    updateProject_ACK: boolean
}
```
### /UpdateProject_Time_Servlet
* update the project time setting info
* POST
```
{
    token: String
    projectName: String
    durationMin: int
    durationSec: int
    warningMin: int
    warningSec: int
}
```
* return
```
{
    updateProject_ACK: boolean
}
```

### /SyncProjectListServlet
* update the project list
* POST
```
{
    token: String
    username: String
}
```
* return
```
{
    syn_ACK: boolean
    projectList: String
    token String
    firstname: String
}
```
