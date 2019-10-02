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
	static final String DB_URL = "jdbc:mysql://localhost:3306/mydb?serverTimezone=UTC&useSSL=false";

	static final String USER = "root";
	static final String PASS = "dhsx950321.";

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
	 * @param username    user's e-mail address
	 * @param projectName the project name
	 * @param subjectCode the subject code
	 * @param subjectName the subject name
	 * @param description the description of the project
	 * @return the generated primary key in the sql databse
	 * @throws SQLException
	 */
	public int createProject(String username, String projectName,
			String subjectCode, String subjectName, String description)
			throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		int lecturerId = 0;
		int pjId = 0;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			lecturerId = getLecturerId(username);
			sql = "INSERT INTO Project(primaryMail, name, subjectCode, subjectName) "
					+ "values( '" + username + "','" + projectName + "','"
					+ subjectCode + "','" + subjectName + "' )";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				pjId = rs.getInt(1);
				sql = "INSERT INTO Lecturers_has_Project(idLecturers, idProject,If_Primary) "
						+ "values( '" + lecturerId + "','" + pjId + "','" + 1
						+ "' )";
				stmt.executeUpdate(sql);
				System.out.println(sql);
			}
			if (description != null) {
				sql = "UPDATE Project SET Description = '" + description + "' "
						+ "WHERE idProject = " + "'" + pjId + "' ";
				stmt.executeUpdate(sql);
				System.out.println(sql);
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
	 * @param username    the user's e-mail
	 * @param projectName the updated project name
	 * @param subjectCode the updated subject code
	 * @param subjectName the updated subject name
	 * @param description the updated project description
	 * @return True = update successfully; False = fail to update
	 * @throws SQLException
	 */
	public boolean updateProjectInfo(String username, String projectName,
			String subjectCode, String subjectName, String description)
			throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		int pjId = 0;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			pjId = getProjectId(username, projectName);
			sql = "UPDATE Project SET primaryMail = '" + username
					+ "', name = '" + projectName + "', subjectCode = '"
					+ username + "', subjectName = '" + subjectName + "' "
					+ "WHERE idProject = " + "'" + pjId + "' ";
			stmt.executeUpdate(sql);
			System.out.println(sql);
			if (description != null) {
				sql = "UPDATE Project SET Description = '" + description + "' "
						+ "WHERE idProject = " + "'" + pjId + "' ";
				stmt.executeUpdate(sql);
				System.out.println(sql);
			}
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
	 * update the time information of a project in the db
	 * 
	 * @param pjId        the primary key in the Table project
	 * @param durationMin
	 * @param durationSec
	 * @param warningMin
	 * @param warningSec
	 * @return True = update successfully; False = fail to update
	 * @throws SQLException
	 */
	public boolean updateTimeInformation(int pjId, int durationMin,
			int durationSec, int warningMin, int warningSec)
			throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		boolean result = false;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "UPDATE Project SET durationMin = '" + durationMin + "',  "
					+ "durationSec = '" + durationSec + "' "
					+ "WHERE idProject = " + "'" + pjId + "' ";
			stmt.executeUpdate(sql);
			System.out.println(sql);

			sql = "UPDATE Project SET warningMin = '" + warningMin + "',  "
					+ "warningSec = '" + warningSec + "' "
					+ "WHERE idProject = " + "'" + pjId + "' ";
			stmt.executeUpdate(sql);
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
	 * delete a project from the db
	 * 
	 * @param pjId the primary key in the Table projec
	 * @return True = delete successfully; False = faile to delete
	 * @throws SQLException
	 */
	public boolean deleteProject(int pjId) throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "DELETE FROM Project WHERE idProject =" + pjId + ";";
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

			sql = "DELETE FROM ProjectSetCriteria WHERE idProject = " + pjId + ";";
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
	
	
	// Meng Ru 
	// 这个是加的不需要打分只有comment的criteria 我没找到新数据库里是哪部分存这个东西
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
			for (int i = 0; i < c.getFieldList().size(); i++) {
				setFieldList(critId, c.getFieldList().get(i));
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
			sql = "INSERT INTO ProjectSetCriteria(name, idProject, weighting, maxMark, markIncrement) "
					+ "values( '" + c.getName() + "','" + pjId + "','"
					+ c.getWeighting() + "','" + c.getMaximunMark() + "','"
					+ c.getMarkIncrement() + "','" + "' )";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);
			
			
			//Meng Ru
			//改的是副本的表的话就要先加到criteria的表里 再generate key 然后再加到projectsetcriteria的表里
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				critId = rs.getInt(1);
			}
			for (int i = 0; i < c.getFieldList().size(); i++) {
				addField(critId, c.getFieldList().get(i));
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
	public int addField(int critId, Field fd) throws SQLException {
		int fdId = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO Field(name, idCriteria) " + "values( '"
					+ fd.getName() + "','" + critId + "' )";
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				fdId = rs.getInt(1);
			}
			for (int i = 0; i < fd.getCommentList().size(); i++) {
				addComment(fdId, fd.getCommentList().get(i));
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return fdId;
	}

	public int addComment(int FdId, Comment ct) throws SQLException {
		int ctId = 0;
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
			pstmt.setString(1, ct.getText());
			pstmt.setInt(2, FdId);
			pstmt.setString(3, ct.getType());
			pstmt.executeUpdate();
			System.out.println(sql);
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				ctId = rs.getInt(1);
			}
			for (int i = 0; i < ct.getEpComment().size(); i++) {
				addEpComment(ctId, ct.getEpComment().get(i));
			}
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close3(conn, pstmt, rs);
		}
		return ctId;
	}

	public void addEpComment(int ctId, String context) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			sql = "INSERT INTO `ExpandedComment`(text, idComment) values(?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, context);
			pstmt.setInt(2, ctId);
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

	public boolean addStudentInfo(int projectId, String studentNumber,
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

	public boolean editGroupNumber(int projectId, String studentNumber,
			int group) throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "UPDATE Students SET " + "groupNumber = '" + group + "' "
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

	public int getProjectId(String username, String projectName)
			throws SQLException {
		int id = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
//			int idLecturer = getLecturerId(username);
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "SELECT * FROM Project";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getString("primaryMail").equals(username)
						&& rs.getString("name").equals(projectName)) {
					id = rs.getInt("idProject");
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

	public ProjectInfo returnProjectInfo(int projectId) throws SQLException {
		ProjectInfo pj = new ProjectInfo();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Project";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idProject") == projectId) {

					pj.setUsername(rs.getString("primaryMail"));
					pj.setProjectName(rs.getString("name"));
					pj.setSubjectCode(rs.getString("subjectCode"));
					pj.setSubjectName(rs.getString("subjectName"));
					pj.setDescription(rs.getString("description"));
					pj.setDurationMin(rs.getInt("durationMin"));
					pj.setDurationSec(rs.getInt("durationSec"));
					pj.setWarningMin(rs.getInt("warningMin"));
					pj.setWarningSec(rs.getInt("warningSec"));
					pj.setCriteria(returnCriteria(projectId));
					pj.setStudentInfoList(returnStudents(projectId));
					pj.setAssistant(returnAssessors(projectId));
					pj.setCommentList(returnOnlyComment(projectId));

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
		return pj;
	}

	public ProjectInfo returnProjectDetails(int projectId) throws SQLException {
		ProjectInfo pj = new ProjectInfo();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Project";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idProject") == projectId) {
					pj.setUsername(rs.getString("primaryMail"));
					pj.setProjectName(rs.getString("name"));
					pj.setSubjectCode(rs.getString("subjectCode"));
					pj.setSubjectName(rs.getString("subjectName"));
					pj.setDescription(rs.getString("description"));
					pj.setAssistant(returnAssessors(projectId));
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
		return pj;
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
					cr.setFieldList(
							returnField(rs.getInt("idCriteria")));
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

	private ArrayList<Field> returnField(int idCriteria)
			throws SQLException {
		ArrayList<Field> fieldList = new ArrayList<Field>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Field";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idCriteria") == idCriteria) {
					Field fd = new Field();
					fd.setName(rs.getString("name"));
					fd.setCommentList(
							returnComment(rs.getInt("idSubSection")));
					fieldList.add(fd);
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

		return fieldList;
	}

	private ArrayList<Comment> returnComment(int idField)
			throws SQLException {
		ArrayList<Comment> commentList = new ArrayList<Comment>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Comment";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idField") == idField) {
					Comment ct = new Comment();
					ct.setType(rs.getString("type"));
					ct.setText(rs.getString("text"));
					ct.setEpComment(returnEpComment(rs.getInt("idComment")));
					commentList.add(ct);
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

	private ArrayList<String> returnEpComment(int idComment)
			throws SQLException {
		ArrayList<String> epComment = new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM `ExpendedCommnet`";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idComment") == idComment) {
					epComment.add(rs.getString("context"));
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
		return epComment;
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
			int i = cr.getFieldList().size();
			for (int j = 0; j < i; j++) {
				addSpecificComments(primaryKey, cr.getFieldList().get(j),
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

	public void addSpecificComments(int primaryKey, Field ss,
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
			pstmt.setString(3, ss.getCommentList().get(0).getText());
			pstmt.setString(4,
					ss.getCommentList().get(0).getEpComment().get(0));
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
					ArrayList<Field> ssList = returnSpecificComment(
							markId);
					cr.setFieldList(ssList);
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
					ArrayList<Feild> ssList = returnSpecificComment(
							markId);
					cr.setFieldList(ssList);
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

	private ArrayList<Field> returnSpecificComment(int markId)
			throws SQLException {
		ArrayList<Field> fieldList = new ArrayList<Field>();
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
					Field fd = new Field();
					fd.setName(rs.getString("subSection"));
					Comment ct = new Comment();
					ct.setText(rs.getString("shortText"));
					ct.getEpComment().add(rs.getString("comment"));
					fd.getCommentList().add(ct);
					fieldList.add(fd);
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

		return fieldList;
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