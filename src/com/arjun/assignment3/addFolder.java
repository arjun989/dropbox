package com.arjun.assignment3;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
@SuppressWarnings("serial")
public class addFolder extends HttpServlet{

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// we will be outputting html to the client
		resp.setContentType("text/html");
		// get access to the user service to get our user
		UserService us = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User u = us.getCurrentUser();
		String logout_url = us.createLogoutURL("/");


		// get the value of the attribute input from the session and forward it to the // JSP
		if (u != null ) {
			req.setAttribute("loggedin", 1);
		} else {
			req.setAttribute("loggedin", null);
		}
		String login_url = us.createLoginURL("/");
		req.setAttribute("user", u);
		req.setAttribute("login_url", login_url);
		req.setAttribute("logout_url", logout_url);

		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/add.jsp");
		rd.forward(req, resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		// get access to the user service to get our user
		UserService us = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User u = us.getCurrentUser();
		String parent = (String) req.getSession().getAttribute("parent");
        if (parent==null)
        {
        	RequestDispatcher rd = req.getRequestDispatcher("/");
    		rd.forward(req, resp);
        }
		String n = req.getParameter("name").trim();
		if (n.equals("")) {
			displayAlert("Invalid Input !", "'/addDir'", out);
			return;
		}
		String uid = u.getUserId();
		PersistenceManager pm = null;
		Key user_key = KeyFactory.createKey("User", uid);
		com.arjun.assignment3.User user;
		directory ap = new directory();
		ap.setName(n);
		ap.setParent(parent);

		try {
			// Check if the KEY exists...
			pm = PMF.get().getPersistenceManager();
			user = pm.getObjectById(com.arjun.assignment3.User.class, user_key);
			ap.setUser(user);
			user.addDirectory(ap);
			pm.makePersistent(ap);
			pm.makePersistent(user);
			displayAlert("Directory Added !", "'/'", out);

		} catch (Exception e) {
			displayAlert("Error Occured !", "'/'", out);
		} finally {
			pm.close();
		}
	}

	private void displayAlert(String msg, String path, PrintWriter out) {
		out.println("<script type=\"text/javascript\">");
		out.println("alert('" + msg + "');");
		out.println("location=" + path + ";");
		out.println("</script>");
	}
}
