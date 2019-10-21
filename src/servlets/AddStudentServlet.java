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
import com.RapidFeedback.Student;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.*;

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
		String projectId = jsonReceive.getString("projectId");
		
		// String studentListString = jsonReceive.getString("studentList");
		// List<StudentInfo> studentList = JSONObject.parseArray(studentListString,StudentInfo.class);
		// ArrayList<StudentInfo> studentArrayList = new ArrayList(); 
		// studentArrayList.addAll(studentList);


		String studentList = jsonReceive.getString("studentList");
  		ArrayList <Student> stuList= (ArrayList<Student>) JSON.parseArray(studentList,Student.class);



		// //the studentID here is the studentNumber.
		// String studentNumber = jsonReceive.getString("studentNumber");
		// String firstName = jsonReceive.getString("firstName");
		// String middleName = jsonReceive.getString("middleName");
		// String lastName = jsonReceive.getString("lastName");
		// String email = jsonReceive.getString("email");

		ServletContext servletContext = this.getServletContext();


		boolean updateStudent_ACK;
		updateStudent_ACK = false;
		// Mention:
		// call the SQL method to add a student
		// return the 'true' or 'false' value to updateStudent_ACK
		//addStudent will only return id which is an int, 0 or minus number both means fail, which will lead to a false to updateStudent_ACK
		//postive id will lead to a true updateStudent_ACK
		int addResult = 0;
  		for (Student stu : stuList){
			addResult = dbFunction.addStudent(stu.getFirstName(), stu.getMiddleName(), stu.getLastName(), stu.getStudentNumber(), stu.getEmail(), projectId);
			if (addResult < 1){
				break;
			}
		}
		return addResult;

		if (addResult > 0) {
			updateStudent_ACK = true;
		}



		// construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("updateStudent_ACK", updateStudent_ACK);

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
	}


}
