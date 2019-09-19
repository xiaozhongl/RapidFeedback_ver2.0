	/*Will change all "Marker" to THE PREVIOUS "Lecturer" to make life easier.
	/**所有相关variable都校正下，然后不需要改动的再删掉


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

//将之前的Lecturers_Comment_Student变成Feedback
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
			sql = "INSERT INTO Feedback(idProject, idlecturers, idStudents, textRemark, audioRemark) values(?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idProject);
			pstmt.setInt(2, idlectuer);
			pstmt.setInt(3, idStudent);
			pstmt.setTinytext(4, textRemark);
			pstmt.setMediumBlob(5, audioRemark);
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

//将之前的Lecturers_Comment_Student变成Feedback
//原本comment和totalMark都是这个表和mark表里提供的，现在feedback只提供comment了，mark还要从这里return吗？还是从assessment里？是从studentinProject里返回吧？
	
//将之前的Lecturers_Comment_Student变成Feedback
//返回了textRemark，audioRemark怎么返回？
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
			sql = "SELECT * FROM Feedback";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				if (rs.getInt("idLecturers") == lecturerId
						&& rs.getInt("idStudents") == studentId) {
					comment = rs.getString("textRemark");
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

//Feedback的表里是没有mark的，那么如果想删除分数是指在studentinProject和assessment里面删？
//删除mark从assessment和studentinProject里面删
