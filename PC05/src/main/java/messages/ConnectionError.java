package messages;

public class ConnectionError extends Message {
	
	private String id;

	public ConnectionError(String origin, String destination, String id) {
		super(origin, destination);
		this.type = MessageType.CONNECTION_ERROR;
	}
	
	public String getId() {
		return this.id;
	}

}
