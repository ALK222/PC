package messages;

public class SendFile extends Message {

	private String path;
	private String destUser;
	private String fileName;

	public SendFile(String origin, String destination, String filename, String path, String destUser) {
		super(origin, destination);
		this.type = MessageType.SEND_FILE;
		this.path = path;
		this.destUser = destUser;
		this.fileName = filename;
	}

	public String getPath() {
		return path;
	}
	
	public String getFileName() {
		return this.fileName;
	}

	public String getDestUser() {
		return destUser;
	}

}
