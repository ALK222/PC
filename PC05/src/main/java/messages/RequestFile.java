package messages;

public class RequestFile extends Message {

	private String fileName;

	public RequestFile(String origin, String destination, String fileName) {
		super(origin, destination);
		this.type = MessageType.REQUEST_FILE;
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
}
