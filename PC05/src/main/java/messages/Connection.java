package messages;

import users.User;

public class Connection extends Message {
	private User u;

	public Connection(String origin, String destination, User u) {
		super(origin, destination);
		this.type = MessageType.CONNECTION;
		this.u = u;
	}

	public User getUser() {
		return this.u;
	}
}
