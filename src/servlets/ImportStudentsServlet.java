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

//import org.graalvm.compiler.hotspot.aarch64.AArch64HotSpotBackend;

import com.RapidFeedback.InsideFunction;
import com.RapidFeedback.MysqlFunction;
import com.RapidFeedback.StudentInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.AddResult;
import com.mysql.cj.xdevapi.Result;

/**
 * @ClassName ImportStudentsServlet
 * @Description This servlet is to store a list of students of a project into
 *              the DB.
 *
 * @author Jingxian Hu, Zhongke Tan
 */
@WebServlet("/ImportStudentsServlet")
public class ImportStudentsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ImportStudentsServlet() {
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
		String studentListString = jsonReceive.getString("studentList");

		List<StudentInfo> studentList = JSONObject.parseArray(studentListString,
				StudentInfo.class);
		ArrayList<StudentInfo> arrayList;
		if (studentList instanceof ArrayList) {
			arrayList = (ArrayList<StudentInfo>) studentList;
		} else {
			arrayList = new ArrayList<StudentInfo>();
			arrayList.addAll(studentList);
		}

		ServletContext servletContext = this.getServletContext();

		boolean updateStudent_ACK;
		// Mention:
		// call the SQL method to import the student list
		// return the 'true' or 'false' value to updateStudent_ACK

		updateStudent_ACK = false;
		updateStudent_ACK = addStudentList(dbFunction, servletContext, token,
				arrayList, projectName);

		// construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("updateStudent_ACK", updateStudent_ACK);

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());

	}

	/**
	 * @Function addStudentList
	 * @Description call the SQL method to add a list of students to DB.
	 *
	 * @param dbFunction
	 * @param servletContext
	 * @param token
	 * @param list           it is studentList.
	 * @param projectName
	 * @return import result
	 */
	private boolean addStudentList(MysqlFunction dbFunction,
			ServletContext servletContext, String token,
			ArrayList<StudentInfo> list, String projectName) {
		boolean result = false;
		if (list.size() == 0 || list == null) {
			result = true;
			return result;
		}
		InsideFunction inside = new InsideFunction(dbFunction);
		for (StudentInfo student : list) {
			result = inside.addStudent(servletContext, token, projectName,
					student);
			if (result == false) {
				break;
			}
		}
		return result;
	}
}
