package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.RapidFeedback.MysqlFunction;
import com.RapidFeedback.Student;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * @ClassName AddStudentServlet
 * @Description a servlet to add a student of a specific project into the
 *              database.
 *
 * @author Xizhi Geng
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
		int projectId = jsonReceive.getIntValue("projectId");
		String studentListString = jsonReceive.getString("studentList");
		ServletContext servletContext = this.getServletContext();
		int loginId = Token.tokenToUser(servletContext, token);
		int ack = 0;
		
		if (dbFunction.isMarkerPrincipal(loginId, projectId)) {
			List<Student> studentList= new Gson().fromJson(studentListString,
					new TypeToken<List<Student>>(){}.getType());
			ArrayList<Student> studentArrayList = new ArrayList();
			studentArrayList.addAll(studentList);


			// Mention:
			// call the SQL method to add a student
			// return the 'true' or 'false' value to updateStudent_ACK
			//addStudent will only return id which is an int, 0 or minus number both means fail, which will lead to a false to updateStudent_ACK
			//postive id will lead to a true updateStudent_ACK
			ack = dbFunction.addStudents(studentArrayList, projectId);
		}

		// construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("updateStudent_ACK", ack);

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
	}
}
