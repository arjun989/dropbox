package com.arjun.assignment3;

import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(detachable = "true") 
public class directory {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	@Persistent
	String name;
	@Persistent
	String parent;
	@Persistent
	User user;
	@Persistent(mappedBy="dir")
	
	private ArrayList<file> files;
	
	public directory(){
		files=new ArrayList<file>();
	}
	
	public ArrayList<file> getFiles() {
		return files;
	}
	Key getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getParent() {
		return parent;
	}
	
	public void addFile(file f){
		files.add(f);
	
	}
	public void setId(Key id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public void setFiles(ArrayList<file> files) {
		this.files = files;
	}
	public void setUser(User user) {
		this.user = user;
	}

}
