package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.RapidFeedback.MysqlFunction;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName LoginServlet
 * @Description servlet to verify the user and log in. After logging
 *              successfully, the servlet will return all the projects' info of
 *              this user and issue a token to user.
 *
 * @author Jingxian Hu, Zhongke Tan
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at:")
				.append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		MysqlFunction dbFunction = new MysqlFunction();

		JSONObject jsonReceive;
		BufferedReader reader = request.getReader();
		String str, wholeString = "";

		while ((str = reader.readLine()) != null) {
			wholeString += str;
		}
		System.out.println("Receive: " + wholeString);
		jsonReceive = JSON.parseObject(wholeString);
		String username = jsonReceive.getString("username");
		String password = jsonReceive.getString("password");

		ServletContext servletContext = this.getServletContext();
		JSONObject jsonSend = new JSONObject();

		/*
		 * login_ACK: successful login return lecturer's id, -2 = exception
		 * occurs in the servlet, -1 = wrong e-mail address, 0 = wrong password
		 */
		int login_ACK;
		try {
			login_ACK = dbFunction.logIn(username, password);

			if (login_ACK > 0) {
				// delete old token-username pair in the ServletContext
				delOldToken(servletContext, username);
				// generate new token and save new token-username pair to the
				// ServletContext
				String token = newToken(request, username);
				servletContext.setAttribute(token, username);
				ProjectInfo[] projectList = getProjectList(dbFunction,
						username);
				String projectListString = JSON.toJSONString(projectList);
				int id = dbFunction.getLecturerId(username);
				String firstName = dbFunction.getLecturerName(id);
				jsonSend.put("projectList", projectListString);
				jsonSend.put("firstName", firstName);
				jsonSend.put("login_ACK", login_ACK);
				jsonSend.put("token", token);
			} else {
				jsonSend.put("login_ACK", login_ACK);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			login_ACK = -2;
		}

		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
		System.out.println("Send: " + jsonSend.toJSONString());

	}

	/**
	 * @Function delOldToken
	 * @Description delete old token-username pair in the ServletContext
	 *
	 * @param servletContext
	 * @param userName
	 */
	private void delOldToken(ServletContext servletContext, String userName) {
		Enumeration<String> e = servletContext.getAttributeNames();
		while (e.hasMoreElements()) {
			String token = (String) e.nextElement();
			if (servletContext.getAttribute(token) == userName) {
				servletContext.removeAttribute(token);
			}
		}
	}

	/**
	 * @Function newToken
	 * @Description generate new token (a unique string) for a user.
	 *
	 * @param request
	 * @param userName
	 * @return new token
	 */
	private String newToken(HttpServletRequest request, String userName) {
		String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
				.format(new Date());
		// token can has nothing to do with the sessionID and time, I just use
		// them to build a unique string.
		String token = request.getSession().getId() + timestamp;
		return token;
	}

	/**
	 * @Function getProjectList
	 * @Description call SQL function to get all the project info of a user.
	 *
	 * @param db
	 * @param userName
	 * @return ProjectInfo array which belongs to this user.
	 * @throws SQLException
	 */
	private ProjectInfo[] getProjectList(MysqlFunction db, String userName)
			throws SQLException {
		List<Integer> pIDs = db.queryProjects(userName);
		ProjectInfo[] projectList = new ProjectInfo[pIDs.size()];
		for (int i = 0; i < pIDs.size(); i++) {
			projectList[i] = db.returnProjectInfo(pIDs.get(i));
		}
		return projectList;
	}

}
