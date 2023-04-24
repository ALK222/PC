package messages;

public class UserList extends Message {

	public UserList(String origin, String destination, String id) {
		super(origin, destination, id);
		this.type = MessageType.USER_LIST;
	}

}
