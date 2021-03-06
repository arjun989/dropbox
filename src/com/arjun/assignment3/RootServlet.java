package com.arjun.assignment3;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class RootServlet extends HttpServlet {
	String parent,path="",current="";
	ArrayList<BlobKey> files=new ArrayList<>();
	ArrayList<String> directories=new ArrayList<>();
	Stack <String> Path=new Stack<String>();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		// set the response type to be html text
		resp.setContentType("text/html");
		// get a request dispatcher and launch a jsp that will render our page

		// we need to get access to the google user service
		UserService us = UserServiceFactory.getUserService(); 
		com.google.appengine.api.users.User u = us.getCurrentUser();
		String login_url = us.createLoginURL("/");
		String logout_url = us.createLogoutURL("/");

		// attach a few things to the request such that we can access them in the
		// jsp
		req.setAttribute("user", u);
		req.setAttribute("login_url", login_url);
		req.setAttribute("logout_url", logout_url);

		String uid;
		PersistenceManager pm= PMF.get().getPersistenceManager();
		String p = (String) req.getSession().getAttribute("parent");
		String refresh = (String) req.getSession().getAttribute("refresh");
		if (p==null || p.equals(""))
		{
        	parent="";
        	req.getSession().setAttribute("parent","");
		}
        else
        	parent=p;
        		
		if (u != null){
			try {
				uid = u.getUserId();
				//displaying current data
				Key user_key = KeyFactory.createKey("User", uid);
				fetch(user_key);
				req.setAttribute("dir", directories);
				req.setAttribute("files", files);
	
			}
			catch(Exception e){
				//req.setAttribute("appointments", null);
				// This user doesnt exist yet, so add it.
				uid = u.getUserId();
				Key user_key = KeyFactory.createKey("User", uid);
				com.arjun.assignment3.User user = new com.arjun.assignment3.User(user_key);
				pm.makePersistent(user);
				pm.close();
				//now fetch
				try{
					fetch(user_key);
					req.setAttribute("dir", directories);
					req.setAttribute("files", files);
				}catch (Exception ex){
					displayAlert("Error Loading Data !","/",resp.getWriter());
				}
				finally{
					pm.close();
				}
				
				current=(String) req.getSession().getAttribute("current");
				if( refresh==null || refresh.equals("true")){
					if (current==null || current.equals("")){
						current="/";
						Path.push(current);
					}
				    else
						Path.push(current);
					
					Object [] temp=Path.toArray();
					for (int i=0;i<temp.length;i++)
						path+=temp[i].toString()+"/";	
					req.getSession().setAttribute("refresh","false");
				}
			}
		}

		//use session here
		req.setAttribute("path", path);
		req.setAttribute("dir", directories);
		req.setAttribute("files", files);
		req.setAttribute("addDir", "/addDir");
		RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/root.jsp");
		rd.forward(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// we need to get access to the google user service
		UserService us = UserServiceFactory.getUserService();
		com.google.appengine.api.users.User u = us.getCurrentUser();
		if(u==null ||  req.getParameter("signout")!=null){
			HttpSession session = req.getSession();
			if (session != null) {
			    session.invalidate();
			}
			resp.sendRedirect(us.createLogoutURL("/"));
			return;	
		}
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Key user_key = KeyFactory.createKey("User", u.getUserId());
		com.arjun.assignment3.User user =pm.getObjectById(com.arjun.assignment3.User.class, user_key);
		
		for (int i=0;i<directories.size();i++){

			if(req.getParameter((i+"ent"))!=null){
				//enter in this dir
				current=directories.get(i);
				req.getSession().setAttribute("parent", current);
				req.getSession().setAttribute("current", current);
				req.getSession().setAttribute("current", current);
				req.getSession().setAttribute("refresh","true");
				resp.sendRedirect("/");
				break;
			}
		}
	}
	
	private void displayAlert(String msg, String path, PrintWriter out) {
		out.println("<script type=\"text/javascript\">");
		out.println("alert('" + msg + "');");
		out.println("location=" + path + ";");
		out.println("</script>");
	}
	
	public void fetch (Key user_key) throws Exception{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			
			com.arjun.assignment3.User user;
			// print out what was stored for each user
			user = pm.getObjectById(com.arjun.assignment3.User.class, user_key);
			files.clear();
			directories.clear();
			for (directory ctemp : user.getDirectoriesWithParent(parent)) {
				directories.add(ctemp.getName());
			}
			directory d=user.getDir(parent);
			if (d!=null){
				for (file ctemp : d.getFiles()) {
					files.add(ctemp.getBlob());
				}
			}
		}catch(Exception e){
			throw e;
		}
		finally{
			pm.close();
		}
	}
}


