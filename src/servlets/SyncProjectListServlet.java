package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.RapidFeedback.MysqlFunction;
import com.RapidFeedback.Project;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class SyncProjectListServlet
 */
@WebServlet("/SyncProjectListServlet")
public class SyncProjectListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SyncProjectListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		MysqlFunction dbFunction = new MysqlFunction();

		JSONObject jsonReceive;
		BufferedReader reader = request.getReader();
		String str, wholeString = "";

		while ((str = reader.readLine()) != null) {
			wholeString += str;
		}
		System.out.println("Receive: " + wholeString);
		jsonReceive = JSON.parseObject(wholeString);
		String token = jsonReceive.getString("token");
		int markerId = jsonReceive.getIntValue("userId");

		ServletContext servletContext = this.getServletContext();
		int loginId = Token.tokenToUser(servletContext, token);
		String projectListString = null;
		boolean syn_ACK = false;
		if (loginId == markerId ) {
			try {
				ArrayList<Project> projectList = new ArrayList<Project>();
				projectList = dbFunction.getProjectList(markerId);
				projectListString = JSON.toJSONString(projectList);
				syn_ACK = true;
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		
		
		
		// construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("syn_ACK", syn_ACK);
		jsonSend.put("projectList", projectListString);
		

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
		System.out.println("Send: " + jsonSend.toJSONString());
	}

}
