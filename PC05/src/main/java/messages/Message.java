package messages;

public abstract class Message {

	protected MessageType type;
	
	protected String destination;
	protected String origin;


	public Message(String origin, String destination) {
		this.destination = destination;
		this.origin = origin;
	}

	public MessageType getType() {
		return this.type;
	}
	
	public String getDestination() {
		return this.destination;
	}
	
	public String getOrigin() {
		return this.origin;
	}

}
