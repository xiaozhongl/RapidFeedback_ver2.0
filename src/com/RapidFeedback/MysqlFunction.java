package com.RapidFeedback;

import java.sql.*;
import java.util.ArrayList;
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

	public enum CommentType {POSITIVE, NEGATIVE, NEUTRAL}

	/**
	 * function: connect to the database
	 *
	 * @param url      url address of the databse
	 * @param userName the db root username
	 * @param password the db root password
	 * @return the Connection to the db
	 */
	public Connection connectToDB(String url, String userName,
			String password) {
		Connection conn = null;
		try {
			// Register JDBC driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Connect the DB
			// System.out.println("Connecting the Database...");
			conn = DriverManager.getConnection(url, userName, password);

		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} catch (Exception e) {
			// Class.forName faults
			e.printStackTrace();
		}
		// System.out.println("Successfully Connected to DB...");
		return conn;
	}


	/**
	 * Login
	 * 
	 * @param mail     e-mail address the user used for login
	 * @param password password used for login
	 * @return successful login return lecturer's id, wrong e-mail address
	 *         reutrn -1, wrong password return 0
	 * @throws SQLException
	 */
	public int logIn(String mail, String password) throws SQLException {
		int num = -1;
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Lecturers";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getString("email").equals(mail)) {
					if (rs.getString("password").equals(password)) {
						num = rs.getInt("idLecturers");
					} else {
						num = 0;
					}
				}
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
			num = -2;
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return num;
	}

	/**
	 * create by: Xizhi Geng
	 * description: add a new marker to database
	 * create time: 2019/9/16 9:54 PM
	 *
	 * @Param: email
	 * @Param: password
	 * @Param: firstName
	 * @Param: middleName
	 * @Param: lastName
	 * @return the marker's id if add successfully; or return 0
	 */
	public int addMarker(String email, String password, String firstName,
							   String middleName, String lastName) throws SQLException {
		int id = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO Marker( email, password, firstName, middleName, lastName) "
					+ "values( '" + email + "','" + password + "','" + firstName
					+ "','" + middleName + "','" + lastName + "');";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return id;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: check if the email already esists
	 * create time: 2019/9/16 10:09 PM
	 *
	 * @Param: email
	 * @return true if the email already exists; false if not
	 */
	public boolean ifEmailExist(String email) throws SQLException {
		boolean emailExist = false;
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "SELECT * FROM Marker WHERE email='"+ email +"';";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			if (rs.next()) {
				emailExist = true;
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return emailExist;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: retireve a marker's id given email
	 * create time: 2019/9/16 10:21 PM
	 *
	 * @Param: email
	 * @return the marker's id if found; or return 0
	 */
	public int getMarkerId(String email) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		int id = 0;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "SELECT * FROM Marker WHERE email='"+ email +"';";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return id;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: get a marker's email given the marker's id
	 * create time: 2019/9/16 10:29 PM
	 *
	 * @Param: id
	 * @return the marker's email if found; or null if not found
	 */
	public String getMarkerEmail(int id) throws SQLException {
		String email = null;
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "SELECT * FROM Marker WHERE id= "+ id +";";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			if (rs.next()){
				email = rs.getString("email");
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return email;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: get a marker's full name given the marker's id
	 * create time: 2019/9/16 10:44 PM
	 *
	 * @Param: id
	 * @return the marker's full name if found; or null if not found
	 */
	public String getMarkerName(int id) throws SQLException {
		String name = null;
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "SELECT * FROM Marker WHERE id= "+ id +";";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			if (rs.next()){
				if (rs.getString("middleName") == null){
					name = rs.getString("firstName") + " "
							+ rs.getString("lastName");
				}
				else{
					name = rs.getString("firstName") + " "
							+ rs.getString("middleName") + " "
							+ rs.getString("lastName");
				}
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return name;
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
	public int createProject(int principalId, String projectName, String subjectCode,
							 String subjectName, int duration, int warning,
							 String description)
			throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		int id = 0;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO Project(name, subjectName, subjectCode, durationSec, " +
					"warningSec, description, idPrincipalMarker) "
					+ "values( '" + projectName + "','" + subjectName + "','" +
					subjectCode + "'," + duration + "," + warning + ",'" +
					description + "'," + principalId + ");";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
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
	public boolean updateProject(int projectId, String projectName, String subjectCode,
								 String subjectName, int duration,
								 int warning, String description)
			throws SQLException {
		boolean updated = false;
		Connection conn = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "UPDATE Project SET " +
					"name = '" + projectName + "', " +
					"subjectName = '" + subjectName + "', " +
					"subjectCode = '" + subjectCode + "', " +
					"durationSec = " + duration + ", " +
					"warningSec = " + warning  + ", " +
					"description = '" + description + "' " +
					"WHERE id = " + projectId + ";";
			stmt.executeUpdate(sql);
			System.out.println(sql);
			updated = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt);
		}
		return updated;
	}

//	/**
//	 * update the time information of a project in the db
//	 *
//	 * @param pjId        the primary key in the Table project
//	 * @param durationMin
//	 * @param durationSec
//	 * @param warningMin
//	 * @param warningSec
//	 * @return True = update successfully; False = fail to update
//	 * @throws SQLException
//	 */
//	public boolean updateTimeInformation(int pjId, int durationMin,
//			int durationSec, int warningMin, int warningSec)
//			throws SQLException {
//		Connection conn = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//		String sql;
//		boolean result = false;
//		try {
//			conn = connectToDB(DB_URL, USER, PASS);
//			stmt = conn.createStatement();
//			sql = "UPDATE Project SET durationMin = '" + durationMin + "',  "
//					+ "durationSec = '" + durationSec + "' "
//					+ "WHERE idProject = " + "'" + pjId + "' ";
//			stmt.executeUpdate(sql);
//			System.out.println(sql);
//
//			sql = "UPDATE Project SET warningMin = '" + warningMin + "',  "
//					+ "warningSec = '" + warningSec + "' "
//					+ "WHERE idProject = " + "'" + pjId + "' ";
//			stmt.executeUpdate(sql);
//			System.out.println(sql);
//			result = true;
//
//		} catch (SQLException se) {
//			// JDBC faults
//			se.printStackTrace();
//		} finally {
//			close2(conn, stmt, rs);
//		}
//		return result;
//	}

	/**
	 * create by: Xiaozhong Liu
	 * description: delete a project from database
	 * create time: 2019/9/15 8:30 PM
	 *
	 * @Param: projectId the primary key in Project table
	 * @return True if delete successfully; False if fail to delete
	 */
	public boolean deleteProject(int projectId) throws SQLException {
		boolean deleted = false;
		Connection conn = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "DELETE FROM Project WHERE id =" + projectId + ";";
			stmt.executeUpdate(sql);
			deleted = true;
			System.out.println(sql);
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt);
		}
		return deleted;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: retrieve project id by principal's id and project name
	 * create time: 2019/9/15 8:33 PM
	 *
	 * @Param: principalId
	 * @Param: projectName
	 * @return the id if found; if not return 0
	 */
	public int getProjectId(int principalId, String projectName)
			throws SQLException {
		int id = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "SELECT id FROM Project " +
					"WHERE idPrincipalMarker ="+ principalId +" " +
					"AND name = '"+ projectName + "';";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return id;
	}


	/**
	 * create by: Xiaozhong Liu
	 * description: retrieve a project by project id
	 * create time: 2019/9/15 8:37 PM
	 *
	 * @Param: projectId
	 * @return the project found, or null if not found
	 */
	public Project getProject(int projectId) throws SQLException {
		boolean found = false;
		Project project = new Project();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Project WHERE id = " + projectId + ";";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			if (rs.next()) {
				project.setPrincipal(rs.getInt("idPrincipalMarker"));
				project.setProjectName(rs.getString("name"));
				project.setSubjectCode(rs.getString("subjectCode"));
				project.setSubjectName(rs.getString("subjectName"));
				project.setDescription(rs.getString("description"));
				project.setDurationSec(rs.getInt("durationSec"));
				project.setWarningSec(rs.getInt("warningSec"));
				found = true;
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		if (found){
			return project;
		}
		return null;
	}

//	public ProjectInfo returnProjectDetails(int projectId) throws SQLException {
//		ProjectInfo pj = new ProjectInfo();
//		Connection conn = null;
//		ResultSet rs = null;
//		Statement stmt = null;
//		try {
//			conn = connectToDB(DB_URL, USER, PASS);
//			stmt = conn.createStatement();
//			String sql;
//			sql = "SELECT * FROM Project";
//			rs = stmt.executeQuery(sql);
//			System.out.println(sql);
//			while (rs.next()) {
//				if (rs.getInt("idProject") == projectId) {
//					pj.setUsername(rs.getString("primaryMail"));
//					pj.setProjectName(rs.getString("name"));
//					pj.setSubjectCode(rs.getString("subjectCode"));
//					pj.setSubjectName(rs.getString("subjectName"));
//					pj.setDescription(rs.getString("description"));
//					pj.setAssistant(returnAssessors(projectId));
//				} else {
//					continue;
//				}
//			}
//		} catch (SQLException se) {
//			// JDBC faults
//			se.printStackTrace();
//		} finally {
//			close2(conn, stmt, rs);
//		}
//		return pj;
//	}

	/**
	 * create by: Peng Kuang
	 * modified by: Xiaozhong Liu
	 * description: add student to database
	 * create time: 2019/9/15 8:54 PM
	 *
	 * @Param: studentNumber
	 * @Param: firstName
	 * @Param: lastName
	 * @Param: middleName
	 * @Param: email
	 * @return true if add successfully; false if not
	 */
	public boolean addStudent(int studentNumber, String firstName, String lastName,
							  String middleName, String email) throws SQLException {
		boolean added = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			sql = "INSERT INTO " +
					"Student(firstName, middleName, lastName, studentNumber, email) " +
					"VALUES (?,?,?,?,?);";
//			pstmt = conn.prepareStatement("INSERT INTO Student VALUES (?,?,?,?,?,?);");
			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, idStudent);
			pstmt.setString(1, firstName);
			pstmt.setString(2, middleName);
			pstmt.setString(3, lastName);
			pstmt.setInt(4, studentNumber);
			pstmt.setString(5, email);
			pstmt.executeUpdate();
//			System.out.println(countInserted + " records inserted. \n");
			added = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, pstmt);
		}
		return added;
	}

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
								 String middleName, String email) throws SQLException {
		boolean updated = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			sql = "UPDATE Student SET " +
					"firstName = ?, " +
					"middleName = ?, " +
					"lastName = ?, " +
					"studentNumber = ? " +
					"email = ?, " +
					"WHERE id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, firstName);
			pstmt.setString(2, middleName);
			pstmt.setString(3, lastName);
			pstmt.setInt(4, studentNumber);
			pstmt.setString(5, email);
			pstmt.setInt(6, studentId);
			pstmt.executeUpdate();
			System.out.println(sql);
			updated = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, pstmt);
		}
		return updated;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: retrieve a student by student id
	 * create time: 2019/9/16 4:03 PM
	 *
	 * @Param: studentId
	 * @return the student object
	 */
	public Student getStudent(int studentId) throws SQLException {
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		String sql;
		Student student = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "SELECT * FROM Student WHERE id = "+ studentId +";";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			if (rs.next()) {
				student = new Student(rs.getString("firstName"),
						rs.getString("middleName"),
						rs.getString("lastName"),
						rs.getString("email"),
						rs.getInt("studentNumber"));
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return student;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: retrieve student id by student number
	 * create time: 2019/9/17 9:14 AM
	 *
	 * @Param: studentNumber
	 * @return the student id if found; 0 if not
	 */
	public int getStudentId(int studentNumber) throws SQLException {
		int studentId = 0;
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "SELECT * FROM Student WHERE studentNumber = "+ studentNumber +";";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			if (rs.next()) {
				studentId = rs.getInt("id");
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return studentId;
	}


	/**
	 * create by: Xiaozhong Liu
	 * description: assign a student to a project
	 * create time: 2019/9/15 9:45 PM
	 *
	 * @Param: studentId
	 * @Param: projectId
	 * @return True if assigned successfully; False if fail to assign
	 */
	public boolean addStudentToProject(int studentId, int projectId)
			throws SQLException {
		boolean assigned = false;
		Connection conn = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO StudentInProject(idProject, idStudent)" +
					"values(" + projectId + "," + studentId + ");";
			stmt.execute(sql);
			System.out.println(sql);
			assigned = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt);
		}
		return assigned;
	}


	/**
	 * create by: Xiaozhong Liu
	 * description: edit the group number of students in a project
	 * create time: 2019/9/15 9:49 PM
	 *
	 * @Param: projectId
	 * @Param: studentId
	 * @Param: group
	 * @return True if update successfully; False if fail to update
	 */
	public boolean updateGroupNumber(int projectId, int studentId, int group)
			throws SQLException {
		boolean updated = false;
		Connection conn = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "UPDATE StudentInProject SET group= " + group + " " +
					"WHERE idProject= " + projectId + " " +
					"AND idStudent= " + studentId + ";";
			stmt.execute(sql);
			System.out.println(sql);
			updated = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt);
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
	public boolean updateResult(int projectId, int studentId, double score, String remark)
			throws SQLException {
		boolean updated = false;
		Connection conn = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "UPDATE StudentInProject SET " +
					"finalScore= " + score + " " +
					"finalRemark='" + remark + "' " +
					"WHERE idProject= " + projectId + " " +
					"AND idStudent= " + studentId + ";";
			stmt.execute(sql);
			System.out.println(sql);
			updated = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt);
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
	public boolean deleteStudentFromProject(int projectId, int studentId)
			throws SQLException {
		boolean deleted = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "DELETE FROM StudentInProject " +
					"WHERE idProject= " + projectId + " " +
					"AND idStudent= '" + studentId + "';";
			stmt.execute(sql);
			System.out.println(sql);
			deleted = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return deleted;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: get a list of student ids in a project
	 * create time: 2019/9/16 5:36 PM
	 *
	 * @Param: projectId
	 * @return a list of student ids
	 */
	public ArrayList<Integer> getProjectStudents(int projectId)
			throws SQLException {
		ArrayList<Integer> studentIdList = new ArrayList<Integer>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT idStudent FROM StudentInProject WHERE idProject = "+ projectId +";";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				studentIdList.add(rs.getInt("idStudent"));
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return studentIdList;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: check if the given student is in the given project
	 * create time: 2019/9/17 9:26 AM
	 *
	 * @Param: idProject
	 * @Param: idStudent
	 * @return true if the student is in the project; false if not
	 */
		public boolean ifStudentInProject(int idProject, int idStudent)
			throws SQLException {
		boolean studentInProject = false;
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "SELECT * FROM StudentInProject " +
					"WHERE idProject = "+ idProject +" " +
					"AND idStudent = "+ idStudent +";";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			if (rs.next()) {
				studentInProject = true;
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return studentInProject;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: create new a new remark
	 * create time: 2019/9/17 10:07 AM
	 *
	 * @Param: idMarker
	 * @Param: idStudent
	 * @Param: idProject
	 * @Param: text
	 * @return true if add successfully; false if not
	 */
	public boolean addRemark(int idMarker, int idStudent, int idProject, String text)
			throws SQLException {
		boolean added = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			sql = "INSERT INTO Remark(idProject, idStudent, idMarker, text) values(?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idProject);
			pstmt.setInt(2, idStudent);
			pstmt.setInt(3, idMarker);
			pstmt.setString(4, text);
			pstmt.executeUpdate();
			System.out.println(sql);
			added = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, pstmt);
		}
		return added;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: retrieve text remark
	 * create time: 2019/9/17 10:50 AM
	 *
	 * @Param: markerId
	 * @Param: studentId
	 * @Param: projectId
	 * @return	text remark if found; null if not found
	 */
	public String getTextRemark(int markerId, int studentId, int projectId)
			throws SQLException {
		String textRemark = null;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			sql = "SELECT * FROM Remark WHERE idProject = ? AND idStudent = ? AND idMarker = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, projectId);
			pstmt.setInt(2, studentId);
			pstmt.setInt(3, markerId);
			pstmt.executeUpdate();
			System.out.println(sql);
			if (rs.next()) {
				textRemark = rs.getString("text");
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, pstmt, rs);
		}
		return textRemark;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: update the text remark
	 * create time: 2019/9/17 10:57 AM
	 *
	 * @Param: markerId
	 * @Param: studentId
	 * @Param: projectId
	 * @Param: text
	 * @return true if update successfully; false if not
	 */
	public boolean updateTextRemark(int markerId, int studentId, int projectId, String text)
			throws SQLException {
		boolean updated = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			sql = "UPDATE Remark SET text = ? WHERE idProject = ? AND idStudent = ? AND idMarker = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, text);
			pstmt.setInt(2, projectId);
			pstmt.setInt(3, studentId);
			pstmt.setInt(4, markerId);
			pstmt.executeUpdate();
			System.out.println(sql);
			updated = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, pstmt);
		}
		return updated;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: assign a mark to a project
	 * create time: 2019/9/17 11:24 AM
	 *
	 * @Param: markerId
	 * @Param: projectId
	 * @return true if assign successfully; false if not
	 */
	public boolean addMarkerToProject(int markerId, int projectId)
			throws SQLException {
		boolean added = false;
		Connection conn = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO MarkerInProject(idMarker, idProject) "
					+ "values(" + markerId + "," + projectId + ");";
			stmt.executeUpdate(sql);
			added = true;
			System.out.println(sql);
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt);
		}
		return added;
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
	public boolean deleteMarkerFromProject(int markerId, int projectId)
			throws SQLException {
		boolean deleted = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "DELETE FROM MarkerInProject WHERE idProject="+ projectId +
					" AND idMarker=" + markerId + ";";
			stmt.execute(sql);
			System.out.println(sql);
			deleted = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return deleted;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: get a list of markers' id in a project
	 * create time: 2019/9/17 11:47 AM
	 *
	 * @Param: projectId
	 * @return a list of marker ids
	 */
	public ArrayList<Integer>  getProjectMarkers(int projectId)
			throws SQLException {
		ArrayList<Integer> markerList = new ArrayList<Integer>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "SELECT * FROM MarkerInProject WHERE idProject = "+ projectId +";";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);

			while (rs.next()) {
				markerList.add(rs.getInt("idMarker"));
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return markerList;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: add a public criterion to database
	 * create time: 2019/9/17 12:28 PM
	 *
	 * @Param: name
	 * @return criterion id if successfully; if not 0
	 */
	public int addCriterion(String name) throws SQLException {
		int id = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO Criterion(name) values('" + name + "');";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
//			for (int i = 0; i < c.getSubsectionList().size(); i++) {
//				addSubSection(critId, c.getSubsectionList().get(i));
//			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return id;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: add a private criterion
	 * create time: 2019/9/20 7:06 AM
	 *
	 * @Param: name
	 * @Param: projectId
	 * @return criterion id if add successfully; 0 if not
	 */
	public int addCriterion(String name, int authorId) throws SQLException {
		int id = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO Criterion(name, idProject) " +
					"values('" + name + "', " + authorId + ");";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
//			for (int i = 0; i < c.getSubsectionList().size(); i++) {
//				addSubSection(critId, c.getSubsectionList().get(i));
//			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return id;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: delete a private criterion
	 * create time: 2019/9/20 8:01 AM
	 *
	 * @Param: criterionId
	 * @Param: authorId
	 * @return true if delete successfully; false if not
	 */
	public boolean deleteCriterion(int criterionId, int authorId) throws SQLException {
		boolean modified = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "DELETE FROM Criterion " +
					"WHERE id = "+ criterionId +" " +
					"AND author="+ authorId +";";
			stmt.executeUpdate(sql);
			modified = true;
			System.out.println(sql);
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt);
		}
		return modified;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: add a criterion to a project
	 * create time: 2019/9/17 12:41 PM
	 *
	 * @Param: idProject
	 * @Param: idCriterion
	 * @Param: maxMark
	 * @Param: weight
	 * @Param: markIncrement
	 * @return true if add successfully; false if not
	 */
	public boolean addProjectCriterion(int idProject, int idCriterion, int maxMark,
									   int weight, String markIncrement) throws SQLException {
		boolean added = false;
		Connection conn = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO ProjectCriterion(idProject, idCriterion, " +
					"maxMark, weight, markIncrement)" +
					" values("+ idProject +","+ idCriterion +","+ maxMark +","
					+ weight +","+ markIncrement +");";
			stmt.executeUpdate(sql);
			System.out.println(sql);
			added = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt);
		}
		return added;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: delete a criterion from a project
	 * create time: 2019/9/17 2:22 PM
	 *
	 * @Param: projectId
	 * @Param: idCriterion
	 * @return true if delete successfully; false if not
	 */
	public boolean deleteProjectCriterion(int projectId, int idCriterion)
			throws SQLException {
		boolean deleted = false;
		Connection conn = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();

			sql = "DELETE FROM ProjectCriterion " +
					"WHERE idProject = "+ projectId +" " +
					"AND idCriterion="+ idCriterion +";";
			stmt.executeUpdate(sql);
			deleted = true;
			System.out.println(sql);
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt);
		}
		return deleted;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: update a criterion in a project
	 * create time: 2019/9/19 7:25 PM
	 *
	 * @Param: idProject
	 * @Param: idCriterion
	 * @Param: maxMark
	 * @Param: weight
	 * @Param: markIncrement
	 * @return if successfully; if not
	 */
	public boolean updateProjectCriterion(int idProject, int idCriterion, int maxMark,
									   int weight, String markIncrement) throws SQLException {
		boolean updated = false;
		Connection conn = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "UPDATE ProjectCriterion SET " +
					"maxMark = " + maxMark + ", " +
					"weight = " + weight + ", " +
					"markIncrement = " + markIncrement  + ", " +
					"WHERE idProject = " + idProject + " " +
					"AND idCriterion = " + idCriterion + ";";
			stmt.executeUpdate(sql);
			System.out.println(sql);
			updated = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt);
		}
		return updated;
	}

	public ArrayList<Integer> getProjectCriteria(int projectId)
			throws SQLException {
		ArrayList<Integer> criteriaList = new ArrayList<Integer>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();

			sql = "SELECT * FROM ProjectCriterion WHERE idProject = "+ projectId +";";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idProject") == projectId) {
					Criteria cr = new Criteria();
					cr.setName(rs.getString("name"));
					cr.setWeighting(rs.getInt("weighting"));
					cr.setMaximunMark(rs.getInt("maxMark"));
					cr.setMarkIncrement(rs.getString("markIncrement"));
					cr.setSubsectionList(
							returnSubsection(rs.getInt("idCriteria")));
					criteriaList.add(cr);
				} else {
					continue;
				}
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return criteriaList;
	}

//	/**
//	 * add the criteria that wont contribute to the final mark
//	 *
//	 * @param pjId the primary key in the Table project
//	 * @param c    the Criteria object imported
//	 * @return the primary key of the Criteria table generated by the system
//	 * @throws SQLException
//	 */
//	public int addOnlyComment(int pjId, Criteria c) throws SQLException {
//		int critId = 0;
//		Connection conn = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//		String sql;
//		try {
//			conn = connectToDB(DB_URL, USER, PASS);
//			stmt = conn.createStatement();
//			sql = "INSERT INTO Criteria(name, idProject, weighting, maxMark, markIncrement, if_only_comment) "
//					+ "values( '" + c.getName() + "','" + pjId + "','"
//					+ c.getWeighting() + "','" + c.getMaximunMark() + "','"
//					+ c.getMarkIncrement() + "','" + 1 + "' )";
//			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
//			System.out.println(sql);
//			rs = stmt.getGeneratedKeys();
//			if (rs.next()) {
//				critId = rs.getInt(1);
//			}
//			for (int i = 0; i < c.getSubsectionList().size(); i++) {
//				addSubSection(critId, c.getSubsectionList().get(i));
//			}
//
//		} catch (SQLException se) {
//			// JDBC faults
//			se.printStackTrace();
//		} finally {
//			disconnectDB(conn, stmt, rs);
//		}
//		return critId;
//	}


	/**
	 * create by: Xiaozhong Liu
	 * description: add a new field to database
	 * create time: 2019/9/17 2:34 PM
	 *
	 * @Param: criterionId
	 * @Param: name
	 * @return field id if add successfully; 0 if not
	 */
	public int addField(int criterionId, String name) throws SQLException {
		int fieldId = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO Field(name, idCriterion) values('"+ name +"',"+ criterionId +");";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				fieldId = rs.getInt(1);
			}
//			for (int i = 0; i < ss.getShortTextList().size(); i++) {
//				addShortText(ssId, ss.getShortTextList().get(i));
//			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return fieldId;
	}


