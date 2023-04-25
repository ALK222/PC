package messages;

public class PrepClientServer extends Message {

	private String userDest;
	private String ip;
	private int port;
	private String fileName;

	public PrepClientServer(String origin, String destination, String ip, int port, String fileName) {
		super(ip, destination);
		this.type = MessageType.PREPAIRING_CLIENT_SERVER;

		this.userDest = origin;
		this.ip = ip;
		this.port = port;
		this.fileName = fileName;
	}

	public String getUserDest() {
		return userDest;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public String getFileName() {
		return fileName;
	}
}
