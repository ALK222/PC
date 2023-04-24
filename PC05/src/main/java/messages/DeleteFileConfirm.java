package messages;

public class DeleteFileConfirm extends Message {

	public DeleteFileConfirm(String origin, String destination, String id) {
		super(origin, destination, id);
		this.type = MessageType.DELETE_FILE_CONFIRM;
	}

}
