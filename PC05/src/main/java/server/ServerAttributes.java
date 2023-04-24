package server;

import java.util.HashMap;

import users.User;

public class ServerAttributes {
	
	private HashMap<String, User> userTable;
	private String ip;
	private int port;

	public ServerAttributes(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public HashMap<String, User> getUserTable(){
		return this.userTable;
	}
	
	public String getIp() {
		return this.ip;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public void addUser(User u) {
		// TODO
	}

}
