package messages;

public class ConnectionConfirm extends Message {

	private static final long serialVersionUID = 6251748910239088424L;

	public ConnectionConfirm(String origin, String destination) {
		super(origin, destination);
		this.type = MessageType.CONNECTION_CONFIRM;
	}

}
