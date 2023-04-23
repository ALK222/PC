package messages;

public abstract class Message {
	
	protected int type;
	
	protected String origin;
	protected String destination;
	protected String id;
	
	public Message(String origin, String destination, String id) {
		this.origin = origin;
		this.destination = destination;
		this.id = id;
	}
	
	public int getType() {
		return this.type;
	}
	
	public String getOrigin() {
		return this.origin;
	}
	
	public String getDestination() {
		return this.destination;
	}

}
