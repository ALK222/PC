package messages;

public class AddFileConfirmation extends Message {

	public AddFileConfirmation(String origin, String destination) {
		super(origin, destination);
		this.type = MessageType.ADD_FILE_CONFIRM;
	}

}
