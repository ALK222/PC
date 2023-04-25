package messages;

public class RequestFileError extends Message {

	public RequestFileError(String origin, String destination) {
		super(origin, destination);
		this.type = MessageType.REQUEST_FILE_ERROR;
	}

}