//	/**
//	 * create by: Xiaozhong Liu
//	 * description: delete a field from project
//	 * create time: 2019/9/19 5:47 PM
//	 *
//	 * @Param: fieldId
//	 * @return true if delete successfully; false if not
//	 */
//	public boolean deleteField(int fieldId)
//			throws SQLException {
//		boolean deleted = false;
//		Connection conn = null;
//		Statement stmt = null;
//		String sql;
//		try {
//			conn = connectToDB(DB_URL, USER, PASS);
//			stmt = conn.createStatement();
//
//			sql = "DELETE FROM ProjectField " +
//					"WHERE id = "+ fieldId +";";
//			stmt.executeUpdate(sql);
//			deleted = true;
//			System.out.println(sql);
//		} catch (SQLException se) {
//			// JDBC faults
//			se.printStackTrace();
//		} finally {
//			disconnectDB(conn, stmt);
//		}
//		return deleted;
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
	 * description: add new a comment to database
	 * create time: 2019/9/19 4:11 PM
	 *
	 * @Param: fieldId
	 * @Param: text
	 * @Param: type
	 * @return the comment id if add successfully; 0 if not
	 */
	public int addComment(int fieldId, String text, CommentType type) throws SQLException {
		int id = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			sql = "INSERT INTO Comment(text, idField, type) "
					+ "values(?,?,?)";
			pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, text);
			pstmt.setInt(2, fieldId);
			pstmt.setString(3, type.name());
			pstmt.executeUpdate();
			System.out.println(sql);
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}
//			for (int i = 0; i < st.getLongtext().size(); i++) {
//				addLongText(stId, st.getLongtext().get(i));
//			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, pstmt, rs);
		}
		return id;
	}


	/**
	 * create by: Xiaozhong Liu
	 * description: add new a expanded comment to database
	 * create time: 2019/9/19 4:20 PM
	 *
	 * @Param: idComment
	 * @Param: text
	 * @return id of the expanded comment if add successfully; 0 if not
	 */
	public int addExpandedComment(int idComment, String text) throws SQLException {
		int id = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			sql = "INSERT INTO ExpandedComment(idComment, text) values(?,?)";
			pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, idComment);
			pstmt.setString(2, text);
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			System.out.println(sql);
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, pstmt, rs);
		}
		return id;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: add an assessment to the database
	 * create time: 2019/9/20 10:19 AM
	 *
	 * @Param: projectId
	 * @Param: criterionId
	 * @Param: studentId
	 * @Param: markerId
	 * @return true if add successfully; false if not
	 */
	public boolean addAssessment(int projectId, int criterionId,
								 int studentId, int markerId) throws SQLException {
		boolean modified = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			sql = "INSERT INTO " +
					"Assessment(idProject, idCriterion, idStudent, idMarker) " +
					"VALUES (?,?,?,?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, projectId);
			pstmt.setInt(2, criterionId);
			pstmt.setInt(3, studentId);
			pstmt.setInt(4, markerId);
			pstmt.executeUpdate();
			modified = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, pstmt);
		}
		return modified;
	}

	/**
	 * create by: Xiaozhong Liu
	 * description: update the score in an assessment
	 * create time: 2019/9/20 11:02 AM
	 *
	 * @Param: idProject
	 * @Param: idCriterion
	 * @Param: idStudent
	 * @Param: idMarker
	 * @Param: score
	 * @return true if updated successfully; false if not
	 */
	public boolean updateAssessment(int idProject, int idCriterion, int idStudent,
										  int idMarker, double score) throws SQLException {
		boolean modified = false;
		Connection conn = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "UPDATE Assessment SET score = " + score + " " +
					"WHERE idProject = " + idProject + " " +
					"AND idCriterion = " + idCriterion + " " +
					"AND idStudent = " + idStudent + " " +
					"AND idMarker = " + idMarker + ";";
			stmt.executeUpdate(sql);
			System.out.println(sql);
			modified = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt);
		}
		return modified;
	}

	public double getTotalScore(int projectId, int studentId) throws SQLException {
		double score = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "SELECT * FROM Assessment " +
					"WHERE email='"+ email +"';";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			if (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return id;
	}

	public boolean selectComment(int projectId, int criterionId,
								 int studentId, int markerId) throws SQLException {
		boolean modified = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			sql = "INSERT INTO " +
					"Assessment(idProject, idCriterion, idStudent, idMarker) " +
					"VALUES (?,?,?,?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, projectId);
			pstmt.setInt(2, criterionId);
			pstmt.setInt(3, studentId);
			pstmt.setInt(4, markerId);
			pstmt.executeUpdate();
			modified = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, pstmt);
		}
		return modified;
	}

