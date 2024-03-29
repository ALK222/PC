package messages;

import users.User;

public class PrepServerClient extends Message {

	private static final long serialVersionUID = 3833481782337583560L;
	private int port;
	private User u;

	public PrepServerClient(String origin, String destination, User u, int port) {
		super(origin, destination);
		this.type = MessageType.PREPAIRING_SERVER_CLIENT;
		this.port = port;
		this.u = u;
	}

	public int getPort() {
		return port;
	}

	public User getUser() {
		return u;
	}

}
