package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.RapidFeedback.MysqlFunction;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName RegisterServlet
 * @Description servlet for the registration.
 *
 * @author Jingxian Hu, Zhongke Tan, Meng Ru
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
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

		JSONObject jsonReceive;
		BufferedReader reader = request.getReader();
		String str, wholeString = "";
		while ((str = reader.readLine()) != null) {
			wholeString += str;
		}
		System.out.println("Receive: " + wholeString);
		jsonReceive = JSON.parseObject(wholeString);
		String email = jsonReceive.getString("email");
		String password = jsonReceive.getString("password");
		String firstName = jsonReceive.getString("firstName");
		String middleName = jsonReceive.getString("middleName");
		String lastName = jsonReceive.getString("lastName");
		boolean register_ACK = false;
		int id;
		try {
			id = register(dbFunction, email, password, firstName,
					middleName, lastName);
			if (id>0){
				register_ACK = true;
			}
			
			// construct JSON object  
			JSONObject jsonSend = new JSONObject();
			jsonSend.put("register_ACK", register_ACK);
			jsonSend.put("id", id);
			
			// send
			PrintWriter output = response.getWriter();
			output.print(jsonSend.toJSONString());
			System.out.println("Send: " + jsonSend.toJSONString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Function register
	 * @Description call SQL method to complete registration.
	 *
	 * @param db
	 * @param email
	 * @param password
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @return registration result
	 * @throws SQLException
	 */
	private int register(MysqlFunction db, String email, String password,
			String firstName, String middleName, String lastName)
			throws SQLException {
		int checkResult = db.addMarker(email, password, firstName,
				   middleName, lastName);
		return checkResult;
		}
		
}
