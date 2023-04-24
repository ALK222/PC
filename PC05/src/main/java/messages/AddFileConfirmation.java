package messages;

public class AddFileConfirmation extends Message {

	public AddFileConfirmation(String origin, String destination, String id) {
		super(origin, destination, id);
		this.type = MessageType.ADD_FILE_CONFIRM;
	}

}
