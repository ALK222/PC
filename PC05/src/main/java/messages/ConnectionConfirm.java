package messages;

public class ConnectionConfirm extends Message {

	public ConnectionConfirm(String origin, String destination) {
		super(origin, destination);
		this.type = MessageType.CONNECTION_CONFIRM;
	}

}
