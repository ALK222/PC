package messages;

public class RequestFile extends Message {

	private String fileName;

	public RequestFile(String origin, String destination, String id, String fileName) {
		super(origin, destination, id);
		this.type = MessageType.REQUEST_FILE;
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
}
