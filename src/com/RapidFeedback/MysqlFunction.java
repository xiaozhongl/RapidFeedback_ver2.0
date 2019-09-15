package com.RapidFeedback;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MysqlFunction
 * @Description All the functions which communicate with database.
 *
 * @author Dinghao Yong
 */
public class MysqlFunction {

	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=UTC&useSSL=false";

	static final String USER = "root";
	static final String PASS = "alfa1994";

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
			// Class.forname faults
			e.printStackTrace();
		}

		// System.out.println("Successfully Connected to DB...");
		return conn;
	}

	/**
	 * function: add lecturer information when a client registers
	 *
	 * @param mail       mail for register, works as the identifier
	 * @param password   user's password
	 * @param firstName  user's first name
	 * @param middleName user's middle name
	 * @param familyName user's family name
	 * @throws SQLException
	 */
	public void addToLecturers(String mail, String password, String firstName,
			String middleName, String familyName) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		String sql;
		sql = "INSERT INTO Lecturers( email, password, FirstName, MiddleName, FamilyName) "
				+ "values( '" + mail + "','" + password + "','" + firstName
				+ "','" + middleName + "','" + familyName + "' )";

		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			System.out.println(sql);
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close1(conn, stmt);
		}
	}

	/**
	 * function: check whether the e-mail has been registered
	 * 
	 * @param mail an e-mail address
	 * @return if the e-mail exists, return 0; if does not exist, return 1
	 * @throws SQLException
	 */
	public int checkLecturerExists(String mail) throws SQLException {
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		int tag = 1;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT email FROM Lecturers";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getString("email").equals(mail)) {
					tag = 0;
				}
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
			tag = 2;
		} finally {
			close2(conn, stmt, rs);
		}
		System.out.println("tag = " + tag);
		return tag;
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
			close2(conn, stmt, rs);
		}
		return num;
	}

	/**
	 * create a project in the server database
	 * 
	 * @param principalId the project founder's id
	 * @param projectName the project name
	 * @param subjectCode the subject code
	 * @param subjectName the subject name
	 * @param duration the presentation duration in second
	 * @param warning  the time left in second before presentation ends
	 * @param description the description of the project
	 * @return the generated primary key in the sql database, if fails return 0
	 * @throws SQLException
	 */
	public int createProject(int principalId, String projectName, String subjectCode,
							 String subjectName, int duration, int warning,
							 String description)
			throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
//		int principalId;
		int pjId = 0;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
