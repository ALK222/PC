package users;

import java.io.Serializable;
import java.util.ArrayList;

import server.File;

public class User implements Serializable {

	private static final long serialVersionUID = 1148386234085335627L;

	private String id;
	private String ip;
	private ArrayList<File> fileList;

	public User(String id, String ip) {
		super();
		this.id = id;
		this.ip = ip;
		this.fileList = new ArrayList<File>();
	}

	public User(String id, String ip, ArrayList<File> listaMensajes) {
		super();
		this.id = id;
		this.ip = ip;
		this.fileList = new ArrayList<File>(listaMensajes);
	}

	public User(User usuario) {
		this.id = usuario.id;
		this.ip = usuario.ip;
		this.fileList = new ArrayList<File>(usuario.fileList);
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getIp() {
		return this.ip;
	}
	
	public ArrayList<File> getFileList() {
		return this.fileList;
	}

	public void addFile(File file) {
		this.fileList.add(new File(file));
	}

	public void removeFile(File file) {
		fileList.remove(file);
	}
	
	public boolean equals(Object o) {
		if(this.getClass() != o.getClass()) {
			return false;
		}
		
		if(this.id != ((User) o).getId()) {
			return false;
		}
		return true;
	}

}
