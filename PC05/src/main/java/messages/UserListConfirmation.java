package messages;

import java.util.ArrayList;

import server.File;
import users.User;

public class UserListConfirmation extends Message {

	private ArrayList<User> userList;
	private ArrayList<ArrayList<File>> fileMatrix;

	public UserListConfirmation(String origin, String destination, ArrayList<User> userList,
			ArrayList<ArrayList<File>> fileMatrix) {
		super(origin, destination);
		this.type = MessageType.USER_LIST_CONFIRM;
		this.userList = userList;
		this.fileMatrix = fileMatrix;
	}

	public ArrayList<User> getUserList() {
		return this.userList;
	}

	public ArrayList<ArrayList<File>> getFileMatrix() {
		return this.fileMatrix;
	}
}
