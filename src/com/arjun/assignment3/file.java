package com.arjun.assignment3;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class file {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	@Persistent
	BlobKey blob;
	@Persistent
	directory dir;
	public Key getId() {
		return id;
	}
	public BlobKey getBlob() {
		return blob;
	}
}
