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
import com.RapidFeedback.Student;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName AddStudentServlet
 * @Description a servlet to add a student of a specific project into the
 *              database.
 *
 * @author Jingxian Hu, Zhongke Tan
 */
@WebServlet("/AddStudentServlet")
public class AddStudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddStudentServlet() {
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
		InsideFunction inside = new InsideFunction(dbFunction);

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
		//the studentID here is the studentNumber.
		String studentNumber = jsonReceive.getString("studentNumber");
		String firstName = jsonReceive.getString("firstName");
		String middleName = jsonReceive.getString("middleName");
		String lastName = jsonReceive.getString("lastName");
		String email = jsonReceive.getString("email");

		ServletContext servletContext = this.getServletContext();


		boolean updateStudent_ACK;
		updateStudent_ACK = false;
		// Mention:
		// call the SQL method to add a student
		// return the 'true' or 'false' value to updateStudent_ACK
		updateStudent_ACK = addStudent(servletContext, token,
				projectName, studentNumber, firstName, middleName, lastName, email);


		// construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("updateStudent_ACK", updateStudent_ACK);

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
	}


	/**
	 * @Function addStudent
	 * @Description An inner function of this servlet, which contains two steps:
	 *			first step is to add a new student to STUDENT TABLE according to given information,
	 *			second step is to get the studentId from STUDENT TABLE and add him to the project
	 *
	 * @param servletContext
	 * @param token
	 * @param projectName
	 * @param studentNumber
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param email
	 * @return the result of inserting a student into a DB.
	 */
	public boolean addStudent(ServletContext servletContext, String token,
			String projectName, String studentNumber, String firstName, String middleName,
					String lastName, String email) {
		String username = this.token2user(servletContext, token);
		InsideFunction inside = new InsideFunction(dbFunction);
		boolean result = false;
		try {
			int principalId = dbFunction.getMarkerId(username);		
			if (username != null && projectName != null) {
				int projectId = dbFunction.getProjectId(principalId, projectName);
				if (projectId <= 0) {
					throw new Exception(
							"Exception: Cannot find the project.");
				}else {
					result = dbFunction.addStudent(studentNumber, firstName,
							lastName, middleName,
							email);
					return result;
				}
			int studentId = dbFunction.getStudentId(studentNumber);	
			result = dbFunction.addStudentToProject(studentId, projectId);
			} else {
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
