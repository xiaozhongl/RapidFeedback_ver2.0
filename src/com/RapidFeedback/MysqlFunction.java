package com.RapidFeedback;

import com.mysql.cj.conf.ConnectionPropertiesTransform;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName MysqlFunction
 * @Description All the functions which communicate with database.
 *
 * @author Xiaozhong Liu
 */
public class MysqlFunction {

	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=UTC&useSSL=false";

	static final String USER = "root";
	static final String PASS = "alfa1994";


	private Connection connectToDB(String url, String userName,
			String password) {
		Connection conn = null;
		try {
			// Register JDBC driver
			Class.forName(JDBC_DRIVER);
			// Connect the DB
			// System.out.println("Connecting the Database...");
			conn = DriverManager.getConnection(url, userName, password);

		} catch (Exception se) {
			// JDBC faults
			se.printStackTrace();
		}// Class.forName faults

		// System.out.println("Successfully Connected to DB...");
		return conn;
	}



//	public int logIn(String mail, String password) throws SQLException {
//		int num = -1;
//		Connection conn = null;
//		ResultSet rs = null;
//		Statement stmt = null;
//		try {
//			conn = connectToDB(DB_URL, USER, PASS);
//			stmt = conn.createStatement();
//			String sql;
//			sql = "SELECT * FROM Lecturers";
//			rs = stmt.executeQuery(sql);
//			System.out.println(sql);
//			while (rs.next()) {
//				if (rs.getString("email").equals(mail)) {
//					if (rs.getString("password").equals(password)) {
//						num = rs.getInt("idLecturers");
//					} else {
//						num = 0;
//					}
//				}
//			}
//		} catch (SQLException se) {
//			// JDBC faults
//			se.printStackTrace();
//			num = -2;
//		} finally {
//			disconnectDB(conn, stmt, rs);
//		}
//		return num;
//	}


	/**
	 * create by: Xiaozhong Liu
	 * description: user login function
	 * create time: 2019/10/17 10:37 PM
	 *
	 * @Param: email
	 * @Param: password
	 * @return markerId if login information correction; -1 if email does not exist; 0 if password is wrong
	 */
    public int logIn(String email, String password){
        int ack = -1;
        Connection connection;
        PreparedStatement statement;
        ResultSet rs;
        try {
            connection  = connectToDB(DB_URL, USER, PASS);
            statement = connection.prepareStatement("SELECT * FROM Marker WHERE email = ?");
            statement.setString(1, email);
            rs = statement.executeQuery();

            if (rs.next()) {
                if (rs.getString("password").equals(password)) {
                    ack = rs.getInt("id");
                } else {
                    ack = 0;
                }
            }
            connection.close();
        } catch (SQLException se) {
            // JDBC faults
            se.printStackTrace();
            ack = -2;
        }
        return ack;
    }

	/**
	 * create by: Xiaozhong Liu
	 * description: create a new marker in database
	 * create time: 2019/9/16 9:54 PM
	 *
	 * @Param: email
	 * @Param: password
	 * @Param: firstName
	 * @Param: middleName
	 * @Param: lastName
	 * @return the marker's id if add successfully;
	 * 		   0 if fail to create
	 * 		   -1 if email exist
	 */
	public int addMarker(String email, String password, String firstName,
							   String middleName, String lastName) {
		int id = 0;
		Connection connection;
		PreparedStatement addMarker;
		PreparedStatement getMarker;
		ResultSet rs;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			getMarker = connection.prepareStatement("SELECT * FROM Marker WHERE email= ?");
			addMarker = connection.prepareStatement(
					"INSERT INTO Marker(email, password, firstName, middleName, lastName) values(?,?,?,?,?)",
					PreparedStatement.RETURN_GENERATED_KEYS);

			// check if marker is already in database
			getMarker.setString(1, email);
			rs = getMarker.executeQuery();
			if (rs.next()) {
				return -1;
			}
			else {
				// create the marker in database
				addMarker.setString(1, email);
				addMarker.setString(2, password);
				addMarker.setString(3, firstName);
				addMarker.setString(4, middleName);
				addMarker.setString(5, lastName);
				addMarker.executeUpdate();
				rs = addMarker.getGeneratedKeys();
				if (rs.next()) {
					id = rs.getInt(1);
				}
			}
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return id;
	}


	/**
	 * create by: Xiaozhong Liu
	 * description: get a marker's full name given the marker's id
	 * create time: 2019/9/16 10:44 PM
	 *
	 * @Param: id
	 * @return the marker's full name if found; or null if not found
	 */
	public String getMarkerName(int id){
		String name = null;
		Connection connection;
		ResultSet rs;
		PreparedStatement statement;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement("SELECT * FROM Marker WHERE id= ?");
			statement.setInt(1, id);
			rs = statement.executeQuery();
			if (rs.next()){
				name = rs.getString("firstName") + " "
						+ rs.getString("middleName") + " "
						+ rs.getString("lastName");
			}
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return name;
	}

	public String getMarkerEmail(int id){
		String email = null;
		Connection connection;
		ResultSet rs;
		PreparedStatement statement;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement("SELECT * FROM Marker WHERE id= ?");
			statement.setInt(1, id);
			rs = statement.executeQuery();
			if (rs.next()){
				email = rs.getString("email");
			}
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return email;
	}

	public boolean isMarkerInProject(int markerId, int projectId){
		boolean isMarkerInProject = false;
		Connection connection;
		ResultSet rs;
		PreparedStatement statement;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement(
					"SELECT * FROM MarkerInProject WHERE idMarker = ? AND idProject = ?");
			statement.setInt(1, markerId);
			statement.setInt(2, projectId);
			rs = statement.executeQuery();
			if (rs.next()){
				isMarkerInProject = true;
			}
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return isMarkerInProject;
	}

