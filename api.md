<!-- LoginServlet -->
POST
{
    username: String    // this key is named as email in RegisterServlet
    password:   String
}
response
{
    login_ACK: // >0 succeed/ 0: wrong password/ -1: no email address in database
    projectList: ArrayList<Project>
    firstName:  String
    id: int       <!-- new added -->
    token:  String
}


<!-- RegisterServlet -->
POST
{
    email:
    password:
    firstName:
    middleName:
    lastName:
}
response
{
    <!-- register_ACK: //true: success/ false: fail -->
    id: > 0 if success, 0 if database error, -1 if username exists   <!-- new added -->
}


<!-- AddStudentServlet -->

To add student into a project
POST
{
    token: String
    projectName: String
    studentID: String
    firstName: String
    middleName: String
    lastName:
    email: // student's email
}
response
{
    updateStudent_ACK: boolean
}

<!-- CriteriaListServlet -->

POST
{
    token: String
    projectId: String
    criteriaList: 
    
}
response
{
    updateProject_ACK: boolean
}


<!-- DeleteProjectServlet -->

POST 
{
    token: String
    projectId:
}
response
{
    updateProject_ACK: boolean
}

<!-- DeleteStudesntServlet -->

to delete a student of a specific project
POST
{
    token: String
    projectId: String
    studentId:   primary key
}
response
{
    updateStudent_ACK: boolean
}


<!-- EditStudentServlet -->
POST
{
    token: String
    <!-- projectName: String -->        <!-- deleted -->
    studentId: int    <!-- the primary key, not student number -->
    firstName: String
    middleName: String
    lastName:  String
    studentNumber: 
    email: String   // student's email
}
response
{
    updateStudent_ACK: boolean
}

<!-- GroupStudentServlet -->
POST
{
    token: String
    projectId:
    studentId: String
    group: int
}
response
{
    updateStudent_ACK: boolean
}

<!-- GetMarkServlet -->
POST
{
    token: String
    projectId: 
    studentNumberList: String
    primaryEmail: String
}
response
{
    getMark_ACK: boolean
    markList: String // if getMark_ACK == true
    otherComments: String // if studentNumberList.size > 1
}



<!-- InviteAssessorServlet -->
POST
{
    token: String
    projectId: int
    markerId: int
}
response
{
    invite_ACK: boolean
    markerName: 
}
DELETE
{
    token: String
    projectId: int
    markerId: int
}
response
{
    invite_ACK: boolean
}

<!-- AssessmentServlet-->
POST
{
    token: String
    projectId: String
    studentId: String
    markerId: String     // actual type: Mark
    remark: text
    assessmentList: ArrayList<Assessment>
}
response
{
    mark_AC: boolean
}

<!-- FinalResultServlet-->
POST
{
    token: String
    projectId: String
    studentId: String
    finalScore:
    finalRemark:
}
response
{
    mark_AC: boolean
}

/SendEmailServlet

POST
{
    token: String
    projectId: 
    studentId: 
    sendBoth: int
}
response
{
    sendMail_ACK: boolean
}

<!-- UpdateProject_About_Servlet -->

request to edit the project time setting info
POST
{
    token: String
    id:
    projectName: String
    subjectName: String
    subjectCode: String
    description: String
    durationSec: int
    warningSec: int
}
response
{
    updateProject_ACK: boolean
}

<!-- SyncProjectListServlet -->
request for the project list
POST
{
    token: String
    userId: 
}
response
{
    syn_ACK: boolean
    projectList: String
}