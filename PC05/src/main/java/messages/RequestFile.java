package messages;

import users.User;

public class RequestFile extends Message {

	private String fileName;
	private User u;

	public RequestFile(String origin, String destination, String fileName, User u) {
		super(origin, destination);
		this.type = MessageType.REQUEST_FILE;
		this.fileName = fileName;
		this.u = u;
	}

	public String getFileName() {
		return fileName;
	}
	
	public User getUser() {
		return this.u;
	}
	
	
}
