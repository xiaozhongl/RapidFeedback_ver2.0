package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.RapidFeedback.MysqlFunction;
import com.RapidFeedback.PDFUtil;
import com.RapidFeedback.Project;
import com.RapidFeedback.ProjectStudent;
import com.RapidFeedback.SendMail;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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
		// ifSendBoth: 1:send to student, 2: send to student and assessor.
		int ifSendBoth = jsonReceive.getIntValue("sendBoth");

		ServletContext servletContext = this.getServletContext();

		boolean sendMail_ACK = false;
		boolean deletefile_ACK = false;
		boolean recordSentEmail_ACK = false;

		/*
		 * add operation to get marklist, generate pdf and send mail.
		 */
		
		try {
		PDFUtil pdf = new PDFUtil();
		ProjectStudent projectStudent = dbFunction.getProjectStudent (studentId, projectId);
		Project project = dbFunction.getProject(projectId);
		String projectName = project.getName();
		String studentEmail = projectStudent.getEmail();
		String firstName = projectStudent.getFirstName();
		String filePath = servletContext.getRealPath("");
		int markerId = Token.tokenToUser(servletContext, token);
		if (dbFunction.isMarkerPrincipal(markerId, projectId)) {
			String studentNumber = String.valueOf(projectStudent.getStudentNumber());
			String fileName = projectName + "_" + studentNumber + ".pdf";
			String markerEmail = dbFunction.getMarkerEmail(markerId);
	
			pdf.create(projectStudent, project, filePath, fileName);
			boolean sendStudent = sendEmail(markerEmail, servletContext,
						projectName, studentEmail,
						firstName, studentNumber, filePath,
						fileName);
	
			if (ifSendBoth == 1) {
				sendMail_ACK = sendStudent;
			} else if (sendStudent && ifSendBoth == 2) {
				sendMail_ACK = sendEmail(markerEmail, servletContext, projectName, markerEmail,
							firstName, studentNumber, filePath, fileName);
				}
	
			// change sendEmail_flag
			recordSentEmail_ACK = dbFunction.sentMail(projectId, studentId);
	
			// delete files
			deletefile_ACK = pdf.deletePdf(filePath + fileName);
			}

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
