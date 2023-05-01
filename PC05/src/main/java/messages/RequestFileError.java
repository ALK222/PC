package messages;

public class RequestFileError extends Message {

	private static final long serialVersionUID = 4562976781252036418L;

	public RequestFileError(String origin, String destination) {
		super(origin, destination);
		this.type = MessageType.REQUEST_FILE_ERROR;
	}

}
