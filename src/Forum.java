import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Forum
 */
@WebServlet("/Forum")
public class Forum extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String url = config.url;
    private static final String user = config.user;
    private static final String password = config.password;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Forum() {
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
			PrintWriter out = response.getWriter();
			out.println("<!DOCTYPE html>\n" + 
					"<html>\n" + 
					"<head>\n" + 
					"<meta charset=\"UTF-8\">\n" + 
					"<script type=\"text/javascript\" src=\"jquery-3.3.1.js\"></script>"+
					"<title>Forum</title>\n" + 
					"<style>body {text-align: center;opacity: .85;}"+
					"html {\n" + 
					"    height: 100%;\n" + 
					"    margin: 0;\n" + 
					"    background-image: url(\"joker.jpg\");\n" + 
					"    background-position: center;\n" + 
					"    background-repeat: no-repeat;\n" + 
					"    background-size: cover;\n" + 
					"}"+
					".child{\n" + 
					"	padding: 5px;\n" + 
					"	display: flex;\n" + 
					"	height: auto;\n" + 
					"}"+
					"p{padding: 10px;}"+
					"#right {\n" + 
					" text-align: left;"+
					"  width: 95%;\n" + 
					"  background: #c0c0c0;\n" + 
					"  border-radius: 10px;border-left: 5px solid black;\n" + 
					"}\n" + 
					"#left {\n" + 
					"  flex: 1;\n" + 
					"  width: 10%;\n" + 
					"  height: 60px;\n" + 
					"  background: white;\n" + 
					"  border-radius: 25%;\n" + 
					"}\n" + 
					".votesymbol{\n" + 
					"	text-align: center;\n" + 
					"	height: 50%;\n" + 
					"}\n" + 
					".votecount{\n" + 
					"	margin: 0 auto;\n" + 
					"	width: 50%;\n" + 
					"	height: 50%;\n" + 
					"}\n" + 
					".vote {\n" + 
					"  display: inline-block;\n" + 
					"  overflow: hidden;\n" + 
					"  width: 40px;\n" + 
					"  height: 25px;\n" + 
					"  cursor: pointer;\n" + 
					"  background: url('upvote.png');\n" + 
					"  background-position: 0 -100%;\n" + 
					"} \n" + 
					"input[type=text] {\n" + 
					"    width: 70%;\n" + 
					"    padding: 10px 5px;\n" + 
					"    box-sizing: border-box;\n" + 
					"    border: 2px solid skyblue;\n" + 
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
					"    background-color: white; \n" + 
					"    color: blue; \n" + 
					"    border: none;\n" + 
					"}\n" + 
					"input[type=submit]:hover {\n" + 
					"    background-color: #00800A;\n" + 
					"    color: white;\n" + 
					"}" + 
					"a{\n" + 
					"    text-decoration: none;\n" + 
					"}"+"</style>"+
					"</head>\n");
			showposts(out, userid);
		}
	}
	private static void showposts(PrintWriter out, String userid) {
		try(Connection conn = DriverManager.getConnection(url, user, password)){
			conn.setAutoCommit(false);
			try(
					PreparedStatement stmt = conn.prepareStatement("select text, case when xvote.post_id is null then 0 else count(*) end as vote, xposts.post_id, (select name from xusers where uid=xposts.uid) from xposts left outer join xvote on xposts.post_id=xvote.post_id group by xvote.post_id,xposts.post_id order by vote desc;");
					PreparedStatement stmt1 = conn.prepareStatement("select name from xusers where uid=?");
					){
				stmt1.setString(1, userid);
				ResultSet rs_name = stmt1.executeQuery();
				rs_name.next();
				out.println("<form action=\"Newpost\" method=\"post\">\n" + 
						"<span style=\"color:#20C20E;\"><b><i>"+rs_name.getString(1)+"</i></b></span>&emsp;" +
						"<input type=\"text\" placeholder=\"Ask a question .....\" name=\"message\">\n" +
						"<input type=\"submit\" value=\"ASK\">" +
						"</form>");
				out.println("<br><form action=\"Logout\"><input type=\"submit\" value=\"Logout\"></form><br>");
				ResultSet rs = stmt.executeQuery();
				out.println("<div id=\"parent\">");
				while(rs.next()) {
					out.println("<div class=\"child\"><div id=\"left\">\n" + 
							"<div class=\"votesymbol\">\n" + 
							"<span class=\"vote\" id="+rs.getInt(3)+"></span>\n" + 
							"</div>\n" + 
							"<div class=\"votecount\">"+rs.getInt(2)+"</div>\n" + 
							"</div>");
					out.println("<div id=\"right\">\n" + 
							"<p>[<b><i>"+rs.getString(4)+"</i></b>]   "+rs.getString(1)+"</p>\n" + 
							"</div></div>");
				}
				out.println("</div>");
				out.println("\n" +
						"<script type=\"text/javascript\">"+
						"    $(document).ready(function() {\n" + 
						"        $(\"span\").on(\"click\", function(event) {\n" +
						"			var x = $(this).closest('div').siblings('.votecount');"+
						"			var num = parseInt(x.text());\n"+
						"			" +
						"			$.ajax({\n" + 
						"    			type: 'POST',\n" + 
						"    			url: 'Upvote',\n" + 
						"    			data: { \n" + 
						"        			'postid': $(this).attr('id'), \n" + 
						"    			},"+
						"				success: function(e){if(e==1){x.text(num+1);}}" + 
						"			});" +
						"    	});"+
						"	});\n" + 
						"</script>");
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
