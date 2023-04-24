package messages;

public abstract class Message {

	protected MessageType type;


	public Message() {
	}

	public MessageType getType() {
		return this.type;
	}

}
