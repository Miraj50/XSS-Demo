import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.Servlet;
import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String url = config.url;
    private static final String user = config.user;
    private static final String password = config.password;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if(session!=null) {
			response.sendRedirect("Home");
		}
		PrintWriter out = response.getWriter(); 
		viewlogin(out, 1);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session1 = request.getSession(false);
		if(session1!=null) {
			response.sendRedirect("Forum");
		}
		
		String id = request.getParameter("uid");
		String passwd = request.getParameter("pass");
		
		PrintWriter out = response.getWriter();
		
		if(id=="" || passwd=="") {
			viewlogin(out, 1);
		}
		else {
			int auth = authenticate(id, passwd);
			if(auth == 1) {
				HttpSession session = request.getSession();
				session.setAttribute("id", id);
				response.sendRedirect("Forum");
			}
			else {
				viewlogin(out,0);
			}
		}
	}
	private static void viewlogin(PrintWriter out, int flag) {
		out.println("<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<meta charset=\"UTF-8\">\n" + 
				"<title>Login</title>\n" + 
				"<style>"+
				"html {\n" + 
				"    height: 100%;\n" + 
				"    margin: 0;\n" + 
				"    background-image: url(\"batman.jpg\");\n" + 
				"    background-position: center;\n" + 
				"    background-repeat: no-repeat;\n" + 
				"    background-size: cover;\n" + 
				"}"+
				".welcome{\n" + 
				"	position: absolute;\n" + 
				"	text-align: center;\n" + 
				"	left: 10%;\n" + 
				"	top: 25%;\n" + 
				"}"+
				"input[type=submit] {\n" + 
				"    padding: 2px 4px;\n" + 
				"    text-align: center;\n" + 
				"    text-decoration: none;\n" + 
				"    display: inline-block;\n" + 
				"    font-size: 16px;\n" + 
				"    margin: 2px 1px;\n" + 
				"    transition-duration: 0.4s;\n" + 
				"    cursor: pointer;\n" + 
				"    background-color: green; \n" + 
				"    color: white; \n" +  
				"	 border: 0;"+
				"}\n" + 
				"input[type=submit]:hover {\n" + 
				"    background-color: black;\n" + 
				"    color: white;\n" + 
				"}\n" + 
				"</style>"+
				"</head>\n" + 
				"<body>\n" + 
				"<div class=\"welcome\"><h1>Login</h1>" +
				"<form action=\"Login\" method=\"post\">\n" + 
				"<input type=\"text\" name = \"uid\" placeholder=\"Username...\">\n<br><br>" + 
				"<input type=\"password\" name=\"pass\" placeholder=\"Password...\">\n<br>");
		if(flag==0) {
			out.println("<p style=\"color:green;\">Incorrect Username or Password!</p>");
		}
		out.println("<br><input type=\"submit\" value=\"Submit\">\n" + 
					"</form></div>\n" + 
					"</body>\n" + 
					"</html>");
	}
	private static int authenticate(String uid, String passwd) {
		try(Connection conn = DriverManager.getConnection(url, user, password)){
			conn.setAutoCommit(false);
			try(
					PreparedStatement stmt = conn.prepareStatement("select id, password from password where id=? and password=?");
				){
				stmt.setString(1, uid);
				stmt.setString(2, passwd);
				ResultSet rs = stmt.executeQuery();
				if (!rs.isBeforeFirst() ) {    
				    return 0; 
				}
				else {
					return 1;
				}
			}
			catch(Exception ex) {
				conn.rollback();
				throw ex;
			}
			finally {
				conn.setAutoCommit(true);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return 5;
	}

}
