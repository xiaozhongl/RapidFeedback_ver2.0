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

import com.RapidFeedback.Assessment;
import com.RapidFeedback.MysqlFunction;
import com.RapidFeedback.Remark;
import com.RapidFeedback.Student;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @ClassName MarkServlet
 * @Description servlet to store the student's mark given by a lecturer to the
 *              DB.
 *
 * @author Jingxian Hu, Zhongke Tan, Meng Ru
 */
@WebServlet("/AddResultServlet")
public class AddResultServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddResultServlet() {
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
        boolean mark_ACK = false;

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
        int studentId = jsonReceive.getIntValue("studentId");
        String remarkSting = jsonReceive.getString("remark");
        ServletContext servletContext = this.getServletContext();
        int LecturerId = Token.tokenToUser(servletContext, token);

        if (dbFunction.isMarkerInProject(LecturerId, projectId)) {

            Remark remark = new Gson().fromJson(remarkSting, new TypeToken<Remark>(){}.getType());

//            ArrayList <Assessment> assessList = (ArrayList<Assessment>) JSON.parseArray(assessmentList, Assessment.class);
//            remark.setAssessmentList(assessList);
            mark_ACK = dbFunction.addResult(remark, projectId, studentId);
        }

        // Mention:
        // call the SQL method to save mark and comments of this student.
        // return the 'true' or 'false' value to mark_ACK



        // construct the JSONObject to send
        JSONObject jsonSend = new JSONObject();
        jsonSend.put("ACK", mark_ACK);

        // send
        PrintWriter output = response.getWriter();
        output.print(jsonSend.toJSONString());
        System.out.println("Send: " + jsonSend.toJSONString());
    }
}

//	/**
//	 * @Function addResult
//	 * @Description call the SQL method to save a student's mark and comments
//	 *              given by one lecturer.
//	 *
//	 * @param inside
//	 * @param dbFunction
//	 * @param servletContext
//	 * @param token
//	 * @param projectName
//	 * @param studentNumber
//	 * @param grade
//	 * @param primaryEmail
//	 * @return result whether adding mark successfully
//	 */
//	private boolean addResult(InsideFunction inside, MysqlFunction dbFunction,
//			ServletContext servletContext, String token, String projectName,
//			String studentNumber, Mark grade, String primaryEmail) {
//		boolean result = false;
//
//		try {
//			String username = inside.token2user(servletContext, token);
//			int uid = dbFunction.getLecturerId(username);
//			int pid = dbFunction.getProjectId(primaryEmail, projectName);
//			int studentID = dbFunction.ifStudentExists(pid, studentNumber);
//			ArrayList<Criteria> criteriaList = grade.getCriteriaList();
//			ArrayList<Double> markList = grade.getMarkList();
//			ArrayList<Criteria> commentList = grade.getCommentList();
//			if (criteriaList.size() != markList.size()) {
//				System.out.println(
//						"Error: MarkList and criteriaList does not have the same size");
//				return result;
//			}
//
//			String studentName = dbFunction.returnOneStudentInfo(studentID)
//					.getFirstName();
//
//			if (!dbFunction.deleteMark(uid, studentID)) {
//				System.out.println(
//						"Error: Cannot delete old mark before adding new mark!");
//				throw new Exception(
//						"Exception: Cannot delete old mark before adding new mark!");
//			}
//
//			// write criteria list with mark to the DB.
//			for (int i = 0; i < markList.size(); i++) {
//
//				int ack = dbFunction.writeIntoMark(uid, studentID,
//						criteriaList.get(i), markList.get(i).doubleValue(), 0,
//						studentName);
//
//				if (ack <= 0) {
//					System.out.println("Error: The " + i
//							+ "th mark result cannot be added to the database.");
//					return result;
//				}
//			}
//
//			// write criteria list without mark to the DB.
//			for (int i = 0; i < commentList.size(); i++) {
//				int ack = dbFunction.writeIntoMark(uid, studentID,
//						commentList.get(i), -1.0, 1, studentName);
//				if (ack <= 0) {
//					System.out.println("Error: The " + i
//							+ "th comment result cannot be added to the database.");
//					return result;
//				}
//			}
//
//			// write additional comment into the DB.
//			if (dbFunction.writeIntoComment(uid, studentID, grade.getComment(),
//					grade.getTotalMark())) {
//				// if the user is the primary assessor, the totalMark in student
//				// table of DB needs to be changed.
//				if (username.equals(primaryEmail)) {
//					result = dbFunction.editStudentMark(studentID,
//							grade.getTotalMark());
//					if (!result) {
//						System.out.println(
//								"Error:Cannot edit students' mark in student table");
//					}
//				} else {
//					result = true;
//				}
//			} else {
//				System.out.println("Error: Cannot insert additional comment.");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return result;
//		}
//		return result;
//	}
//
//}
