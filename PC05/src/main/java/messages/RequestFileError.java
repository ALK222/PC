package messages;

public class RequestFileError extends Message {

	public RequestFileError(String origin, String destination, String id) {
		super(origin, destination, id);
		this.type = MessageType.REQUEST_FILE_ERROR;
	}

}
