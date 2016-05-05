//simple JDO object that holds a collection of blob keys
package com.arjun.assignment3;
//imports
import java.util.ArrayList; import java.util.List;
import javax.jdo.annotations.PersistenceCapable; import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.blobstore.BlobKey; import com.google.appengine.api.datastore.Key;
//class definition
@PersistenceCapable
public class BlobCollection {
	//the id of this BlobCollection
	@PrimaryKey
	@Persistent
	private Key id;
	// the list of keys that belong in this collection
	@Persistent
	private ArrayList<BlobKey> blobs;
	// setter and getter for the id
	public Key id() { return id; }
	public void setID(final Key id) { this.id = id; }
	// returns the list of blobkeys
	public ArrayList<BlobKey> blobKeys() { return blobs; }
	//adds a blob key into the list. will initialise the list // if it was not created yet
	public void addKey(final BlobKey b) {
		if(blobs == null)
			blobs = new ArrayList<BlobKey>();
		blobs.add(b);
	}
}