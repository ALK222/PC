package messages;

import users.User;

public class PrepClientServer extends Message {

	private static final long serialVersionUID = 2251606576579005871L;
	private User userDest;
	private int port;

	public PrepClientServer(String origin, String destination, int port, User user) {
		super(origin, destination);
		this.type = MessageType.PREPAIRING_CLIENT_SERVER;

		this.userDest = user;
		this.port = port;
	}

	public User getUserDest() {
		return userDest;
	}

	public int getPort() {
		return port;
	}

}
