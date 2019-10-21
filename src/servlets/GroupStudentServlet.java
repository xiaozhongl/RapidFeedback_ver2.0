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

import com.RapidFeedback.MysqlFunction;
import com.RapidFeedback.ProjectStudent;
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
		String projectId = jsonReceive.getString("projectId");
		String studentId = jsonReceive.getString("studentId");
		int group = jsonReceive.getInteger("group");

		ServletContext servletContext = this.getServletContext();

		boolean updateGroupNumber_ACK;
		updateStudent_ACK = false;
		// Mention:
		// call the SQL method to edit the student groupID of a certain studentNumber
		// get the group from FE
		// get the projectId from FE
		// get the studentId from FE
		// return the 'true' or 'false' value to updateStudent_ACK
		updateGroupNumber_ACK = dbFunction.updateGroupNumber(projectId,	studentId, group);

		// construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("updateStudent_ACK", updateGroupNumber_ACK);

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
		System.out.println("Send: " + jsonSend.toJSONString());
	}

}
