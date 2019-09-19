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
* response
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
* response
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
* response
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
* response
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
* response
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
* response
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
* response
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
* response
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
* response
```
{
    updateStudent_ACK: boolean
}
```
### /ImportStudentsServlet
* send a list of students of a project to server
* POST
```
{
    token: String
    projectName: String
    studentList: String
}
```
* response
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
* response
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
* response
```
{
    invite_ACK: boolean
    assessorEmail: String
    sendMail_ACK: boolean
}
```
### /MarkServlet
* send the student's mark to server
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
* response
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
* response
```
{
    sendMail_ACK: boolean
}
```
### /UpdateProject_About_Servlet
* require to edit project about info
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
* response
```
{
    updateProject_ACK: boolean
}
```
### /UpdateProject_Time_Servlet
* request to edit the project time setting info
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
* response
```
{
    updateProject_ACK: boolean
}
```

### /SyncProjectListServlet
* request for the project list
* POST
```
{
    token: String
    username: String
}
```
* response
```
{
    syn_ACK: boolean
    projectList: String
    token: String
    firstname: String
}
```

-----
### peojectList
```
{
     "assistant ": [], // usernames
     "commentList ": [],
     "criteria ": [
          {
               "markIncrement ": "null",
               "maximunMark ": 0,
               "name ": "",
               "subsectionList ": [],
               "weighting ": 0
          }
     ],
     "description ": " ",
     "durationMin ": , // int
     "durationSec ": , // int
     "projectName ": "",
     "studentInfo ": [
          {
               "email ": "",
               "firstName ": "",
               "group ": , //int
               "middleName ": "",
               "number ": "",
               "sendEmail ": false, boolean
               "surname ": "",
               "totalMark ": -999 // int
          }
     ],
     "subjectCode ": "",
     "subjectName ": "",
     "username ": "",
     "warningMin ": 0, // int
     "warningSec ": 0 // int
}
```
