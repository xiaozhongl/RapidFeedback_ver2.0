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
 * @ClassName GroupStudentServlet
 * @Description This servlet is to change the group number of a student in the
 *              DB.
 *
 * @author Jingxian Hu, Zhongke Tan, Xizhi Geng
 */
@WebServlet("/GroupStudentServlet")
public class GroupStudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GroupStudentServlet() {
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
		String studentNumber = jsonReceive.getString("studentNumber");
		int group = jsonReceive.getInteger("group");

		ServletContext servletContext = this.getServletContext();

		boolean updateGroupNumber_ACK;
		// Mention:
		// call the SQL method to edit the student groupID of a certain studentNumber
		// get the markerID from principalMarker username(email)
		// get the projectId from principalMarker username and projectName
		// get the studentId from studentNumber
		// return the 'true' or 'false' value to updateStudent_ACK
		String username = inside.token2user(servletContext, token);
		try {
			int principalId = dbFunction.getMarkerId(username);
			int projectId = dbFunction.getProjectId(principalId, projectName);
			if (projectId <= 0) {
				throw new Exception(
						"Exception: Cannot find the project, or the user is "
								+ "not the primary assessor of the project.");
			}
			int studentId = dbFunction.getStudentId(studentNumber);
			updateGroupNumber_ACK = dbFunction.updateGroupNumber(projectId,
					studentId, group);
		} catch (Exception e) {
			updateGroupNumber_ACK = false;
			e.printStackTrace();
		}

		// construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("updateStudent_ACK", updateGroupNumber_ACK);

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
		System.out.println("Send: " + jsonSend.toJSONString());
	}

}
