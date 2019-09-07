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
 * @ClassName DeleteStudentServlet
 * @Description The servlet to delete a student of a specific project in a
 *              database.
 *
 * @author Jingxian Hu, Zhongke Tan
 */
@WebServlet("/DeleteStudentServlet")
public class DeleteStudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteStudentServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ")
				.append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//
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
		String studentID = jsonReceive.getString("studentID");

		ServletContext servletContext = this.getServletContext();

		/*
		 * Attention: This method is to delete the student whose student number
		 * is 'studentID': we assume the Student number cannot change and is the
		 * primary key.
		 */

		boolean updateStudent_ACK;
		// Mention:
		// call the SQL method to delete the student whose student number
		// is 'studentID' from a project whose name is 'projectName'.
		// return the 'true' or 'false' value to updateStudent_ACK
		updateStudent_ACK = false;
		updateStudent_ACK = deleteStudent(dbFunction, servletContext, token,
				projectName, studentID);

		// construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("updateStudent_ACK", updateStudent_ACK);

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
	}

	/**
	 * @Function deleteStudent
	 * @Description call the db function to delete the student whose student
	 *              number is 'studentID' from a project whose name is
	 *              'projectName', and return deletion result.
	 *
	 * @param dbFunction
	 * @param servletContext
	 * @param token
	 * @param projectName
	 * @param studentID
	 * @return result of deleting a student in DB
	 */
	private boolean deleteStudent(MysqlFunction dbFunction,
			ServletContext servletContext, String token, String projectName,
			String studentID) {
		boolean result = false;
		InsideFunction inside = new InsideFunction(dbFunction);
		String username = inside.token2user(servletContext, token);
		try {
			int pid = dbFunction.getProjectId(username, projectName);
			if (pid <= 0) {
				throw new Exception(
						"Exception: Cannot find the project, or the user "
								+ "is not the primary assessor of the project.");
			}
			result = dbFunction.deleteStudent(pid, studentID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
