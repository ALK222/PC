package messages;

public class ConnectionError extends Message {
	
	private String id;

	public ConnectionError(String id) {
		this.type = MessageType.CONNECTION_ERROR;
	}
	
	public String getId() {
		return this.id;
	}

}
