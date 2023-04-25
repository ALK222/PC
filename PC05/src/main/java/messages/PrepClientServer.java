package messages;

import users.User;

public class PrepClientServer extends Message {

	private User userDest;
	private String ip;
	private int port;

	public PrepClientServer(String origin, String destination, String ip, int port, User user) {
		super(ip, destination);
		this.type = MessageType.PREPAIRING_CLIENT_SERVER;

		this.userDest = user;
		this.ip = ip;
		this.port = port;
	}

	public User getUserDest() {
		return userDest;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

}
