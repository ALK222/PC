package messages;

public class AddFileConfirmation extends Message {

	private static final long serialVersionUID = -3379842496196550322L;

	public AddFileConfirmation(String origin, String destination) {
		super(origin, destination);
		this.type = MessageType.ADD_FILE_CONFIRM;
	}

}