	public boolean isMarkerPrincipal(int markerId, int projectId){
		boolean isMarkerPrincipal = false;
		Connection connection;
		ResultSet rs;
		PreparedStatement statement;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement(
					"SELECT * FROM Project WHERE idPrincipal = ? AND id = ?");
			statement.setInt(1, markerId);
			statement.setInt(2, projectId);
			rs = statement.executeQuery();
			if (rs.next()){
				isMarkerPrincipal = true;
			}
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return isMarkerPrincipal;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: create a project in database
	 * create time: 2019/9/15 8:18 PM
	 *
	 * @Param: principalId: principal marker's id
	 * @Param: projectName
	 * @Param: subjectCode
	 * @Param: subjectName
	 * @Param: duration	in second
	 * @Param: warning: time left in second when warning
	 * @Param: description of project
	 * @return project id or 0 if fail to create
	 */
	public int createProject(String projectName, String subjectName, String subjectCode,
				 	int duration, int warning, String description, int principalId) {
		Connection connection;
		PreparedStatement statement;
		ResultSet rs;
		String sql;
		int id = 0;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			sql = "INSERT INTO Project(`name`, subjectName, subjectCode, durationSec, " +
					"warningSec, description, idPrincipal) "
					+ "values(?,?,?,?,?,?,?);";
			statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			statement.setString(1, projectName);
			statement.setString(2, subjectName);
			statement.setString(3,subjectCode);
			statement.setInt(4,duration);
			statement.setInt(5, warning);
			statement.setString(6,description);
			statement.setInt(7,principalId);
			statement.executeUpdate();
			rs = statement.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return id;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: update the project information in database
	 * create time: 2019/9/15 8:28 PM
	 *
	 * @Param: projectId the primary key in Project table
	 * @Param: projectName
	 * @Param: subjectCode
	 * @Param: subjectName
	 * @Param: duration in second
	 * @Param: warning time left in second when warning
	 * @Param: description
	 * @return True if update successfully; False if fail to update
	 */
	public boolean updateProject(int projectId, String projectName, String subjectName,
								 String subjectCode, int duration,
								 int warning, String description) {
		boolean updated = false;
		Connection connection;
		PreparedStatement statement;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement(
					"UPDATE Project SET `name` = ?, subjectName = ?, subjectCode = ?, " +
							"durationSec = ?, warningSec = ?, description = ? WHERE id = ?");

			statement.setString(1, projectName);
			statement.setString(2, subjectName);
			statement.setString(3, subjectCode);
			statement.setInt(4, duration);
			statement.setInt(5, warning);
			statement.setString(6, description);
			statement.setInt(7, projectId);
			statement.executeUpdate();
			updated = true;
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return updated;
	}


	/**
	 * create by: Xiaozhong Liu
	 * description: soft delete a project from database
	 * create time: 2019/9/15 8:30 PM
	 *
	 * @Param: projectId the primary key in Project table
	 * @return True if delete successfully; False if fail to delete
	 */
	public boolean deleteProject(int projectId){
		boolean deleted = false;
		Connection connection;
		PreparedStatement statement;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement("UPDATE Project SET idPrincipal = 1 WHERE id = ?");
			statement.setInt(1, projectId);
			statement.executeUpdate();
			deleted = true;
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return deleted;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: assign a marker to a project
	 * create time: 2019/9/17 11:24 AM
	 *
	 * @Param: markerId
	 * @Param: projectId
	 * @return 1 if add successfully
	 * 		   0 if SQL Exception
	 * 		   -1 if marker id invalid
	 */
	public int addProjectMarker(int markerId, int projectId) {
		int ack = 0;
		Connection connection;
		PreparedStatement getMarker;
		PreparedStatement addMarker;
		ResultSet rs;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			getMarker = connection.prepareStatement("SELECT * FROM Marker WHERE id= ?");
			addMarker = connection.prepareStatement(
					"INSERT INTO MarkerInProject(idMarker, idProject) values(?,?)");


			getMarker.setInt(1, markerId);
			rs = getMarker.executeQuery();
			if (rs.next()){
				addMarker.setInt(1, markerId);
				addMarker.setInt(2, projectId);
				addMarker.executeUpdate();
				ack = 1;
			}
			else {
				ack = -1;
			}
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return ack;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: resign a marker from a project
	 * create time: 2019/9/17 11:30 AM
	 *
	 * @Param: markerId
	 * @Param: projectId
	 * @return true if resign successfully; false if not
	 */
	public boolean deleteProjectMarker(int markerId, int projectId) {
		boolean deleted = false;
		Connection connection;
		PreparedStatement statement;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement(
					"DELETE FROM MarkerInProject WHERE idProject = ? AND idMarker = ?");
			statement.setInt(1, projectId);
			statement.setInt(2, markerId);
			statement.executeUpdate();
			deleted = true;
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return deleted;
	}


	public int addStudent(String firstName, String middleName,String lastName,
				   int studentNumber, String email, int projectId) {
		int ack = 0;
		int id;
		Connection connection;
		HashMap<String,PreparedStatement> statements = new HashMap<String, PreparedStatement>();
		Student student;
		try {
			student = new Student(0, firstName, middleName, lastName, email, studentNumber);

			connection = connectToDB(DB_URL, USER, PASS);
			// prepare statements
			statements.put("getStudent", connection.prepareStatement(
					"SELECT * FROM Student WHERE studentNumber = ?"));

			statements.put("addStudent", connection.prepareStatement(
					"INSERT INTO Student(firstName, middleName, lastName, studentNumber, email) " +
							"VALUES (?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS));

			statements.put("addStudentToProject", connection.prepareStatement(
					"INSERT INTO StudentInProject(idProject, idStudent, `group`, finalScore, " +
							"finalRemark, ifEmailed, idAudio) values(?,?,?,?,?,?,?)"));

			statements.put("getStudentFromProject", connection.prepareStatement(
					"SELECT * FROM StudentInProject WHERE idProject = ? AND idStudent = ?"));

			// create student
			id = createStudent(statements, student);
			// add student to project
			if (id != 0) {
				if (!addStudentToProject(statements, projectId, id)) {
					// could only due to database error
					ack = -1 * student.getStudentNumber();
					connection.close();
					return ack;
				}
			}
			else{
				ack = -1 * student.getStudentNumber();
				connection.close();
				return ack;
			}
			ack = 1;
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return ack;
	}


	/**
	 * create by: Xiaozhong Liu
	 * description: add a list of students to a project
	 * create time: 2019/10/19 2:50 PM
	 *
	 * @Param: studentList
	 * @Param: projectId
	 * @return 1 if all students are added successfully
	 * 		   0 if SQL exception
	 *		   -studentNumber if the student information is illegal
	 */
	public int addStudents(ArrayList<Student> studentList,int projectId) {
		int ack = 0;
		int id;
		Connection connection;
		HashMap<String,PreparedStatement> statements = new HashMap<String, PreparedStatement>();

		try {
			connection = connectToDB(DB_URL, USER, PASS);

			// prepare statements
			statements.put("getStudent", connection.prepareStatement(
							"SELECT * FROM Student WHERE studentNumber = ?"));

			statements.put("addStudent", connection.prepareStatement(
					"INSERT INTO Student(firstName, middleName, lastName, studentNumber, email) " +
							"VALUES (?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS));

			statements.put("addStudentToProject", connection.prepareStatement(
					"INSERT INTO StudentInProject(idProject, idStudent, `group`, finalScore, " +
							"finalRemark, ifEmailed, idAudio) values(?,?,?,?,?,?,?)"));

			statements.put("getStudentFromProject", connection.prepareStatement(
					"SELECT * FROM StudentInProject WHERE idProject = ? AND idStudent = ?"));

			for (Student student : studentList) {
				// create student
				id = createStudent(statements, student);
				// add student to project
				if (id != 0) {
					if (!addStudentToProject(statements, projectId, id)) {
						// could only due to database error
						ack = -1 * student.getStudentNumber();
						connection.close();
						return ack;
					}
				}
				else{
					ack = -1 * student.getStudentNumber();
					connection.close();
					return ack;
				}
			}
			ack = 1;
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return ack;
	}

	private int createStudent(HashMap<String,PreparedStatement> statements, Student student){
		int id = 0;
		PreparedStatement getStudent = statements.get("getStudent");
		PreparedStatement addStudent = statements.get("addStudent");
		ResultSet rs;
		try{
			//check if student exists
			getStudent.setInt(1, student.getStudentNumber());
			rs = getStudent.executeQuery();
			if (rs.next()) {
				id = rs.getInt("id");
			}
			else{
				// add student to database
				addStudent.setString(1, student.getFirstName());
				addStudent.setString(2, student.getMiddleName());
				addStudent.setString(3, student.getLastName());
				addStudent.setInt(4, student.getStudentNumber());
				addStudent.setString(5, student.getEmail());
				addStudent.executeUpdate();
				rs = addStudent.getGeneratedKeys();
				if(rs.next()){
					id = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return id;
	}

	private boolean addStudentToProject(HashMap<String,PreparedStatement> statements, int projectId, int studentId){
//		"SELECT * FROM StudentInProject WHERE idProject = ? AND idStudent = ?"
//		"INSERT INTO StudentInProject(idProject, idStudent, group, finalScore, " +
//				"finalRemark, ifEmailed, idAudio) values(?,?,?,?,?,?,?)");
		PreparedStatement getStudentFromProject = statements.get("getStudentFromProject");
		PreparedStatement addStudentToProject = statements.get("addStudentToProject");
		ResultSet rs;
		try {
			//check if student already in the project
			getStudentFromProject.setInt(1, projectId);
			getStudentFromProject.setInt(2, studentId);
			rs = getStudentFromProject.executeQuery();
			if (rs.next()) {
				return true;
			}
			else{
				// add the student to the project
				addStudentToProject.setInt(1, projectId);
				addStudentToProject.setInt(2, studentId);
				addStudentToProject.setInt(3, 0);
				addStudentToProject.setDouble(4, -1);
				addStudentToProject.setString(5, "");
				addStudentToProject.setInt(6, 0);
				addStudentToProject.setInt(7, 1);
				addStudentToProject.executeUpdate();
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			return false;
		}
		return true;
	}

	//	/**
//	 * create by: Xiaozhong Liu
//	 * description: check if the given student is in the given project
//	 * create time: 2019/9/17 9:26 AM
//	 *
//	 * @Param: idProject
//	 * @Param: idStudent
//	 * @return true if the student is in the project; false if not
//	 */
//		public boolean ifStudentInProject(int idProject, int idStudent)
//			throws SQLException {
//		boolean studentInProject = false;
//		Connection conn = null;
//		ResultSet rs = null;
//		Statement stmt = null;
//		String sql;
//		try {
//			conn = connectToDB(DB_URL, USER, PASS);
//			stmt = conn.createStatement();
//			sql = "SELECT * FROM StudentInProject " +
//					"WHERE idProject = "+ idProject +" " +
//					"AND idStudent = "+ idStudent +";";
//			rs = stmt.executeQuery(sql);
//			System.out.println(sql);
//			if (rs.next()) {
//				studentInProject = true;
//			}
//		} catch (SQLException se) {
//			// JDBC faults
//			se.printStackTrace();
//		} finally {
//			disconnectDB(conn, stmt, rs);
//		}
//		return studentInProject;
//	}

	/**
	 * create by: Xiaozhong Liu
	 * description: update a student's information
	 * create time: 2019/9/15 11:20 PM
	 *
	 * @Param: studentId
	 * @Param: studentNumber
	 * @Param: firstName
	 * @Param: lastName
	 * @Param: middleName
	 * @Param: email
	 * @return true if updated successfully; false if not
	 */
	public boolean updateStudent(int studentId, int studentNumber, String firstName, String lastName,
								 String middleName, String email){
		if (studentId == 1){
			return false;
		}
		boolean updated = false;
		Connection connection;
		PreparedStatement statement;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement(
					"UPDATE Student SET firstName = ?, middleName = ?, lastName = ?, studentNumber = ?, " +
							"email = ? WHERE id = ?");
			statement.setString(1, firstName);
			statement.setString(2, middleName);
			statement.setString(3, lastName);
			statement.setInt(4, studentNumber);
			statement.setString(5, email);
			statement.setInt(6, studentId);
			statement.executeUpdate();
			updated = true;

			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return updated;
	}


	/**
	 * create by: Xiaozhong Liu
	 * description: update the group number of students in a project
	 * create time: 2019/9/15 9:49 PM
	 *
	 * @Param: projectId
	 * @Param: studentId
	 * @Param: group
	 * @return True if update successfully; False if fail to update
	 */
	public boolean updateGroupNumber(int projectId, int studentId, int group) {
		boolean updated = false;
		Connection connection;
		PreparedStatement statement;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement(
					"UPDATE StudentInProject SET `group` = ? WHERE idProject = ? AND idStudent= ?");
			statement.setInt(1, group);
			statement.setInt(2, projectId);
			statement.setInt(3, studentId);
			statement.executeUpdate();
			updated = true;

			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return updated;
	}


	/**
	 * create by: Xiaozhong Liu
	 * description: update final result of a student
	 * create time: 2019/9/15 10:27 PM
	 *
	 * @Param: projectId
	 * @Param: studentId
	 * @Param: score
	 * @Param: remark
	 * @return True if update successfully; False if fail to update
	 */
	public boolean updateFinalResult(int projectId, int studentId, double finalScore,
						String finalRemark) {
		boolean updated = false;
		Connection connection;
		PreparedStatement statement;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement(
					"UPDATE StudentInProject SET finalScore = ?, finalRemark = ?, ifEmailed = ? " +
							"WHERE idProject = ? AND idStudent = ?");
			statement.setDouble(1, finalScore);
			statement.setString(2, finalRemark);
			statement.setInt(3, 1);
//			statement.setInt(4, audioId);
			statement.setInt(4, projectId);
			statement.setInt(5, studentId);
			statement.executeUpdate();
			updated = true;
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return updated;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: delete a student from a project
	 * create time: 2019/9/15 10:33 PM
	 *
	 * @Param: projectId
	 * @Param: studentNumber
	 * @return True if delete successfully; False if fail to delete
	 */
	public boolean deleteStudentFromProject(int projectId, int studentId) {
		boolean deleted = false;
		Connection connection;
		PreparedStatement deleteFromProject;
		PreparedStatement deleteAssessment;

		try {
			connection = connectToDB(DB_URL, USER, PASS);
			deleteFromProject = connection.prepareStatement(
					"DELETE FROM StudentInProject WHERE idProject = ? AND idStudent = ?");
			deleteAssessment = connection.prepareStatement(
					"DELETE FROM Assessment WHERE idProject = ? AND idStudent = ?");

			deleteFromProject.setInt(1, projectId);
			deleteFromProject.setInt(2, studentId);
			deleteFromProject.executeUpdate();

			deleteAssessment.setInt(1, projectId);
			deleteAssessment.setInt(2, studentId);
			deleteAssessment.executeUpdate();

			deleted = true;
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return deleted;
	}



	/**
	 * create by: Xiaozhong Liu
	 * description: add the result for a student in a project from a marker
	 * create time: 2019/10/5 11:20 AM
	 *
	 * @Param: remark
	 * @Param: projectId
	 * @Param: studentId
	 * @return if successfully; if not
	 */
	public boolean addResult(Remark remark, int projectId, int studentId){
		boolean added = false;
		Connection connection;
		HashMap<String,PreparedStatement> statements = new HashMap<String, PreparedStatement>();
//		ArrayList<PreparedStatement> statements = new ArrayList<PreparedStatement>();
		try {

			connection = connectToDB(DB_URL, USER, PASS);

			// prepare statements
			statements.put("addRemark", connection.prepareStatement(
					"INSERT INTO Remark(idProject, idStudent, idMarker, text) values(?,?,?,?)"));
			statements.put("addAssessment", connection.prepareStatement(
					"INSERT INTO Assessment(idProject, idCriterion, idStudent, idMarker, score) " +
							"VALUES (?,?,?,?,?)"));
			statements.put("selectComment", connection.prepareStatement(
					"INSERT INTO SelectedComment(idProject, idCriterion, idStudent, idMarker, " +
							"idField, idExpandedComment) VALUES (?,?,?,?,?,?)"));

			if(addRemark(statements, remark, projectId, studentId)){
				added = true;
			}
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return added;
	}

	private boolean addRemark(HashMap<String,PreparedStatement> statements, Remark remark,
							  int projectId, int studentId) {
//		"INSERT INTO Remark(idProject, idStudent, idMarker, text) values(?,?,?,?)";
		boolean added = false;
		PreparedStatement statement = statements.get("addRemark");
		try {
			statement.setInt(1, projectId);
			statement.setInt(2, studentId);
			statement.setInt(3, remark.getId());
			statement.setString(4, remark.getText());
			statement.executeUpdate();
			for (int i = 0; i < remark.getAssessmentList().size(); i++) {
				if (!addAssessment(statements, remark.getAssessmentList().get(i),
						projectId, studentId, remark.getId())){
					return false;
				}
			}
			added = true;
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return added;
	}

	private boolean addAssessment(HashMap<String,PreparedStatement> statements, Assessment assessment,
								  int projectId, int studentId, int markerId){
//		"INSERT INTO Assessment(idProject, idCriterion, idStudent, idMarker, score) VALUES (?,?,?,?,?)"
		boolean added = false;
		PreparedStatement statement = statements.get("addAssessment");
		try {
			statement.setInt(1, projectId);
			statement.setInt(2, assessment.getCriterionId());
			statement.setInt(3, studentId);
			statement.setInt(4, markerId);
			statement.setDouble(5, assessment.getScore());
			statement.executeUpdate();

			for (int i = 0; i < assessment.getSelectedCommentList().size(); i++) {
				if (!selectComment(statements.get("selectComment"),
						assessment.getSelectedCommentList().get(i),
						projectId, assessment.getCriterionId(),
						studentId, markerId)){
					return false;
				}
			}
			added = true;
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return added;
	}

		private boolean selectComment(PreparedStatement statement, SelectedComment selComment,
									  int projectId, int criterionId, int studentId, int markerId){
//			"INSERT INTO SelectedComment(idProject, idCriterion, idStudent, idMarker, idField,
//				idExpandedComment) VALUES (?,?,?,?,?,？)"
		boolean selected = false;
		try {
			statement.setInt(1, projectId);
			statement.setInt(2, criterionId);
			statement.setInt(3, studentId);
			statement.setInt(4, markerId);
			statement.setInt(5, selComment.getFieldId());
			statement.setInt(6, selComment.getExCommentId());
			statement.executeUpdate();
			selected = true;
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return selected;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: Add a list of criterion to the given project
	 * create time: 2019/10/5 10:25 AM
	 *
	 * @Param: criterionList
	 * @Param: projectId
	 * @return if successfully; if not
	 */
	public boolean addCriteria(ArrayList<Criterion> criterionList, int projectId){
		boolean allAdded = false;
		Connection connection;
		// contains five prepared statements [addCriterion, addField, addComment, addExpandedComment, addToProject]
		ArrayList<PreparedStatement> statements = new ArrayList<PreparedStatement>();
		try {
			connection = connectToDB(DB_URL, USER, PASS);

			// prepare statements
			statements.add(connection.prepareStatement(
					"INSERT INTO Criterion(`name`) values(?)",
					PreparedStatement.RETURN_GENERATED_KEYS));
			statements.add(connection.prepareStatement(
					"INSERT INTO Field(`name`, idCriterion) values(?,?)",
					PreparedStatement.RETURN_GENERATED_KEYS));
			statements.add(connection.prepareStatement(
					"INSERT INTO Comment(text, idField, `type`) values(?,?,?)",
					PreparedStatement.RETURN_GENERATED_KEYS));
			statements.add(connection.prepareStatement(
					"INSERT INTO ExpandedComment(text, idComment) values(?,?)",
					PreparedStatement.RETURN_GENERATED_KEYS));
			statements.add(connection.prepareStatement(
					"INSERT INTO ProjectCriterion(idProject, idCriterion, " +
							"maximumMark, weight, markIncrement)" +
							" values(?,?,?,?,?)"));

			// add criteria
			for (Criterion criterion : criterionList) {
				if (addCriterion(statements, criterion, projectId) == 0) {
					connection.close();
					return false;
				}
			}
			allAdded = true;
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return allAdded;
}

	private int addCriterion(ArrayList<PreparedStatement> statements, Criterion criterion, int projectId) {
//		"INSERT INTO Criterion(name) values(?)"
		int id = 0;
		PreparedStatement addCriterion = statements.get(0);
		PreparedStatement addToProject = statements.get(4);
		ResultSet rs;
		try {
			// create a criterion
			addCriterion.setString(1, criterion.getName());
			addCriterion.executeUpdate();
			rs = addCriterion.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);

				// add fields
				for (int i = 0; i < criterion.getFieldList().size(); i++) {
					if(addField(statements, criterion.getFieldList().get(i), id) == 0){
						return 0;
					}
				}

				// add the criterion to the project
				if (!addProjectCriterion(addToProject, projectId, id, criterion.getMaximumMark(),
						criterion.getWeight(), criterion.getMarkIncrement())){
					return 0;
				}
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return id;
	}

	private boolean addProjectCriterion(PreparedStatement statement, int projectId, int criterionId,
										double maximumMark, double weight,
										double markIncrement) {
//		"INSERT INTO ProjectCriterion(idProject, idCriterion, maximumMark, weight, markIncrement)
//			values(?,?,?,?,?)"
		boolean added = false;
		try {
			statement.setInt(1, projectId);
			statement.setInt(2, criterionId);
			statement.setDouble(3, maximumMark);
			statement.setDouble(4, weight);
			statement.setDouble(5, markIncrement);
			statement.executeUpdate();
			added = true;
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return added;
	}

	private int addField(ArrayList<PreparedStatement> statements, Field field, int criterionId){
//		"INSERT INTO Field(name, idCriterion) values(?,?)"
		int id = 0;
		ResultSet rs;
		PreparedStatement addField = statements.get(1);
		try {
			addField.setString(1, field.getName());
			addField.setInt(2, criterionId);
			addField.executeUpdate();
			rs = addField.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);

				// add comments
				for (int i = 0; i < field.getCommentList().size(); i++) {
					if (addComment(statements, field.getCommentList().get(i), id) == 0){
						return 0;
					}
				}
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return id;
	}


	private int addComment(ArrayList<PreparedStatement> statements, Comment comment, int fieldId){
//		"INSERT INTO Comment(text, idField, type) values(?,?,?)"
		int id = 0;
		ResultSet rs;
		PreparedStatement addComment = statements.get(2);
		try {
			addComment.setString(1, comment.getText());
			addComment.setInt(2, fieldId);
			addComment.setString(3, comment.getType());
			addComment.executeUpdate();
			rs = addComment.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);

				// add expanded comments
				for (int i = 0; i < comment.getExpandedCommentList().size(); i++) {
					if (addExpandedComment(statements, id, comment.getExpandedCommentList().get(i).getText())== 0){
						return 0;
					}
				}
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return id;
	}


	private int addExpandedComment(ArrayList<PreparedStatement> statements, int idComment, String text){
//		"INSERT INTO ExpandedComment(text, idComment) values(?,?)"
		int id = 0;
		ResultSet rs;
		PreparedStatement addExComment = statements.get(3);
		try {
			addExComment.setString(1, text);
			addExComment.setInt(2, idComment);
			addExComment.executeUpdate();
			rs = addExComment.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return id;
	}

//	/**
//	 * create by: Xiaozhong Liu
//	 * description: delete a private criterion
//	 * create time: 2019/9/20 8:01 AM
//	 *
//	 * @Param: criterionId
//	 * @Param: authorId
//	 * @return true if delete successfully; false if not
//	 */
//	public boolean deleteCriterion(int criterionId) throws SQLException {
//		boolean modified = false;
//		Connection conn = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//		String sql;
//		try {
//			conn = connectToDB(DB_URL, USER, PASS);
//			stmt = conn.createStatement();
//			sql = "DELETE FROM Criterion " +
//					"WHERE id = "+ criterionId +";";
//			stmt.executeUpdate(sql);
//			modified = true;
//			System.out.println(sql);
//		} catch (SQLException se) {
//			// JDBC faults
//			se.printStackTrace();
//		} finally {
//			disconnectDB(conn, stmt);
//		}
//		return modified;
//	}


	/**
	 * create by: Xiaozhong Liu
	 * description: delete all criteria for a project, used before add criteria when updating project criterion
	 * create time: 2019/9/17 2:22 PM
	 *
	 * @Param: projectId
	 * @return true if delete successfully; false if not
	 */
	public boolean deleteCriteria(int projectId) {
		boolean deleted = false;
		Connection connection;
		PreparedStatement statement;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement("DELETE FROM ProjectCriterion WHERE idProject = ?");
			statement.setInt(1, projectId);
			statement.executeUpdate();
			deleted = true;

			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return deleted;
	}

	public String getCriteriaName(int id) {
		String name = "";
		Connection connection;
		PreparedStatement statement;
		ResultSet resultSet;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement("SELECT * FROM Criterion WHERE id = ?");
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			if (resultSet.next()){
				name = resultSet.getString("name");
			}
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return name;
	}

	public String getFieldName(int id) {
		String name = "";
		Connection connection;
		PreparedStatement statement;
		ResultSet resultSet;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement("SELECT * FROM Field WHERE id = ?");
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			if (resultSet.next()){
				name = resultSet.getString("name");
			}
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return name;
	}

	public String getExpandedCommentText(int id) {
		String text = "";
		Connection connection;
		PreparedStatement statement;
		ResultSet resultSet;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement("SELECT * FROM ExpandedComment WHERE id = ?");
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			if (resultSet.next()){
				text = resultSet.getString("text");
			}
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return text;
	}

//	/**
//	 * create by: Xiaozhong Liu
//	 * description: update a criterion in a project
//	 * create time: 2019/9/19 7:25 PM
//	 *
//	 * @Param: idProject
//	 * @Param: idCriterion
//	 * @Param: maxMark
//	 * @Param: weight
//	 * @Param: markIncrement
//	 * @return if successfully; if not
//	 */
//	public boolean updateProjectCriterion(int idProject, int idCriterion, BigDecimal maximumMark,
//									   BigDecimal weight, BigDecimal markIncrement) throws SQLException {
//		boolean updated = false;
//		Connection conn = null;
//		Statement stmt = null;
//		String sql;
//		try {
//			conn = connectToDB(DB_URL, USER, PASS);
//			stmt = conn.createStatement();
//			sql = "UPDATE ProjectCriterion SET " +
//					"maximumMark = " + maximumMark + ", " +
//					"weight = " + weight + ", " +
//					"markIncrement = " + markIncrement  + ", " +
//					"WHERE idProject = " + idProject + " " +
//					"AND idCriterion = " + idCriterion + ";";
//			stmt.executeUpdate(sql);
//			System.out.println(sql);
//			updated = true;
//		} catch (SQLException se) {
//			// JDBC faults
//			se.printStackTrace();
//		} finally {
//			disconnectDB(conn, stmt);
//		}
//		return updated;
//	}



//	/**
//	 * create by: Xiaozhong Liu
//	 * description: update a field in project
//	 * create time: 2019/9/19 6:07 PM
//	 *
//	 * @Param: id
//	 * @Param: name
//	 * @return true if update successfully; false if not
//	 */
//	public boolean updateProjectField(int id, String name)
//			throws SQLException {
//		boolean updated = false;
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		String sql;
//		try {
//			conn = connectToDB(DB_URL, USER, PASS);
//			sql = "UPDATE ProjectField SET text = ? " +
//					"WHERE id = ?;";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, name);
//			pstmt.setInt(2, id);
//			pstmt.executeUpdate();
//			System.out.println(sql);
//			updated = true;
//		} catch (SQLException se) {
//			// JDBC faults
//			se.printStackTrace();
//		} finally {
//			disconnectDB(conn, pstmt);
//		}
//		return updated;
//	}


	/**
	 * create by: Xiaozhong Liu
	 * description: get a list of project with the marker involved
	 * create time: 2019/10/5 11:21 AM
	 *
	 * @Param: markerId
	 * @return if successfully; if not
	 */
	public ArrayList<Project> getProjectList (int markerId){
		ArrayList<Project> projectList = new ArrayList<Project>();
		Connection connection;
		HashMap<String,PreparedStatement> statements = new HashMap<String,PreparedStatement>();
		PreparedStatement getProjects;
		ResultSet rs;
		Project project;
		try {
			connection = connectToDB(DB_URL, USER, PASS);

			// prepare statements
			statements.put("getProjects", connection.prepareStatement(
					"SELECT * FROM MarkerInProject RIGHT JOIN Project " +
							"ON MarkerInProject.idProject = Project.id " +
							"WHERE idMarker = ? OR idPrincipal = ?"));
			statements.put("getCriteria", connection.prepareStatement(
					"SELECT * FROM ProjectCriterion INNER JOIN Criterion " +
							"ON ProjectCriterion.idCriterion = Criterion.id " +
							"WHERE idProject = ?"));
			statements.put("getFields", connection.prepareStatement(
					"SELECT * FROM Field WHERE idCriterion = ?"));
			statements.put("getComments", connection.prepareStatement(
					"SELECT * FROM Comment WHERE idField = ?"));
			statements.put("getExComments", connection.prepareStatement(
					"SELECT * FROM ExpandedComment WHERE idComment = ?"));
			statements.put("getStudents", connection.prepareStatement(
					"SELECT * FROM StudentInProject INNER JOIN Student " +
							"ON StudentInProject.idStudent = Student.id " +
							"WHERE idProject = ?"));
			statements.put("getStudentMarkers", connection.prepareStatement(
					"SELECT * FROM Remark WHERE idProject = ? AND idStudent = ?"));
			statements.put("getAssessments", connection.prepareStatement(
					"SELECT * FROM Assessment WHERE idProject = ? AND idStudent = ? AND idMarker = ?"));
			statements.put("getSelectedComments", connection.prepareStatement(
					"SELECT * FROM SelectedComment " +
							"WHERE idProject = ? AND idCriterion = ? AND idStudent = ? AND idMarker = ?"));
			statements.put("getMarkers", connection.prepareStatement(
					"SELECT * FROM MarkerInProject INNER JOIN Marker " +
							"ON MarkerInProject.idMarker = Marker.id " +
							"WHERE idProject = ?"));
			statements.put("getPrincipal", connection.prepareStatement(
					"SELECT * FROM Project INNER JOIN Marker " +
							"ON Project.idPrincipal = Marker.id " +
							"WHERE Project.id = ?"));

			// get projects of a given marker, either be principal or normal marker
			getProjects = statements.get("getProjects");
			getProjects.setInt(1, markerId);
			getProjects.setInt(2, markerId);
			rs = getProjects.executeQuery();
			while (rs.next()) {
				project = new Project(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getString("subjectName"),
						rs.getString("subjectCode"),
						rs.getInt("durationSec"),
						rs.getInt("warningSec"),
						rs.getInt("idPrincipal"),
						rs.getString("description"));

				project.setCriterionList(getCriterionList(statements, rs.getInt("id")));
				project.setMarkerList(getMarkerList(statements, rs.getInt("id")));
				project.setStudentList(getStudentList(statements, rs.getInt("id")));
				projectList.add(project);
			}
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return projectList;
	}

	public Project getProject (int projectId){
		Connection connection;
		PreparedStatement statement;
		ResultSet rs;
		Project project = null;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement("SELECT * FROM Project WHERE id = ?");
			statement.setInt(1, projectId);
			rs = statement.executeQuery();
			if (rs.next()) {
				project = new Project(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getString("subjectName"),
						rs.getString("subjectCode"),
						rs.getInt("durationSec"),
						rs.getInt("warningSec"),
						rs.getInt("idPrincipal"),
						rs.getString("description"));
			}
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return project;
	}

	public ProjectStudent getProjectStudent (int studentId, int projectId){
		Connection connection;
		HashMap<String,PreparedStatement> statements = new HashMap<String,PreparedStatement>();
		PreparedStatement getStudent;
		ResultSet rs;
		ProjectStudent student = null;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			// prepare statements
			statements.put("getStudent", connection.prepareStatement(
					"SELECT * FROM StudentInProject INNER JOIN Student " +
							"ON StudentInProject.idStudent = Student.id " +
							"WHERE idProject = ? AND idStudent = ?"));
			statements.put("getStudentMarkers", connection.prepareStatement(
					"SELECT * FROM Remark WHERE idProject = ? AND idStudent = ?"));
			statements.put("getAssessments", connection.prepareStatement(
					"SELECT * FROM Assessment WHERE idProject = ? AND idStudent = ? AND idMarker = ?"));
			statements.put("getSelectedComments", connection.prepareStatement(
					"SELECT * FROM SelectedComment " +
							"WHERE idProject = ? AND idCriterion = ? AND idStudent = ? AND idMarker = ?"));


			// get projects of a given marker, either be principal or normal marker
			getStudent = statements.get("getStudent");
			getStudent.setInt(1, projectId);
			getStudent.setInt(2, studentId);
			rs = getStudent.executeQuery();
			if (rs.next()) {
				student = new ProjectStudent(
						rs.getInt("id"),
						rs.getString("firstName"),
						rs.getString("middleName"),
						rs.getString("lastName"),
						rs.getInt("studentNumber"),
						rs.getString("email"),
						rs.getInt("group"),
						rs.getDouble("finalScore"),
						rs.getString("finalRemark"),
						rs.getInt("ifEmailed"),
						rs.getInt("idAudio"));
				student.setRemarkList(getRemarkList(statements, projectId, rs.getInt("id")));
			}
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return student;
	}

	private ArrayList<Marker> getMarkerList(HashMap<String,PreparedStatement> statements,
											int projectId) throws SQLException {
//		"SELECT * FROM MarkerInProject INNER JOIN Marker " +
//				"ON MarkerInProject.idMarker = Marker.id " +
//				"WHERE idProject = ?"
		ArrayList<Marker> markerList = new ArrayList<Marker>();
		PreparedStatement statement = statements.get("getMarkers");
		PreparedStatement getPrincipal = statements.get("getPrincipal");
		ResultSet rs;
		Marker marker;
		try {
			statement.setInt(1, projectId);
			rs = statement.executeQuery();
			while (rs.next()) {
				marker = new Marker(
						rs.getInt("id"),
						rs.getString("email"),
						rs.getString("firstName"),
						rs.getString("middleName"),
						rs.getString("lastName"));
				markerList.add(marker);
			}

			getPrincipal.setInt(1, projectId);
			rs = getPrincipal.executeQuery();
			if (rs.next()) {
				marker = new Marker(
						rs.getInt("idPrincipal"),
						rs.getString("email"),
						rs.getString("firstName"),
						rs.getString("middleName"),
						rs.getString("lastName"));
				markerList.add(marker);
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return markerList;
	}




	private ArrayList<ProjectStudent> getStudentList(HashMap<String,PreparedStatement> statements, int projectId){
//		"SELECT * FROM StudentInProject INNER JOIN Student ON StudentInProject.idStudent = Student.id " +
//				"WHERE idProject = ?"
		ArrayList<ProjectStudent> studentList = new ArrayList<ProjectStudent>();
		PreparedStatement getStudents = statements.get("getStudents");
		ResultSet rs;
		ProjectStudent student;
		try {
			getStudents.setInt(1, projectId);
			rs = getStudents.executeQuery();
			while (rs.next()) {
				student = new ProjectStudent(
						rs.getInt("id"),
						rs.getString("firstName"),
						rs.getString("middleName"),
						rs.getString("lastName"),
						rs.getInt("studentNumber"),
						rs.getString("email"),
						rs.getInt("group"),
						rs.getDouble("finalScore"),
						rs.getString("finalRemark"),
						rs.getInt("ifEmailed"),
						rs.getInt("idAudio"));
				student.setRemarkList(getRemarkList(statements, projectId, rs.getInt("id")));
				studentList.add(student);
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return studentList;
	}

	private ArrayList<Remark> getRemarkList(HashMap<String,PreparedStatement> statements,
														  int projectId, int studentId){
//		"SELECT * FROM Remark WHERE idProject = ? AND idStudent = ?"
		ArrayList<Remark> remarkList = new ArrayList<Remark>();
		PreparedStatement statement = statements.get("getStudentMarkers");
		ResultSet rs;
		Remark remark;
		try {
			statement.setInt(1, projectId);
			statement.setInt(2, studentId);
			rs = statement.executeQuery();
			while (rs.next()) {
				remark = new Remark(
						rs.getInt("idMarker"),
						rs.getString("text"));

				remark.setAssessmentList(getAssessmentList(
						statements, projectId, studentId, rs.getInt("idMarker")));
				remarkList.add(remark);
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return remarkList;
	}

	private ArrayList<Assessment> getAssessmentList(HashMap<String,PreparedStatement> statements, int projectId,
													int studentId, int markerId){
//		"SELECT * FROM Assessment WHERE idProject = ? AND idStudent = ? AND idMarker = ?"
		ArrayList<Assessment> assessmentList = new ArrayList<Assessment>();
		PreparedStatement statement = statements.get("getAssessments");
		ResultSet rs;
		Assessment assessment;
		try {
			statement.setInt(1, projectId);
			statement.setInt(2, studentId);
			statement.setInt(3, markerId);
			rs = statement.executeQuery();
			while (rs.next()) {
				assessment = new Assessment(
						rs.getInt("idCriterion"),
						rs.getDouble("score"));
				assessment.setSelectedCommentList(getSelectedCommentList(statements, projectId,
						rs.getInt("idCriterion"), studentId, markerId));
				assessmentList.add(assessment);
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return assessmentList;
	}

	private ArrayList<SelectedComment> getSelectedCommentList(HashMap<String,PreparedStatement> statements, int projectId,
														 int criterionId, int studentId, int markerId){
//		"SELECT * FROM SelectedComment WHERE idProject = ? AND idCriterion = ? AND idStudent = ? AND idMarker = ?"
		ArrayList<SelectedComment> selCommentList = new ArrayList<SelectedComment>();
		PreparedStatement statement = statements.get("getSelectedComments");
		ResultSet rs;
		SelectedComment selectedComment;
		try {
			statement.setInt(1, projectId);
			statement.setInt(2, criterionId);
			statement.setInt(3, studentId);
			statement.setInt(4, markerId);
			rs = statement.executeQuery();
			while (rs.next()) {
				selectedComment = new SelectedComment(
						rs.getInt("idField"),
						rs.getInt("idExpandedComment"));
				selCommentList.add(selectedComment);
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return selCommentList;
	}


	private ArrayList<Criterion> getCriterionList(HashMap<String,PreparedStatement> statements, int projectId){
//		"SELECT * FROM ProjectCriterion INNER JOIN Criterion ON ProjectCriterion.idCriterion = Criterion.id " +
//				"WHERE idProject = ?"
		ArrayList<Criterion> criterionList = new ArrayList<Criterion>();
		PreparedStatement getCriteria = statements.get("getCriteria");
		ResultSet rs;
		Criterion criterion;
		try {
			getCriteria.setInt(1, projectId);
			rs = getCriteria.executeQuery();
			while (rs.next()) {
				criterion = new Criterion(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getDouble("weight"),
						rs.getDouble("maximumMark"),
						rs.getDouble("markIncrement"));
				criterion.setFieldList(getFieldList(statements, rs.getInt("id")));
				criterionList.add(criterion);
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return criterionList;
	}

//	public ArrayList<Criterion> getCriterionList(int projectId){
////		"SELECT * FROM ProjectCriterion INNER JOIN Criterion ON ProjectCriterion.idCriterion = Criterion.id " +
////				"WHERE idProject = ?"
//		Connection connection;
//		HashMap<String,PreparedStatement> statements = new HashMap<String,PreparedStatement>();
//		ArrayList<Criterion> criterionList = new ArrayList<Criterion>();
//		PreparedStatement getCriteria = statements.get("getCriteria");
//		ResultSet rs;
//		Criterion criterion;
//
//		try {
//			connection = connectToDB(DB_URL, USER, PASS);
//			statements.put("getFields", connection.prepareStatement(
//					"SELECT * FROM Field WHERE idCriterion = ?"));
//			statements.put("getComments", connection.prepareStatement(
//					"SELECT * FROM Comment WHERE idField = ?"));
//			statements.put("getExComments", connection.prepareStatement(
//					"SELECT * FROM ExpandedComment WHERE idComment = ?"));
//			getCriteria.setInt(1, projectId);
//			rs = getCriteria.executeQuery();
//			while (rs.next()) {
//				criterion = new Criterion(
//						rs.getInt("id"),
//						rs.getString("name"),
//						rs.getDouble("weight"),
//						rs.getDouble("maximumMark"),
//						rs.getDouble("markIncrement"));
//				criterion.setFieldList(getFieldList(statements, rs.getInt("id")));
//				criterionList.add(criterion);
//			}
//		} catch (SQLException e) {
//			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
//		}
//		return criterionList;
//	}

	private ArrayList<Field> getFieldList(HashMap<String,PreparedStatement> statements, int criterionId){
//		"SELECT * FROM Field WHERE idCriterion = ?")
		ArrayList<Field> fieldList = new ArrayList<Field>();
		PreparedStatement getFields = statements.get("getFields");
		ResultSet rs;
		Field field;
		try {
			getFields.setInt(1, criterionId);
			rs = getFields.executeQuery();
			while (rs.next()) {
				field = new Field(rs.getInt("id"), rs.getString("name"));
				field.setCommentList(getCommentList(statements, rs.getInt("id")));
				fieldList.add(field);
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return fieldList;
	}

	private ArrayList<Comment> getCommentList(HashMap<String,PreparedStatement> statements, int fieldId){
//		"SELECT * FROM Comment WHERE idField = ?"
		ArrayList<Comment> commentList = new ArrayList<Comment>();
		PreparedStatement getComments = statements.get("getComments");
		ResultSet rs;
		Comment comment;
		try {
			getComments.setInt(1, fieldId);
			rs = getComments.executeQuery();
			while (rs.next()) {
				comment = new Comment(
						rs.getInt("id"),
						rs.getString("text"),
						rs.getString("type"));
				comment.setExpandedCommentList(getExCommentList(statements.get("getExComments"), rs.getInt("id")));
				commentList.add(comment);
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return commentList;
	}


	private ArrayList<ExpandedComment> getExCommentList(PreparedStatement statement, int commentId) {
//		"SELECT * FROM ExpandedComment WHERE idComment = ?"
		ArrayList<ExpandedComment> expandedCommentList = new ArrayList<ExpandedComment>();
		ResultSet rs;
		ExpandedComment exComment;
		try {
			statement.setInt(1, commentId);
			rs = statement.executeQuery();
			while (rs.next()) {
				exComment = new ExpandedComment(rs.getInt("id"), rs.getString("text"));
				expandedCommentList.add(exComment);
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return expandedCommentList;
	}


//
//	public int writeIntoMark(int idlecturer, int idStudent, Criteria cr,
//			double mark, int if_only_comment, String studentName)
//			throws SQLException {
//		int primaryKey = 0;
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		try {
//			conn = connectToDB(DB_URL, USER, PASS);
//			String sql;
//			sql = "INSERT INTO Mark(idlecturers, idStudents, CriteriaName, Mark, MaxMark, if_only_comment) values(?,?,?,?,?,?)";
//			pstmt = conn.prepareStatement(sql,
//					PreparedStatement.RETURN_GENERATED_KEYS);
//			pstmt.setInt(1, idlecturer);
//			pstmt.setInt(2, idStudent);
//			pstmt.setString(3, cr.getName());
//			pstmt.setDouble(4, mark);
//			pstmt.setInt(5, cr.getMaximunMark());
//			pstmt.setInt(6, if_only_comment);
//			pstmt.executeUpdate();
//			System.out.println(sql);
//			rs = pstmt.getGeneratedKeys();
//			if (rs.next()) {
//				primaryKey = rs.getInt(1);
//			}
//			int i = cr.getSubsectionList().size();
//			for (int j = 0; j < i; j++) {
//				addSpecificComments(primaryKey, cr.getSubsectionList().get(j),
//						studentName);
//			}
//			System.out.println("add successfully ! Grader: " + idlecturer
//					+ " student: " + idStudent);
//		} catch (SQLException se) {
//			// JDBC faults
//			se.printStackTrace();
//		} finally {
//			disconnectDB(conn, pstmt, rs);
//		}
//		return primaryKey;
//	}

//	public void addSpecificComments(int primaryKey, SubSection ss,
//			String studentName) throws SQLException {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		try {
//			conn = connectToDB(DB_URL, USER, PASS);
//			String sql;
//			sql = "INSERT INTO CriteriaComment(idMark, subsection, shortText, comment) values(?,?,?,?)";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, primaryKey);
//			pstmt.setString(2, ss.getName());
//			pstmt.setString(3, ss.getShortTextList().get(0).getName());
//			pstmt.setString(4,
//					ss.getShortTextList().get(0).getLongtext().get(0));
//			pstmt.executeUpdate();
//			System.out.println(sql);
//		} catch (SQLException se) {
//			// JDBC faults
//			se.printStackTrace();
//		} finally {
//			disconnectDB(conn, pstmt, rs);
//		}
//	}


//	public boolean editStudentMark(int idStudent, double mark)
//			throws SQLException {
//		boolean result = false;
//		Connection conn = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//		String sql;
//		try {
//			conn = connectToDB(DB_URL, USER, PASS);
//			stmt = conn.createStatement();
//			sql = "UPDATE Students SET " + "mark = '" + mark + "' "
//					+ "WHERE idStudents= " + "'" + idStudent + "';  ";
//			stmt.execute(sql);
//			System.out.println(sql);
//			result = true;
//		} catch (SQLException se) {
//			// JDBC faults
//			se.printStackTrace();
//		} finally {
//			disconnectDB(conn, stmt, rs);
//		}
//		return result;
//	}


//	private ArrayList<SubSection> returnSpecificComment(int markId)
//			throws SQLException {
//		ArrayList<SubSection> subsectionList = new ArrayList<SubSection>();
//		Connection conn = null;
//		ResultSet rs = null;
//		Statement stmt = null;
//		try {
//			conn = connectToDB(DB_URL, USER, PASS);
//			stmt = conn.createStatement();
//			String sql;
//			sql = "SELECT * FROM CriteriaComment";
//			rs = stmt.executeQuery(sql);
//			System.out.println(sql);
//			while (rs.next()) {
//				if (rs.getInt("idMark") == markId) {
//					SubSection ss = new SubSection();
//					ss.setName(rs.getString("subSection"));
//					ShortText st = new ShortText();
//					st.setName(rs.getString("shortText"));
//					st.getLongtext().add(rs.getString("comment"));
//					ss.getShortTextList().add(st);
//					subsectionList.add(ss);
//				} else {
//					continue;
//				}
//			}
//		} catch (SQLException se) {
//			// JDBC faults
//			se.printStackTrace();
//		} finally {
//			disconnectDB(conn, stmt, rs);
//		}
//
//		return subsectionList;
//	}


//	public boolean deleteMark(int lecturerId, int studentId)
//			throws SQLException {
//		boolean result = false;
//		Connection conn = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//		String sql;
//		try {
//			conn = connectToDB(DB_URL, USER, PASS);
//			stmt = conn.createStatement();
//			sql = "DELETE FROM Mark " + "WHERE idStudents= " + "'" + studentId
//					+ "' AND idLecturers= " + "'" + lecturerId + "';  ";
//			stmt.execute(sql);
//			System.out.println(sql);
//			sql = null;
//			sql = "DELETE FROM Lecturers_comment_Students "
//					+ "WHERE idStudents= " + "'" + studentId
//					+ "' AND idLecturers= " + "'" + lecturerId + "'; ";
//			stmt.execute(sql);
//			System.out.println(sql);
//			result = true;
//		} catch (SQLException se) {
//			// JDBC faults
//			se.printStackTrace();
//		} finally {
//			disconnectDB(conn, stmt, rs);
//		}
//		return result;
//	}

	public boolean sentMail(int projectId, int studentId) {
		boolean updated = false;
		Connection connection;
		PreparedStatement statement;

		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement(
					"UPDATE StudentInProject SET ifEmailed = 1 " +
							"WHERE idProject = ? AND idStudent = ?");

			statement.setInt(1, projectId);
			statement.setInt(2,studentId);
			statement.executeUpdate();
			updated = true;
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return updated;
	}

	public double getMaximumMark(int criterionId, int projectId){
		double mark = -1;
		Connection connection;
		ResultSet rs;
		PreparedStatement statement;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement(
					"SELECT * FROM ProjectCriterion WHERE idProject= ? AND idCriterion = ?");
			statement.setInt(1, projectId);
			statement.setInt(2, criterionId);
			rs = statement.executeQuery();
			if (rs.next()){
				mark = rs.getDouble("maximumMark");
			}
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return mark;
	}

//	public List<Integer> queryProjects(String mail) throws SQLException {
//		List<Integer> numList = new ArrayList<Integer>();
//		int id = 0;
//		Connection conn = null;
//		ResultSet rs = null;
//		Statement stmt = null;
//		try {
//			conn = connectToDB(DB_URL, USER, PASS);
//			stmt = conn.createStatement();
//			String sql;
//			sql = "SELECT * FROM Lecturers";
//			rs = stmt.executeQuery(sql);
//			System.out.println(sql);
//			while (rs.next()) {
//				// id = rs.getInt("idLecturers");
//				// System.out.println("kkkkk"+id);
//				if (rs.getString("email").equals(mail)) {
//					id = rs.getInt("idLecturers");
//				} else {
//					continue;
//				}
//			}
//			sql = "SELECT * FROM Lecturers_has_Project WHERE idLecturers ="
//					+ id;
//			rs = stmt.executeQuery(sql);
//			System.out.println(sql);
//			while (rs.next()) {
//				int pjId = rs.getInt(2);
//				numList.add(pjId);
//			}
//		} catch (SQLException se) {
//			// JDBC faults
//			se.printStackTrace();
//		} finally {
//			disconnectDB(conn, stmt, rs);
//		}
//		return numList;
//	}

	public int createAudio() {
		Connection connection;
		PreparedStatement statement;
		ResultSet rs;
		int id = 0;
		try {
			connection = connectToDB(DB_URL, USER, PASS);
			statement = connection.prepareStatement(
					"INSERT INTO Audio() values ()", PreparedStatement.RETURN_GENERATED_KEYS);
			statement.executeUpdate();
			rs = statement.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
			connection.close();
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		}
		return id;
	}

}
