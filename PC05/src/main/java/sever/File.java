package sever;

import java.io.Serializable;

public class File implements Serializable{

	private static final long serialVersionUID = 7927011959779472303L;
	
	private String name;
	private String path;
	
	public File(String name, String path) {
		this.name = name;
		this.path = path;
	}
	
	public File(File f) {
		this.name = f.name;
		this.path = f.path;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPath() {
		return this.path;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
