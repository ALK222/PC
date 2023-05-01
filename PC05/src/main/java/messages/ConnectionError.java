package messages;

public class ConnectionError extends Message {
	
	private static final long serialVersionUID = -365002182109637706L;
	private String id;

	public ConnectionError(String origin, String destination, String id) {
		super(origin, destination);
		this.type = MessageType.CONNECTION_ERROR;
	}
	
	public String getId() {
		return this.id;
	}

}
