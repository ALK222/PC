package messages;

public class PrepServerClient extends Message {

	private int port;
	private String ip;

	public PrepServerClient(String origin, String destination, String id, String ip, int port) {
		super(origin, destination, id);
		this.type = MessageType.PREPAIRING_SERVER_CLIENT;
		this.port = port;
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public String getIp() {
		return ip;
	}

}