//			principalId = getLecturerId(principalName);
			sql = "INSERT INTO Project(name, subjectName, subjectCode, durationSec, " +
					"warningSec, description, idPrincipalMarker) "
					+ "values( '" + projectName + "','" + subjectName + "','" + subjectCode + "',"
					+ duration + "," + warning + ",'" + description + "'," + principalId + ");";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				pjId = rs.getInt(1);
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return pjId;
	}

	/**
	 * update the project information in the server database
	 * 
	 * @param projectId   the project's Id
	 * @param projectName the updated project name
	 * @param subjectCode the updated subject code
	 * @param subjectName the updated subject name
	 * @param duration the presentation duration in second
	 * @param warning  the time left in second before presentation ends
	 * @param description the updated project description
	 * @return True = update successfully; False = fail to update
	 * @throws SQLException
	 */
	public boolean updateProjectInfo(int projectId, String projectName, String subjectCode,
									 String subjectName, int duration, int warning, String description)
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
			close1(conn, stmt);
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
	 * delete a project from the db
	 * 
	 * @param projectId  the project's Id
	 * @return True = delete successfully; False = faile to delete
	 * @throws SQLException
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
			close1(conn, stmt);
		}
		return deleted;
	}

	/**
	 * get the project id given principal's id and project name
	 *
	 * @param principalId the project founder's id
	 * @param projectName name of the project
	 * @return the id found, if not return 0
	 * @throws SQLException
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
			close2(conn, stmt, rs);
		}
		return id;
	}

	/**
	 * retrieve a project given project id
	 *
	 * @param projectId the project's id
	 * @return the project class
	 * @throws SQLException
	 */
	public Project getProjectInfo(int projectId) throws SQLException {
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
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return project;
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
	 * delete the existing criterias of a project
	 * 
	 * @param pjId the primary key in the Table project
	 * @return True = delete successfully; False = faile to delete
	 * @throws SQLException
	 */
	public boolean deleteCriterias(int pjId) throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();

			sql = "DELETE FROM Criteria WHERE idProject = " + pjId + ";";
			stmt.executeUpdate(sql);
			result = true;
			System.out.println(sql);
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return result;
	}

	/**
	 * add the criteria that wont contribute to the final mark
	 * 
	 * @param pjId the primary key in the Table project
	 * @param c    the Criteria object imported
	 * @return the primary key of the Criteria table generated by the system
	 * @throws SQLException
	 */
	public int addOnlyComment(int pjId, Criteria c) throws SQLException {
		int critId = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO Criteria(name, idProject, weighting, maxMark, markIncrement, if_only_comment) "
					+ "values( '" + c.getName() + "','" + pjId + "','"
					+ c.getWeighting() + "','" + c.getMaximunMark() + "','"
					+ c.getMarkIncrement() + "','" + 1 + "' )";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				critId = rs.getInt(1);
			}
			for (int i = 0; i < c.getSubsectionList().size(); i++) {
				addSubSection(critId, c.getSubsectionList().get(i));
			}

		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return critId;
	}

	/**
	 * add a Criteria to a project
	 * 
	 * @param pjId the primary key in the Table project
	 * @param c    the Criteria object imported
	 * @return the primary key of the Criteria table generated by the system
	 * @throws SQLException
	 */
	public int addCriteria(int pjId, Criteria c) throws SQLException {
		int critId = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO Criteria(name, idProject, weighting, maxMark, markIncrement, if_only_comment) "
					+ "values( '" + c.getName() + "','" + pjId + "','"
					+ c.getWeighting() + "','" + c.getMaximunMark() + "','"
					+ c.getMarkIncrement() + "','" + 0 + "' )";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);

			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				critId = rs.getInt(1);
			}
			for (int i = 0; i < c.getSubsectionList().size(); i++) {
				addSubSection(critId, c.getSubsectionList().get(i));
			}

		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return critId;
	}

	/**
	 * add a subsection to a criteria
	 * 
	 * @param critId a primary key in the Criteria Table
	 * @param ss     a Subsection object
	 * @return the primary key of the Subsection table generated by the system
	 * @throws SQLException
	 */
	public int addSubSection(int critId, SubSection ss) throws SQLException {
		int ssId = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO SubSection(name, idCriteria) " + "values( '"
					+ ss.getName() + "','" + critId + "' )";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				ssId = rs.getInt(1);
			}
			for (int i = 0; i < ss.getShortTextList().size(); i++) {
				addShortText(ssId, ss.getShortTextList().get(i));
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return ssId;
	}

	public int addShortText(int subsId, ShortText st) throws SQLException {
		int stId = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			sql = "INSERT INTO ShortText(name, grade, idSubSection) "
					+ "values(?,?,?)";
			pstmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, st.getName());
			pstmt.setInt(2, st.getGrade());
			pstmt.setInt(3, subsId);
			pstmt.executeUpdate();
			System.out.println(sql);
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				stId = rs.getInt(1);
			}
			for (int i = 0; i < st.getLongtext().size(); i++) {
				addLongText(stId, st.getLongtext().get(i));
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close3(conn, pstmt, rs);
		}
		return stId;
	}

	public void addLongText(int stId, String context) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			sql = "INSERT INTO `LongText`(context, idShortText) values(?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, context);
			pstmt.setInt(2, stId);
			pstmt.executeUpdate();
			System.out.println(sql);
			pstmt.close();
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close3(conn, pstmt, rs);
		}
	}

	public int ifStudentExists(int idProject, String studentNumber)
			throws SQLException {
		int result = 0;
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Students";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idProject") == idProject && rs
						.getString("studentNumber").equals(studentNumber)) {
					result = rs.getInt("idStudents");
				}
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return result;
	}


	/**
	 * add a student to the database
	 *
	 * @param critId a primary key in the Criteria Table
	 * @param ss     a Subsection object
	 * @return the primary key of the Subsection table generated by the system
	 * @throws SQLException
	 */
	public boolean addStudentInfo(int projectId, String studentNumber,
			String email, String firstName, String lastName, String middleName,
			int group) throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs;
		String sql;
		try {
			double mark = -999.00;
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO Students(studentNumber, emailAddress, idProject, firstName, "
					+ "surName, middleName, groupNumber, mark) values( '"
					+ studentNumber + "','" + mail + "','" + projectId + "','"
					+ firstName + "','" + surName + "','" + middleName + "','"
					+ group + "','" + mark + "' );";
			stmt.execute(sql);
			System.out.println(sql);
			result = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return result;
	}

	public boolean editStudentInfo(int projectId, String studentNumber,
			String mail, String firstName, String surName, String middleName,
			int group) throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			double mark = -999.00;
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "UPDATE Students SET " + "emailAddress = '" + mail + "',  "
					+ "firstName = '" + firstName + "', " + "surName = '"
					+ surName + "', " + "middleName = '" + middleName + "', "
					+ "groupNumber = '" + group + "', " + "mark = '" + mark
					+ "' " + "WHERE idProject= " + "'" + projectId
					+ "' AND studentNumber= " + "'" + studentNumber + "';  ";
			stmt.execute(sql);
			System.out.println(sql);
			result = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return result;
	}

	public boolean deleteStudent(int projectId, String studentNumber)
			throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "DELETE FROM Students " + "WHERE idProject= " + "'"
					+ projectId + "' AND studentNumber= " + "'" + studentNumber
					+ "';  ";
			stmt.execute(sql);
			System.out.println(sql);
			result = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return result;
	}
	/**
	 * assign a student to a project
	 *
	 * @param studentId  the student id
	 * @param projectId  the project id
	 * @return True = assigned successfully; False = fail to assign
	 * @throws SQLException
	 */
	public boolean assignStudentToProject(int studentId, int projectId)
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
			close1(conn, stmt);
		}
		return assigned;
	}


	/**
	 * edit the group number of students in a project
	 *
	 * @param studentId  the student id
	 * @param projectId  the project id
	 * @return True = edited successfully; False = fail to edit
	 * @throws SQLException
	 */
	public boolean editGroupNumber(int projectId, int studentId, int group)
			throws SQLException {
		boolean edited = false;
		Connection conn = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "UPDATE Students SET group= " + group + " " +
					"WHERE idProject= " + projectId + " " +
					"AND idStudent= " + studentId + ";";
			stmt.execute(sql);
			System.out.println(sql);
			edited = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close1(conn, stmt);
		}
		return edited;
	}

	/**
	 * assign a student to a project
	 *
	 * @param studentId  the student id
	 * @param projectId  the project id
	 * @return True = assigned successfully; False = fail to assign
	 * @throws SQLException
	 */
	public boolean updateFinalResult(int projectId, int studentId, double score, String remark)
			throws SQLException {
		boolean updated = false;
		Connection conn = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "UPDATE Students SET " +
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
			close1(conn, stmt);
		}
		return updated;
	}

	public boolean addOtherAssessor(int lecturerId, int projectId)
			throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO Lecturers_has_Project(idLecturers, idProject,If_Primary) "
					+ "values( '" + lecturerId + "','" + projectId + "','" + 0
					+ "' )";
			stmt.executeUpdate(sql);
			result = true;
			System.out.println(sql);
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close1(conn, stmt);
		}
		return result;
	}

	public boolean deleteAssessor(int lecturerId, int projectId)
			throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "DELETE FROM Lecturers_has_Project " + "WHERE idProject= "
					+ "'" + projectId + "' AND idLecturers= " + "'" + lecturerId
					+ "';  ";
			stmt.execute(sql);
			System.out.println(sql);
			result = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return result;
	}


	public int getLecturerId(String mail) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		int id = 0;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
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
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return id;
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
			close2(conn, stmt, rs);
		}
		return numList;
	}


	public ArrayList<Criteria> returnOnlyComment(int projectId)
			throws SQLException {
		ArrayList<Criteria> criteriaList = new ArrayList<Criteria>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Criteria WHERE if_only_comment = 1";
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
			close2(conn, stmt, rs);
		}
		return criteriaList;
	}

	public ArrayList<Criteria> returnCriteria(int projectId)
			throws SQLException {
		ArrayList<Criteria> criteriaList = new ArrayList<Criteria>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Criteria WHERE if_only_comment <> 1";
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
			close2(conn, stmt, rs);
		}
		return criteriaList;
	}

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
			close2(conn, stmt, rs);
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
			close2(conn, stmt, rs);
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
			close2(conn, stmt, rs);
		}
		return longtext;
	}

	public ArrayList<StudentInfo> returnStudents(int projectId)
			throws SQLException {
		ArrayList<StudentInfo> studentInfoList = new ArrayList<StudentInfo>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Students";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idProject") == projectId) {
					StudentInfo studentInfo = new StudentInfo(
							rs.getString("studentNumber"),
							rs.getString("firstName"),
							rs.getString("middleName"), rs.getString("surName"),
							rs.getString("emailAddress"));
					studentInfo.setTotalMark(rs.getDouble("mark"));
					studentInfo.setGroup(rs.getInt("groupNumber"));
					if (rs.getInt("if_send_mail") == 1) {
						studentInfo.setSendEmail(true);
					} else {
						studentInfo.setSendEmail(false);
					}
					studentInfoList.add(studentInfo);
				} else {
					continue;
				}
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return studentInfoList;
	}

	public ArrayList<String> returnAssessors(int projectId)
			throws SQLException {
		ArrayList<String> assessorList = new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Lecturers_has_Project ORDER BY If_Primary DESC";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idProject") == projectId) {
					assessorList.add(getLecturerMail(rs.getInt("idLecturers")));
				} else {
					continue;
				}
			}

		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return assessorList;
	}

	public String getLecturerMail(int id) throws SQLException {
		String mail = null;
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
				if (rs.getInt("idLecturers") == id) {
					mail = rs.getString("email");
				} else {
					continue;
				}
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return mail;
	}

	public String getLecturerName(int id) throws SQLException {
		String mail = null;
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
				if (rs.getInt("idLecturers") == id) {
					mail = rs.getString("FirstName") + " "
							+ rs.getString("MiddleName") + " "
							+ rs.getString("FamilyName");
				} else {
					continue;
				}
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return mail;
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
			close3(conn, pstmt, rs);
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
			close3(conn, pstmt, rs);
		}
	}

	// write lines into table comment
	public boolean writeIntoComment(int idlecturer, int idStudent,
			String comment, double totalmark) throws SQLException {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			sql = "INSERT INTO Lecturers_comment_Students(idlecturers, idStudents, comment, totalMark) values(?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idlecturer);
			pstmt.setInt(2, idStudent);
			pstmt.setString(3, comment);
			pstmt.setDouble(4, totalmark);
			pstmt.executeUpdate();
			System.out.println(sql);
			result = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close3(conn, pstmt, rs);
		}
		return result;
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
			close2(conn, stmt, rs);
		}
		return result;
	}

	public StudentInfo returnOneStudentInfo(int studentId) throws SQLException {
		StudentInfo studentInfo = null;
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Students";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idStudents") == studentId) {
					studentInfo = new StudentInfo(rs.getString("studentNumber"),
							rs.getString("firstName"),
							rs.getString("middleName"), rs.getString("surName"),
							rs.getString("emailAddress"));
					studentInfo.setTotalMark(rs.getDouble("mark"));
					studentInfo.setGroup(rs.getInt("groupNumber"));
					if (rs.getInt("if_send_mail") == 1) {
						studentInfo.setSendEmail(true);
					} else {
						studentInfo.setSendEmail(false);
					}
				} else {
					continue;
				}
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return studentInfo;
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
			close2(conn, stmt, rs);
		}
		return markObject;
	}

	public String returnOtherComment(int lecturerId, int studentId)
			throws SQLException {
		String comment = null;
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
					comment = rs.getString("comment");
				} else {
					continue;
				}
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return comment;
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
			close2(conn, stmt, rs);
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
			close2(conn, stmt, rs);
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
			close2(conn, stmt, rs);
		}
		return result;
	}

	public void close1(Connection conn, Statement stmt) throws SQLException {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} finally {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		}
	}

	public void close2(Connection conn, Statement stmt, ResultSet rs)
			throws SQLException {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} finally {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			}
		}
	}

	public void close3(Connection conn, PreparedStatement stmt, ResultSet rs)
			throws SQLException {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
			} finally {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			}
		}
	}

}
