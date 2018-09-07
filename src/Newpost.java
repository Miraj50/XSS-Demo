import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Newpost
 */
@WebServlet("/Newpost")
public class Newpost extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String url = config.url;
    private static final String user = config.user;
    private static final String password = config.password;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Newpost() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession(false);
		if(session == null) {
			response.sendRedirect("Login");
		}
		else {
			String userid = (String)session.getAttribute("id");
			String post = (String)request.getParameter("message");
			try(Connection conn = DriverManager.getConnection(url, user, password)){
				conn.setAutoCommit(false);
				try(
						PreparedStatement stmt = conn.prepareStatement("insert into xposts (uid, text) values (?,?)");
						){
					stmt.setString(1, userid);
					stmt.setString(2, post);
					stmt.executeUpdate();
					response.sendRedirect("Forum");
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
		}
	}

}
