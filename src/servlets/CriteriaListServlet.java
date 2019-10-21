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

import com.RapidFeedback.InsideFunction;
import com.RapidFeedback.MysqlFunction;
import com.RapidFeedback.Criterion;
import com.RapidFeedback.ExpandedComment;
import com.RapidFeedback.Field;
import com.google.gson.*;

/**
 * @ClassName CriteriaListServlet
 * @Description This servlet inserts or updates two criteriaList
 *              (markedCriteriaList and commentCriteriaList) to the DB. Because
 *              the server cannot verify the create and update, the server will
 *              delete all the old criteria before adding criteria lists.
 *
 * @author 
 */
@WebServlet("/CriteriaListServlet")
public class CriteriaListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CriteriaListServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
		String CriteronListString = jsonReceive.getString("criterionList");

		List<Criterion> criterionList = new Gson().fromJson(criterionListString, new TypeToken<List<Criterion>>(){}.getType());
		ArrayList<Criterion> criterionArrayList = new ArrayList();
		criterionArrayList .addAll(criterionList);

		ServletContext servletContext = this.getServletContext();

		boolean update_ACK;
		// Mention:
		// call the SQL method to save two criteriaList: markedCriteriaList and
		// commentCriteriaList
		// return the 'true' or 'false' value to update_ACK
		update_ACK = false;
		boolean deleted = false;
		deleted = dbFunction.deleteCriteria(projectId);
		if (deleted) {
			update_ACK = dbFunction.addCriteria(criterionList, projectId);
		}

		// construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("updateProject_ACK", update_ACK);

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
	}

	// /**
	//  * @Function addCriteriaList
	//  * @Description Insert a criteriaList into DB.
	//  *
	//  * @param dbFunction
	//  * @param servletContext
	//  * @param token
	//  * @param projectName
	//  * @param clist          criteriaList
	//  * @param isCommentOnly
	//  * @param delOldCriteria
	//  * @return the result of adding a list of criteria into DB.
	//  */
	// private boolean addCriteriaList(MysqlFunction dbFunction,
	// 		ServletContext servletContext, String token, String projectName,
	// 		ArrayList<Criteria> clist, boolean isCommentOnly,
	// 		boolean delOldCriteria) {
	// 	boolean result = false;
	// 	if (clist.size() == 0 || clist == null) {
	// 		result = true;
	// 		return result;
	// 	}
	// 	InsideFunction inside = new InsideFunction(dbFunction);
	// 	String username = inside.token2user(servletContext, token);
	// 	try {
	// 		int pid = dbFunction.getProjectId(username, projectName);
	// 		if (pid <= 0) {
	// 			throw new Exception(
	// 					"Exception: Cannot find the project, or the user "
	// 							+ "is not the primary assessor of the project.");
	// 		}
	// 		// before adding, delete all the old criteriaList of the project.
	// 		if (delOldCriteria) {
	// 			boolean delete = dbFunction.deleteCriterias(pid);
	// 			if (!delete) {
	// 				return result;
	// 			}
	// 		}
	// 		// using different db functions to store different kinds of criteria
	// 		// lists.
	// 		if (isCommentOnly) {
	// 			// add criteriaList without numerical mark.
	// 			for (Criteria c : clist) {
	// 				result = dbFunction.addOnlyComment(pid, c) > 0 ? true
	// 						: false;
	// 				if (!result) {
	// 					break;
	// 				}
	// 			}
	// 		} else {
	// 			// add criteriaList with numerical mark.
	// 			for (Criteria c : clist) {
	// 				result = dbFunction.addCriteria(pid, c) > 0 ? true : false;
	// 				if (!result) {
	// 					break;
	// 				}
	// 			}
	// 		}

	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 	}
	// 	return result;
	// }

}