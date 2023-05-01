package messages;

public class DeleteFileConfirm extends Message {

	private static final long serialVersionUID = 768115266154339190L;

	public DeleteFileConfirm(String origin, String destination) {
		super(origin, destination);
		this.type = MessageType.DELETE_FILE_CONFIRM;
	}

}
