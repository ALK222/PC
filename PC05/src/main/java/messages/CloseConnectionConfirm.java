package messages;

public class CloseConnectionConfirm extends Message {

	public CloseConnectionConfirm(String origin, String destination) {
		super(origin, destination);
		this.type = MessageType.CLOSE_CONNECTION_CONFIRM;
	}

}
