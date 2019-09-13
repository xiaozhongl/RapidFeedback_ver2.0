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