//	public ArrayList<Criteria> returnOnlyComment(int projectId)
//			throws SQLException {
//		ArrayList<Criteria> criteriaList = new ArrayList<Criteria>();
//		Connection conn = null;
//		ResultSet rs = null;
//		Statement stmt = null;
//		try {
//			conn = connectToDB(DB_URL, USER, PASS);
//			stmt = conn.createStatement();
//			String sql;
//			sql = "SELECT * FROM Criteria WHERE if_only_comment = 1";
//			rs = stmt.executeQuery(sql);
//			System.out.println(sql);
//			while (rs.next()) {
//				if (rs.getInt("idProject") == projectId) {
//					Criteria cr = new Criteria();
//					cr.setName(rs.getString("name"));
//					cr.setWeighting(rs.getInt("weighting"));
//					cr.setMaximunMark(rs.getInt("maxMark"));
//					cr.setMarkIncrement(rs.getString("markIncrement"));
//					cr.setSubsectionList(
//							returnSubsection(rs.getInt("idCriteria")));
//					criteriaList.add(cr);
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
//		return criteriaList;
//	}


	private ArrayList<SubSection> returnSubsection(int idCriteria)
			throws SQLException {
		ArrayList<SubSection> subsectionList = new ArrayList<SubSection>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM SubSection";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idCriteria") == idCriteria) {
					SubSection ss = new SubSection();
					ss.setName(rs.getString("name"));
					ss.setShortTextList(
							returnShortText(rs.getInt("idSubSection")));
					subsectionList.add(ss);
				} else {
					continue;
				}
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}

		return subsectionList;
	}

	private ArrayList<ShortText> returnShortText(int idSubSection)
			throws SQLException {
		ArrayList<ShortText> shortTextList = new ArrayList<ShortText>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM ShortText";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idSubSection") == idSubSection) {
					ShortText st = new ShortText();
					st.setGrade(rs.getInt("grade"));
					st.setName(rs.getString("name"));
					st.setLongtext(returnLongText(rs.getInt("idShortText")));
					shortTextList.add(st);
				} else {
					continue;
				}
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return shortTextList;
	}

	private ArrayList<String> returnLongText(int idShortText)
			throws SQLException {
		ArrayList<String> longtext = new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM `LongText`";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idShortText") == idShortText) {
					longtext.add(rs.getString("context"));
				} else {
					continue;
				}
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return longtext;
	}


	public int writeIntoMark(int idlecturer, int idStudent, Criteria cr,
			double mark, int if_only_comment, String studentName)
			throws SQLException {
		int primaryKey = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			String sql;
			sql = "INSERT INTO Mark(idlecturers, idStudents, CriteriaName, Mark, MaxMark, if_only_comment) values(?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, idlecturer);
			pstmt.setInt(2, idStudent);
			pstmt.setString(3, cr.getName());
			pstmt.setDouble(4, mark);
			pstmt.setInt(5, cr.getMaximunMark());
			pstmt.setInt(6, if_only_comment);
			pstmt.executeUpdate();
			System.out.println(sql);
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				primaryKey = rs.getInt(1);
			}
			int i = cr.getSubsectionList().size();
			for (int j = 0; j < i; j++) {
				addSpecificComments(primaryKey, cr.getSubsectionList().get(j),
						studentName);
			}
			System.out.println("add successfully ! Grader: " + idlecturer
					+ " student: " + idStudent);
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, pstmt, rs);
		}
		return primaryKey;
	}

	public void addSpecificComments(int primaryKey, SubSection ss,
			String studentName) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			String sql;
			sql = "INSERT INTO CriteriaComment(idMark, subsection, shortText, comment) values(?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, primaryKey);
			pstmt.setString(2, ss.getName());
			pstmt.setString(3, ss.getShortTextList().get(0).getName());
			pstmt.setString(4,
					ss.getShortTextList().get(0).getLongtext().get(0));
			pstmt.executeUpdate();
			System.out.println(sql);
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, pstmt, rs);
		}
	}



	public boolean editStudentMark(int idStudent, double mark)
			throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "UPDATE Students SET " + "mark = '" + mark + "' "
					+ "WHERE idStudents= " + "'" + idStudent + "';  ";
			stmt.execute(sql);
			System.out.println(sql);
			result = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return result;
	}



	public Mark returnMark(int projectId, int lecturerId, int studentId)
			throws SQLException {
		Mark markObject = new Mark();
		String str = null;
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Lecturers_comment_Students";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idLecturers") == lecturerId
						&& rs.getInt("idStudents") == studentId) {
					str = rs.getString("comment");
					markObject.setComment(str);
					markObject.setTotalMark(rs.getDouble("totalMark"));

				} else {
					continue;
				}
			}
			// set the lecturer name and e-mail of whom gave this mark
			markObject.setLecturerName(getLecturerName(lecturerId));
			markObject.setLecturerEmail(getLecturerMail(lecturerId));

			sql = "SELECT * FROM Mark";
			rs = null;
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idLecturers") == lecturerId
						&& rs.getInt("idStudents") == studentId
						&& rs.getInt("if_only_comment") == 0) {
					str = rs.getString("CriteriaName");
					double mk = rs.getDouble("Mark");
					int maxmk = rs.getInt("MaxMark");
					int markId = rs.getInt("idMark");
					Criteria cr = new Criteria();
					cr.setName(str);
					cr.setMaximunMark(maxmk);
					ArrayList<SubSection> ssList = returnSpecificComment(
							markId);
					cr.setSubsectionList(ssList);
					markObject.getCriteriaList().add(cr);
					markObject.getMarkList().add(mk);
					System.out.println("get the mark of "
							+ markObject.getCriteriaList().size()
							+ " marking criteria!");
				} else if (rs.getInt("idLecturers") == lecturerId
						&& rs.getInt("idStudents") == studentId
						&& rs.getInt("if_only_comment") == 1) {
					str = rs.getString("CriteriaName");
					int markId = rs.getInt("idMark");
					Criteria cr = new Criteria();
					cr.setName(str);
					ArrayList<SubSection> ssList = returnSpecificComment(
							markId);
					cr.setSubsectionList(ssList);
					markObject.getCommentList().add(cr);
					System.out.println("get the mark of "
							+ markObject.getCommentList().size()
							+ " marking criteria!");
				} else {
					continue;
				}
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return markObject;
	}


	private ArrayList<SubSection> returnSpecificComment(int markId)
			throws SQLException {
		ArrayList<SubSection> subsectionList = new ArrayList<SubSection>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM CriteriaComment";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idMark") == markId) {
					SubSection ss = new SubSection();
					ss.setName(rs.getString("subSection"));
					ShortText st = new ShortText();
					st.setName(rs.getString("shortText"));
					st.getLongtext().add(rs.getString("comment"));
					ss.getShortTextList().add(st);
					subsectionList.add(ss);
				} else {
					continue;
				}
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}

		return subsectionList;
	}

	public boolean deleteMark(int lecturerId, int studentId)
			throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "DELETE FROM Mark " + "WHERE idStudents= " + "'" + studentId
					+ "' AND idLecturers= " + "'" + lecturerId + "';  ";
			stmt.execute(sql);
			System.out.println(sql);
			sql = null;
			sql = "DELETE FROM Lecturers_comment_Students "
					+ "WHERE idStudents= " + "'" + studentId
					+ "' AND idLecturers= " + "'" + lecturerId + "'; ";
			stmt.execute(sql);
			System.out.println(sql);
			result = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return result;
	}

	public boolean editsentMail(int projectId, String studentNumber)
			throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "UPDATE Students SET " + "if_send_mail = '" + 1 + "' "
					+ "WHERE idProject= " + "'" + projectId
					+ "' AND studentNumber= " + "'" + studentNumber + "';  ";
			stmt.execute(sql);
			System.out.println(sql);
			result = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return result;
	}

	public List<Integer> queryProjects(String mail) throws SQLException {
		List<Integer> numList = new ArrayList<Integer>();
		int id = 0;
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Lecturers";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				// id = rs.getInt("idLecturers");
				// System.out.println("kkkkk"+id);
				if (rs.getString("email").equals(mail)) {
					id = rs.getInt("idLecturers");
				} else {
					continue;
				}
			}
			sql = "SELECT * FROM Lecturers_has_Project WHERE idLecturers ="
					+ id;
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				int pjId = rs.getInt(2);
				numList.add(pjId);
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			disconnectDB(conn, stmt, rs);
		}
		return numList;
	}

	public void disconnectDB(Connection conn, Statement stmt) throws SQLException {
		try {
			if (stmt != null) {
				stmt.close();
//				stmt = null;
			}
		} finally {
			if (conn != null) {
				conn.close();
//				conn = null;
			}
		}
	}

	public void disconnectDB(Connection conn, Statement stmt, ResultSet rs)
			throws SQLException {
		try {
			if (rs != null) {
				rs.close();
//				rs = null;
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
//					stmt = null;
				}
			} finally {
				if (conn != null) {
					conn.close();
//					conn = null;
				}
			}
		}
	}

//	public void disconnectDB(Connection conn, PreparedStatement pstmt)
//			throws SQLException {
//		try {
//			if (pstmt != null) {
//				pstmt.close();
////				pstmt = null;
//			}
//		} finally {
//			if (conn != null) {
//				conn.close();
////				conn = null;
//			}
//		}
//	}
//
//	public void disconnectDB(Connection conn, PreparedStatement pstmt, ResultSet rs)
//			throws SQLException {
//		try{
//			if (rs != null) {
//				rs.close();
//			}
//		} finally {
//			try {
//				if (pstmt != null) {
//					pstmt.close();
////					stmt = null;
//				}
//			} finally {
//				if (conn != null) {
//					conn.close();
////					conn = null;
//				}
//			}
//		}
//	}

}
