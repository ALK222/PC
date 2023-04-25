package messages;

import users.User;

public class SendFile extends Message {

	private String path;
	private User destUser;
	private String fileName;
	private int port;

	public SendFile(String origin, String destination, String filename, User destUser, int port) {
		super(origin, destination);
		this.type = MessageType.SEND_FILE;
		this.port = port;
		this.destUser = destUser;
		this.fileName = filename;
	}

	public int getPort() {
		return this.port;
	}
	
	public String getFileName() {
		return this.fileName;
	}

	public User getDestUser() {
		return destUser;
	}

}
