package messages;

public class ConnectionConfirm extends Message {

	public ConnectionConfirm(String origin, String destination, String id) {
		super(origin, destination, id);
		this.type = MessageType.CONNECTION_CONFIRM;
	}

}
