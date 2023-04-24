package messages;

public class CloseConnection extends Message {

	public CloseConnection() {
		this.type = MessageType.CLOSE_CONNECTION;
	}

}
