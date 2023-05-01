package server;

import java.io.Serializable;

import users.User;

public class File implements Serializable{

	private static final long serialVersionUID = 7927011959779472303L;
	
	private final String name;
	private final String path;
	private final User user;
	
	public File(String name, String path, User user) {
		this.name = name;
		this.path = path;
		this.user = user;
	}
	
	
	public String getName() {
		return this.name;
	}
	
	public String getPath() {
		return this.path;
	}
	
	 public boolean hasFilename(String filename) {
	        return name.equals(filename);
	    }

	    public User getUser() {
	        return user;
	    }
	
	@Override
	public String toString() {
		return this.name;
	}
}
