package messages;

public class CloseConnection extends Message {

	public CloseConnection(String origin, String destination) {
		super(origin, destination);
		this.type = MessageType.CLOSE_CONNECTION;
	}

}
