package messages;

public class DeleteFileConfirm extends Message {

	public DeleteFileConfirm(String origin, String destination) {
		super(origin, destination);
		this.type = MessageType.DELETE_FILE_CONFIRM;
	}

}
