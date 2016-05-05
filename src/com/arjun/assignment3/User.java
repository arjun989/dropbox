package com.arjun.assignment3;
import java.util.ArrayList;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(detachable = "true") 
public class User {
	
	@PrimaryKey
	@Persistent
	private Key id;
    @Persistent(mappedBy="user")
	private ArrayList<directory> directories=new ArrayList<>();
	
	public User(Key id){
		//adding root
		directory d=new directory();
		d.setName("root");
		d.setParent("");
		directories=new ArrayList<directory>();
		directories.add(d);
		this.id=id;
	}
	public Key getId() {
		return id;
	}
	public void setId(Key id) {
		this.id = id;
	}
	public ArrayList<directory> getAppointments() {
		return directories;
	}
    
	public void addDirectory(directory ap){
		if (directories==null)	
			directories=new ArrayList<directory>();
    	directories.add(ap);
    }
	
	public ArrayList<directory> getDirectoriesWithParent (String n){
		ArrayList<directory> res=new ArrayList<directory>();
		for (int i=0;i<directories.size();i++){
			if (directories.get(i).getParent().equals(n))
				res.add(directories.get(i));
		}
		return res;
	}
	
	public directory getDir (String s){
		for (int i=0;i<directories.size();i++){
			if (directories.get(i).getName().equals(s)){
				return directories.get(i);
			}
		}
		return null;
	}
	public ArrayList<directory> getDirectories() {
		return directories;
	}
	public void setDirectories(ArrayList<directory> directories) {
		this.directories = directories;
	}

}
