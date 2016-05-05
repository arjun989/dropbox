package com.arjun.assignment3;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;


import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class UpAndDownServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// get the blob collection from the datastore and add in the blob key // that was just added by the user.
		UserService us = UserServiceFactory.getUserService(); 
		com.google.appengine.api.users.User u = us.getCurrentUser();
		if (u!=null){
			// check to see if we have a blob collection in the datastore if not then create
			// one otherwise retrieve it
			Key k = KeyFactory.createKey("BlobCollection", u.getUserId());

			PersistenceManager pm = PMF.get().getPersistenceManager();
			BlobCollection bc = pm.getObjectById(BlobCollection.class, k);
			Map<String, List<BlobKey>> files_sent =
					BlobstoreServiceFactory.getBlobstoreService().getUploads(req); 
			BlobKey b = files_sent.get("file").get(0);
			bc.addKey(b);
			pm.makePersistent(bc);
			pm.close();
			resp.sendRedirect("/");
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// we have a key attached to this request which is the file that the // user wants served to them. get the key and serve the file PersistenceManager pm = PMF.get().getPersistenceManager();
		UserService us = UserServiceFactory.getUserService(); 
		com.google.appengine.api.users.User u = us.getCurrentUser();
		if (u!=null){
			// check to see if we have a blob collection in the datastore if not then create
			// one otherwise retrieve it
			Key k = KeyFactory.createKey("BlobCollection", u.getUserId());

			PersistenceManager pm = PMF.get().getPersistenceManager();
			BlobCollection bc = pm.getObjectById(BlobCollection.class, k); 
			int index = Integer.parseInt(req.getParameter("key_value")); 
			BlobKey bk = bc.blobKeys().get(index); 
			BlobstoreServiceFactory.getBlobstoreService().serve(bk, resp); 
			pm.close();
		}
	}

}
