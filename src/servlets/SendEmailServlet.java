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

import com.RapidFeedback.Criteria;
import com.RapidFeedback.InsideFunction;
import com.RapidFeedback.Mark;
import com.RapidFeedback.MysqlFunction;
import com.RapidFeedback.PDFUtil;
import com.RapidFeedback.ProjectInfo;
import com.RapidFeedback.SendMail;
import com.RapidFeedback.StudentInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 * @ClassName SendEmailServlet
 * @Description Generate pdf result report for a student, and email the report,
 *              and delete the local pdf file.
 *
 * @author Jingxian Hu, Zhongke Tan
 */
@WebServlet("/SendEmailServlet")
public class SendEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SendEmailServlet() {
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
		String studentNumber = jsonReceive.getString("studentID");
		// ifSendBoth: 1:send to student, 2: send to student and assessor.
		int ifSendBoth = jsonReceive.getIntValue("sendBoth");

		ServletContext servletContext = this.getServletContext();

		boolean sendMail_ACK = false;

		boolean deletefile_ACK = false;
		boolean recordSentEmail_ACK = false;

		/*
		 * add operation to get marklist, generate pdf and send mail.
		 */
		PDFUtil pdf = new PDFUtil();
		String userEmail = inside.token2user(servletContext, token);
		String filePath = servletContext.getRealPath("");
		String fileName = projectName + "_" + studentNumber + ".pdf";

		try {
			int projectId = dbFunction.getProjectId(userEmail, projectName);
			if (projectId <= 0) {
				throw new Exception(
						"Exception: Cannot find the project, or the user is "
								+ "not the primary assessor of the project.");
			}
			System.out.println("projectId: " + projectId);
			ProjectInfo pj = dbFunction.returnProjectDetails(projectId);
			int studentId = dbFunction.ifStudentExists(projectId,
					studentNumber);
			StudentInfo studentInfo = dbFunction
					.returnOneStudentInfo(studentId);
			System.out.println("studentEmail: " + studentInfo.getEmail());

			// get marklist
			ArrayList<String> assessors = dbFunction.returnAssessors(projectId);
			ArrayList<Mark> markList = new ArrayList<Mark>();
			for (String lecturer : assessors) {
				int lecturerId = dbFunction.getLecturerId(lecturer);

				Mark mark = dbFunction.returnMark(projectId, lecturerId,
						studentId);
				markList.add(mark);
			}
			// get marklist end

			pdf.create(markList, pj, studentInfo, filePath, fileName);

			boolean sendStudent = sendEmail(userEmail, servletContext,
					projectName, studentInfo.getEmail(),
					studentInfo.getFirstName(), studentNumber, filePath,
					fileName);

			if (ifSendBoth == 1) {
				sendMail_ACK = sendStudent;
			} else if (sendStudent && ifSendBoth == 2) {
				sendMail_ACK = sendEmail(userEmail, servletContext, projectName,
						userEmail, studentInfo.getFirstName(), studentNumber,
						filePath, fileName);
			}

			// change sendEmail_flag
			recordSentEmail_ACK = dbFunction.editsentMail(projectId,
					studentNumber);

			// delete files
			deletefile_ACK = pdf.deletePdf(filePath + fileName);

		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONObject jsonSend = new JSONObject();
		jsonSend.put("sendMail_ACK", sendMail_ACK);

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
		System.out.println("recordSentEmail_ACK: " + recordSentEmail_ACK);
		System.out.println("deletefile_ACK: " + deletefile_ACK);
		System.out.println("Send: " + jsonSend.toJSONString());
	}

	/**
	 * @Function sendEmail
	 * @Description email the pdf report the target email address
	 *
	 * @param userEmail
	 * @param servletContext
	 * @param projectName
	 * @param targetEmail
	 * @param firstName
	 * @param studentNumber
	 * @param filePath
	 * @param fileName
	 * @return the result of sending email.
	 */
	public boolean sendEmail(String userEmail, ServletContext servletContext,
			String projectName, String targetEmail, String firstName,
			String studentNumber, String filePath, String fileName) {
		boolean result = false;
		SendMail send = new SendMail();
		String subject = projectName + " Presentation Result for "
				+ studentNumber;
		String msg = "Hi " + firstName + ",\n\n"
				+ "This is a feedback for your " + projectName
				+ "Presentation.\n\n"
				+ "If you have any problems, please don\'t hesitate to contact the lecturers/tutors: "
				+ userEmail + "\n\n" + "Regards,\n" + "RapidFeedback Team";
		String host = "smtp.gmail.com";
		String user = "feedbackrapid@gmail.com";
		String pwd = "gkgkbzzbavwowfbh";
		send.setAddress(user, targetEmail, subject);
		send.setAffix(filePath + fileName, fileName);
		result = send.send(host, user, pwd, msg);
		return result;
	}
}
