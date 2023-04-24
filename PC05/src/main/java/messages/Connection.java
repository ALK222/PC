package messages;

import users.User;

public class Connection extends Message {
	private User u;

	public Connection( User u) {
		this.type = MessageType.CONNECTION;
		this.u = u;
	}

	public User getUser() {
		return this.u;
	}
}
