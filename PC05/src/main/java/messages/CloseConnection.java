package messages;

import users.User;

public class CloseConnection extends Message {
	
	private static final long serialVersionUID = -6542922052859806280L;
	private User u;

	public CloseConnection(String origin, String destination, User u) {
		super(origin, destination);
		this.type = MessageType.CLOSE_CONNECTION;
		this.u = u;
	}
	
	public User getUser() {
		return u;
	}

}
