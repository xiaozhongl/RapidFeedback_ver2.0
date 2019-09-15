

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MysqlFunction3
 * @Description All the functions which communicate with database.
 *
 * @author Dinghao Yong & Peng Kuang
 */
public class MysqlFunction {

	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/mydb?serverTimezone=UTC&useSSL=false";

	static final String USER = "root";
	static final String PASS = "Feedback123456";

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

	public int ifStudentExists(int idStudent) throws SQLException {
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
				if (rs.getInt("idStudent") == idStudent) {
					result = rs.getInt("idStudent");
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

	public boolean addStudentInfo(int idStudent, String studentNumber, String firstName, 
		String lastName, String middleName, String email) throws SQLException {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			double mark = -999.00;
			conn = connectToDB(DB_URL, USER, PASS);
			pstmt = conn.prepareStatement("INSERT INTO student VALUES (?,?,?,?,?,?)");
			pstmt.setInt(1, idStudent);
			pstmt.setString(2, studentNumber);
			pstmt.setString(3, firstName);
			pstmt.setString(4, lastName);
			pstmt.setString(5, middleName);
			pstmt.setString(6, email);
			Int countInserted = pstmt.executeUpdate();
			System.out.println(countInserted + " records inserted. \n");
			result=true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close4(conn, pstmt);
		}
		return result;
	}

	public boolean editStudentInfo(int studentId,
			String firstName, String lastName, String middleName, String studentNumber) throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "UPDATE student SET " + "firstName = '" + firstName + "', " + "lastName = '"
					+ lastName + "', " + "middleName = '" + middleName + "', "
					+ "studentNumber = '" + studentNumber
					+ "' " + "WHERE idStudent= " + "'" + studentId + "';  ";
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

	public boolean deleteStudent(int studentId) throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			sql = "DELETE FROM student " + "WHERE idStudent=studentId;";
			stmt.execute(sql);
			System.out.println(sql);
			System.out.println("student "+studentId+" is deleted");
			result = true;
		} catch (SQLException se) {
			// JDBC faults
			se.printStackTrace();
		} finally {
			close2(conn, stmt, rs);
		}
		return result;
	}

	public boolean addSelectedComment(int idProject, int idCriteria, int idStudent, int idMarker, int idExpandedComment) 
		throws SQLException {
			Connection conn = null;
			PreparedStatement pstmt = null;
			boolean result=false;
			try {
				conn = connectToDB(DB_URL, USER, PASS);
				pstmt = conn.prepareStatement("INSERT INTO selectedComment VALUES (?,?,?,?,?)");
				pstmt.setInt(1, idProject);
				pstmt.setInt(2, idCriteria);
				pstmt.setInt(3, idStudent);
				pstmt.setInt(4, idMarker);
				pstmt.setInt(5, idExpandedComment);
				Int countInserted = pstmt.executeUpdate();
				System.out.println(countInserted + " records inserted. \n");
				result = true;
			} catch (SQLException se) {
				// JDBC faults
				se.printStackTrace();
			} finally {
				close4(conn, pstmt);
			}
				return result;
	}
	
	public boolean editSelectedComment(int idProject, int idCriteria, int idStudent, int idMarker, int idExpandedComment)
		throws SQLException {
			Connection conn = null;
			PreparedStatement pstmt = null;
			boolean result = false;
			try {
				conn = connectToDB(DB_URL, USER, PASS);
				pstmt = conn.prepareStatement("UPDATE selectedComment SET idProject=?, idCriteria=?, idStudent=?, idMarker=?, idExpandedComment=? " 
						+ "WHERE idProject=?, idCriteria=?, idStudent=?, idMarker=?, idExpandedComment=?");
				Int countUpdated = pstmt.executeUpdate();
				System.out.println(countUpdateted + " records updated. \n");
				result = true;
			} catch (SQLException se) {
				// JDBC faults
				se.printStackTrace();
			} finally {
				close4(conn, pstmt);
			}
				return result;
	}
	
	
	public boolean deleteSelectedComment(int idProject, int idCriteria, int idStudent, int idMarker, int idExpandedComment)
		throws SQLException {
			Connection conn = null;
			PreparedStatement pstmt = null;
			boolean result = false;
			try {
				conn = connectToDB(DB_URL, USER, PASS);
				pstmt = conn.prepareStatement("DELETE FROM SelectedComment WHERE (idProject=?, idCriteria=?, idStudent=?, idMarker=?, idExpandedComment=?)");
				pstmt.setInt(1, idProject);
				pstmt.setInt(2, idCriteria);
				pstmt.setInt(3, idStudent);
				pstmt.setInt(4, idMarker);
				pstmt.setInt(5, idExpandedComment);
				Int countDeleted = pstmt.executeUpdate();
				System.out.println(countDeleted + " records deleted. \n");
				result=true;
			} catch (SQLException se) {
				// JDBC faults
				se.printStackTrace();
			} finally {
				close4(conn, stmt);
			}
				return result;
	}
	
	public boolean addAssessment( int idCriteria, int idProject, int idStudent, int idMarker, double score)
		throws SQLException {
			Connection conn = null;
			PreparedStatement pstmt = null;
			boolean result = false;
			try {
				conn = connectToDB(DB_URL, USER, PASS);
				pstmt = conn.prepareStatement("INSERT INTO assessment VALUES (?,?,?,?,?)");
				pstmt.setInt(1, idCriteria);
				pstmt.setInt(2, idProject);
				pstmt.setInt(3, idStudent);
				pstmt.setInt(4, idMarker);
				pstmt.setInt(5, score);
				Int countInserted = pstmt.executeUpdate();
				System.out.println(countInserted + " records inserted. \n");
				result=true;
			} catch (SQLException se) {
				// JDBC faults
				se.printStackTrace();
			} finally {
				close4(conn, pstmt);
			}
				return result;
	}

	public boolean editAssessment(int idCriteria, int idProject, int idStudent, int idMarker, double score)
			throws SQLException {
				Connection conn = null;
				PreparedStatement pstmt = null;
				boolean result = false;
				try {
					conn = connectToDB(DB_URL, USER, PASS);
					pstmt = conn.prepareStatement("UPDATE assessment SET idCriteria=?, idProject=?, idStudent=?, idMarker=?, score=? " 
							+ "WHERE idCriteria=?, idProject=?, idStudent=?, idMarker=?");
					Int countUpdated = pstmt.executeUpdate();
					System.out.println(countUpdateted + " records updated. \n");
					result = true;
				} catch (SQLException se) {
					// JDBC faults
					se.printStackTrace();
				} finally {
					close4(conn, pstmt);
				}
					return result;
		}
	
	public boolean deleteAssessmment(int idCriteria, int idProject, int idStudent, int idMarker, double score)
		throws SQLException {
			Connection conn = null;
			PreparedStatement pstmt = null;
			boolean result = false;
			try {
				conn = connectToDB(DB_URL, USER, PASS);
				pstmt = conn.prepareStatement("DELETE FROM SelectedComment WHERE (idProject=?, idCriteria=?, idStudent=?, idMarker=?, score=?)");
				pstmt.setInt(1, idCriteria);
				pstmt.setInt(2, idProject);
				pstmt.setInt(3, idStudent);
				pstmt.setInt(4, idMarker);
				pstmt.setDouble(5, score);
				Int countDeleted = pstmt.executeUpdate();
				System.out.println(countDeleted + " records deleted. \n");
				result=true;
			} catch (SQLException se) {
				// JDBC faults
				se.printStackTrace();
			} finally {
				close4(conn, stmt);
			}
				return result;
	}

	public StudentInfo returnOneStudentInfo(int studentId) 
		throws SQLException {
		StudentInfo studentInfo = null;
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = connectToDB(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM student";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idStudent") == studentId) {
					studentInfo = new StudentInfo(rs.getString("studentNumber"),
							rs.getString("firstName"),
							rs.getString("middleName"), rs.getString("lastName"));
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

	public String returnOtherComment(int lecturerId, int studentId) throws SQLException {
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

	public void close1(Connection conn, Statement stmt) 
		throws SQLException {
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
	
	public void close4(Connection conn, PreparedStatement pstmt) 
			throws SQLException {
			try {
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			} finally {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			}
	}

}
