package messages;

import server.File;
import users.User;

public class SendFile extends Message {

	private String path;
	private User destUser;
	private File file;
	private int port;

	public SendFile(String origin, String destination, File file, User destUser, int port) {
		super(origin, destination);
		this.type = MessageType.SEND_FILE;
		this.port = port;
		this.destUser = destUser;
		this.file = file;
	}

	public int getPort() {
		return this.port;
	}
	
	public File getFile() {
		return this.file;
	}

	public User getDestUser() {
		return destUser;
	}

}
