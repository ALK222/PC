package messages;

public class UserList extends Message {

	private static final long serialVersionUID = 6015513564018371187L;

	public UserList(String origin, String destination) {
		super(origin, destination);
		this.type = MessageType.USER_LIST;
	}

}
