package messages;

public class CloseConnectionConfirm extends Message {

	private static final long serialVersionUID = 7073036180378460296L;

	public CloseConnectionConfirm(String origin, String destination) {
		super(origin, destination);
		this.type = MessageType.CLOSE_CONNECTION_CONFIRM;
	}

}
