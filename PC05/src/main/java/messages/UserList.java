package messages;

public class UserList extends Message {

	public UserList(String origin, String destination) {
		super(origin, destination);
		this.type = MessageType.USER_LIST;
	}

}
