package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.RapidFeedback.InsideFunction;
import com.RapidFeedback.MysqlFunction;
import com.RapidFeedback.SendMail;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName InviteAssessorServlet
 * @Description This servlet is to invite (POST) or delete (DELETE) the assessor
 *              in a project assessment. After the invitation or the deletion,
 *              the assessor will receive a email notification.
 *
 * @author Jingxian Hu, Zhongke Tan
 */
@WebServlet("/InviteAssessorServlet")
public class InviteAssessorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InviteAssessorServlet() {
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
	 * 
	 *      description: invite the assessor.
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		MysqlFunction dbFunction = new MysqlFunction();
		InsideFunction inside = new InsideFunction(dbFunction);

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
		// other arguments
		String projectName = jsonReceive.getString("projectName");
		String assessorEmail = jsonReceive.getString("assessorEmail");

		ServletContext servletContext = this.getServletContext();

		boolean invite_ACK = false;
		String inviter = inside.token2user(servletContext, token);
		// the ids hash map stores the assessorID and projectID.
		HashMap<String, Integer> ids = getIDs(inside, servletContext, inviter,
				assessorEmail, projectName, dbFunction);
		if (ids.size() == 2) {
			try {
				invite_ACK = dbFunction.addOtherAssessor(ids.get("assessorID"),
						ids.get("projectID"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		boolean sendMail_ACK = false;
		// if the assessor is added into the project assessment in the DB
		// successfully, then send the email notification to the assessor.
		if (invite_ACK) {
			try {
				String subject = "Invitation from the project <" + projectName
						+ ">";
				String inviterName = dbFunction
						.getLecturerName(dbFunction.getLecturerId(inviter));
				String assessorName = dbFunction
						.getLecturerName(ids.get("assessorID"));
				String msg = "Hi " + assessorName + ",\r\n\r\n"
						+ "We’re just writing to let you know that you’ve joined the assessment of"
						+ " the project <" + projectName
						+ "> on RapidFeedback, which was invited by the "
						+ "project's primary assessor " + inviterName
						+ "\r\n\r\n" + "Regards,\r\n" + "RapidFeedback Team";
				sendMail_ACK = sendInvitation(inside, servletContext,
						projectName, assessorEmail, dbFunction, subject, msg);
				sendMail_ACK = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		JSONObject jsonSend = new JSONObject();
		jsonSend.put("invite_ACK", invite_ACK);
		jsonSend.put("assessorEmail", assessorEmail);
		jsonSend.put("sendMail_ACK", sendMail_ACK);

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
		System.out.println("Send: " + jsonSend.toJSONString());

	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 * 
	 *      description: delete the assessor.
	 */
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		MysqlFunction dbFunction = new MysqlFunction();
		InsideFunction inside = new InsideFunction(dbFunction);

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
		// other arguments
		String projectName = jsonReceive.getString("projectName");
		String assessorEmail = jsonReceive.getString("assessorEmail");

		ServletContext servletContext = this.getServletContext();

		boolean delete_ACK = false;

		String inviter = inside.token2user(servletContext, token);

		HashMap<String, Integer> ids = getIDs(inside, servletContext, inviter,
				assessorEmail, projectName, dbFunction);

		if (ids.size() == 2) {
			try {
				delete_ACK = dbFunction.deleteAssessor(ids.get("assessorID"),
						ids.get("projectID"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		boolean sendMail_ACK = false;
		// if the assessor is deleted from the project assessment in the DB
		// successfully, then send the email notification to the assessor.
		if (delete_ACK) {
			try {
				String subject = "Notification from the project <" + projectName
						+ ">";
				String assessorName = dbFunction
						.getLecturerName(ids.get("assessorID"));
				String msg = "Hi " + assessorName + ",\r\n\r\n"
						+ "We’re just writing to let you know that you’ve left the assessment of"
						+ " the project <" + projectName
						+ "> on RapidFeedback\r\n\r\n" + "Regards,\r\n"
						+ "RapidFeedback Team";
				sendMail_ACK = sendInvitation(inside, servletContext,
						projectName, assessorEmail, dbFunction, subject, msg);
				sendMail_ACK = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		JSONObject jsonSend = new JSONObject();
		jsonSend.put("delete_ACK", delete_ACK);
		jsonSend.put("assessorEmail", assessorEmail);
		jsonSend.put("sendMail_ACK", sendMail_ACK);

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
		System.out.println("Send: " + jsonSend.toJSONString());
	}

	/**
	 * @Function getIDs
	 * @Description return the assessorID and projectID in a hash map.
	 *
	 * @param inside
	 * @param servletContext
	 * @param inviter
	 * @param assessorEmail
	 * @param projectName
	 * @param dbFunction
	 * @return a hash map which includes the assessorID and projectID
	 */
	private HashMap<String, Integer> getIDs(InsideFunction inside,
			ServletContext servletContext, String inviter, String assessorEmail,
			String projectName, MysqlFunction dbFunction) {
		HashMap<String, Integer> ids = new HashMap<String, Integer>();

		try {
			int assessorID = dbFunction.getLecturerId(assessorEmail);
			if (assessorID <= 0) {
				System.out.println("assessor not exists");
				return ids;
			}
			int projectID = dbFunction.getProjectId(inviter, projectName);
			System.out.println(inviter);
			System.out.println(projectName);
			System.out.println(projectID);
			if (projectID <= 0) {
				System.out.println("project not exists");
				throw new Exception(
						"Exception: Cannot find the project, or the user is not the primary assessor of the project.");
			}
			ids.put("assessorID", assessorID);
			ids.put("projectID", projectID);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ids;
	}

	/**
	 * @Function sendInvitation
	 * @Description send email notification to the assessor.
	 *
	 * @param inside
	 * @param servletContext
	 * @param projectName
	 * @param assessorEmail
	 * @param dbFunction
	 * @param subject
	 * @param msg
	 * @return result of sending email.
	 */
	public boolean sendInvitation(InsideFunction inside,
			ServletContext servletContext, String projectName,
			String assessorEmail, MysqlFunction dbFunction, String subject,
			String msg) {
		boolean result = false;
		SendMail send = new SendMail();
		String host = "smtp.gmail.com";
		String user = "feedbackrapid@gmail.com";
		String pwd = "gkgkbzzbavwowfbh";
		send.setAddress(user, assessorEmail, subject);
		result = send.sendSimpleMail(host, user, pwd, msg);
		return result;
	}

}
