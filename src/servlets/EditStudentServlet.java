package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.RapidFeedback.InsideFunction;
import com.RapidFeedback.MysqlFunction;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName EditStudentServlet
 * @Description Servlet to edit a student information in a specific project in
 *              DB.
 *
 * @author Jingxian Hu, Zhongke Tan
 */
@WebServlet("/EditStudentServlet")
public class EditStudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditStudentServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ")
				.append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		MysqlFunction dbFunction = new MysqlFunction();

		// get JSONObject from request
		JSONObject jsonReceive;
		BufferedReader reader = request.getReader();
		String str, wholeString = "";
		while ((str = reader.readLine()) != null) {
			wholeString += str;
		}
		System.out.println("Receive: " + wholeString);
		jsonReceive = JSON.parseObject(wholeString);

		// get values from received JSONObject
		String token = jsonReceive.getString("token");
		String projectName = jsonReceive.getString("projectName");
		// studentID here is the student number.
		String studentID = jsonReceive.getString("studentID");
		String firstName = jsonReceive.getString("firstName");
		String middleName = jsonReceive.getString("middleName");
		String lastName = jsonReceive.getString("lastName");
		String email = jsonReceive.getString("email");

		ServletContext servletContext = this.getServletContext();
		StudentInfo student = new StudentInfo(studentID, firstName, middleName,
				lastName, email);

		/*
		 * Attention: This method is very like 'AddStudent' method, the
		 * difference is: we assume the StudentID cannot change, while other
		 * attributes can be changed.
		 */

		boolean updateStudent_ACK;
		// Mention:
		// call the SQL method to edit the student information whose student
		// number
		// is 'studentID'.
		// return the 'true' or 'false' value to updateStudent_ACK
		updateStudent_ACK = false;
		updateStudent_ACK = editStudent(dbFunction, servletContext, token,
				projectName, student);

		// construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("updateStudent_ACK", updateStudent_ACK);

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
	}

	/**
	 * @Function editStudent
	 * @Description call the SQL method to edit the student's information whose
	 *              student number is 'studentID' and in the project called
	 *              'projectName'.
	 *
	 * @param dbFunction
	 * @param servletContext
	 * @param token
	 * @param projectName
	 * @param student
	 * @return the result of edit student info.
	 */
	private boolean editStudent(MysqlFunction dbFunction,
			ServletContext servletContext, String token, String projectName,
			StudentInfo student) {
		boolean result = false;
		InsideFunction inside = new InsideFunction(dbFunction);
		String username = inside.token2user(servletContext, token);
		try {
			if (username != null && projectName != null) {
				int pid = dbFunction.getProjectId(username, projectName);
				if (pid <= 0) {
					throw new Exception(
							"Exception: Cannot find the project, or the user "
									+ "is not the primary assessor of the project.");
				}
				// only when student exists in this project, the student can be
				// edited.
				if (dbFunction.ifStudentExists(pid, student.getNumber()) > 0) {
					result = dbFunction.editStudentInfo(pid,
							student.getNumber(), student.getEmail(),
							student.getFirstName(), student.getSurname(),
							student.getMiddleName(), student.getGroup());
					return result;
				} else {
					return result;
				}
			} else {
				return result;
			}
		} catch (Exception e) {
			return result;
		}
	}

}
