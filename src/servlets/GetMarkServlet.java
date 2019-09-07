package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.RapidFeedback.InsideFunction;
import com.RapidFeedback.Mark;
import com.RapidFeedback.MysqlFunction;
import com.RapidFeedback.ProjectInfo;
import com.RapidFeedback.StudentInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName GetMarkServlet
 * @Description servlet which returns the mark of a student/group of students.
 *              When return the mark of a group (the request student list's size
 *              > 1), in addition to a mark list, an collection of additional
 *              comments are returned. Only when all the assessors completed the
 *              assessment, this servlet will return the mark.
 *
 * @author Jingxian Hu, Zhongke Tan
 */
@WebServlet("/GetMarkServlet")
public class GetMarkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetMarkServlet() {
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
		String studentNumberListString = jsonReceive
				.getString("studentNumberList");
		String primaryEmail = jsonReceive.getString("primaryEmail");

		List<String> list = JSONObject.parseArray(studentNumberListString,
				String.class);
		ArrayList<String> studentNumberList = new ArrayList<String>();
		studentNumberList.addAll(list);

		ServletContext servletContext = this.getServletContext();

		boolean getMark_ACK = false;
		String markListString = "";
		String otherCommentsString = "";
		String username = inside.token2user(servletContext, token);

		/*
		 * This hash map stores all the additional comments for a group of
		 * students.
		 * 
		 * For example, there are 4 assessors, and a group has 2 students. The
		 * size of the hash map will be 8.
		 * 
		 * The key of the hash map is studentNumber::lecturerEmail, the value is
		 * the additional comment this lecturer made for the student.
		 */
		HashMap<String, String> otherComments = new HashMap<String, String>();
		try {
			if (username == null) {
				throw new Exception(
						"Exception: Authentication error, please log in again.");
			}
			if (studentNumberList.size() < 1) {
				throw new Exception(
						"Exception: StudentNumberList length less than 1");
			}

			int projectId = dbFunction.getProjectId(primaryEmail, projectName);
			ArrayList<String> assessors = dbFunction.returnAssessors(projectId);
			ArrayList<Mark> markList = new ArrayList<Mark>();
			String studentNumber_0 = studentNumberList.get(0);
			int studentId_0 = dbFunction.ifStudentExists(projectId,
					studentNumber_0);
			for (String lecturer : assessors) {
				int lecturerId = dbFunction.getLecturerId(lecturer);
				Mark mark = dbFunction.returnMark(projectId, lecturerId,
						studentId_0);
				markList.add(mark);
				if (studentNumberList.size() > 1) {
					for (String studentNumber : studentNumberList) {
						int studentId = dbFunction.ifStudentExists(projectId,
								studentNumber);
						String comment = dbFunction
								.returnOtherComment(lecturerId, studentId);
						if (comment == null) {
							comment = "";
						}
						otherComments.put(studentNumber + "::" + lecturer,
								comment);
						System.out.println(otherComments
								.get(studentNumber + "::" + lecturer));
					}
				}
			}

			markListString = JSON.toJSONString(markList);
			otherCommentsString = JSON.toJSONString(otherComments);

			getMark_ACK = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		// construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("getMark_ACK", getMark_ACK);
		if (getMark_ACK == true) {
			jsonSend.put("markList", markListString);
			if (studentNumberList.size() > 1) {
				jsonSend.put("otherComments", otherCommentsString);
			}
		}

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
		System.out.println("Send: " + jsonSend.toJSONString());
	}

}
