package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PrintWriter;
import java.sql.SQLException;

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
 * @ClassName UpdateProject_Time_Servlet
 * @Description update the project time setting info to the DB.
 *
 * @author Jingxian Hu, Zhongke Tan
 */
@WebServlet("/UpdateProject_Time_Servlet")
public class UpdateProject_Time_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateProject_Time_Servlet() {
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
		int durationMin = jsonReceive.getIntValue("durationMin");
		int durationSec = jsonReceive.getIntValue("durationSec");
		int warningMin = jsonReceive.getIntValue("warningMin");
		int warningSec = jsonReceive.getIntValue("warningSec");

		ServletContext servletContext = this.getServletContext();

		boolean updateProject_ACK;
		// Mention:
		// call the SQL method to save the 'Time' page
		// return the 'true' or 'false' value to update_ACK
		updateProject_ACK = false;
		try {
			updateProject_ACK = projectP2(dbFunction, servletContext, token,
					projectName, durationMin, durationSec, warningMin,
					warningSec);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("updateProject_ACK", updateProject_ACK);

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
		System.out.println("Send: " + jsonSend.toJSONString());
	}

	/**
	 * @Function projectP2
	 * @Description call the SQL method to save the 'Time' page (P2)
	 *
	 * @param dbFunction
	 * @param servletContext
	 * @param token
	 * @param projectName
	 * @param durationMin
	 * @param durationSec
	 * @param warningMin
	 * @param warningSec
	 * @return update result
	 * @throws SQLException
	 */
	private boolean projectP2(MysqlFunction dbFunction,
			ServletContext servletContext, String token, String projectName,
			int durationMin, int durationSec, int warningMin, int warningSec)
			throws SQLException {
		InsideFunction in = new InsideFunction(dbFunction);
		String username = in.token2user(servletContext, token);
		if (username == null || username.isEmpty()) {
			return false;
		}
		int pid = dbFunction.getProjectId(username, projectName);
		if (pid <= 0) {
			return false;
		}
		System.out.println(pid);
		System.out.println(durationMin);
		System.out.println(durationSec);
		System.out.println(warningMin);
		System.out.println(warningSec);
		return dbFunction.updateTimeInformation(pid, durationMin, durationSec,
				warningMin, warningSec);
	}

}
