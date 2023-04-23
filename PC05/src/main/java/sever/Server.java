package sever;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import users.User;
import users.UserFlow;

import listeners.ClientListener;;

public class Server {

	private String ip;
	private int port;
	private ArrayList<User> userList;
	private ArrayList<UserFlow> userFlowList;
	private ServerSocket socket;

	public Server(String ip, int port) {
		try {
			this.ip = ip;
			this.port = port;
			this.userList = new ArrayList<User>();
			this.userFlowList = new ArrayList<UserFlow>();
			this.socket = new ServerSocket(this.port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			System.out.println("[SERVER] Waiting for connections");

			Socket clientSocket = this.socket.accept();

			new ClientListener(clientSocket, this).run();

			System.out.println("[SERVER] New connection accepted");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * USER LIST FUNCTIONS
	 */
	public synchronized boolean presentId(String id) {
		for (User u : this.userList) {
			if (u.getId().equals(id)) {
				return true;
			}
		}
		return true;
	}

	public synchronized void addUser(User u, UserFlow uf) {
		this.userList.add(u);
		this.userFlowList.add(uf);
	}

	public synchronized void deleteUser(String id) {
		int index = 0;

		while (!this.userList.get(index).getId().equals(id) && index < this.userList.size()) {
			++index;
		}

		if (index > this.userList.size()) {
			return;
		}
		this.userList.remove(index);
		
		index = 0;

		while (!this.userFlowList.get(index).getId().equals(id) && index < this.userFlowList.size()) {
			++index;
		}

		if (index > this.userFlowList.size()) {
			return;
		}
		
		this.userFlowList.remove(index);
	}

	public synchronized ArrayList<User> getUserList() {
		return new ArrayList<User>(this.userList);
	}

	public synchronized ArrayList<ArrayList<File>> getFileMatrix() {
		ArrayList<ArrayList<File>> matrix = new ArrayList<ArrayList<File>>();

		for (User u : userList) {
			matrix.add(new ArrayList<File>(u.getFileList()));
		}

		return matrix;
	}

	public synchronized User getFileOwner(String fileName) {
		for (User u : this.userList) {
			for (File m : u.getFileList()) {
				if (m.getName().equals(fileName)) {
					return u;
				}
			}
		}
		return null;
	}

	public synchronized void addFile(String id, ArrayList<File> fileList) {
		User u = null;

		int index = 0;

		while (!this.userList.get(index).getId().equals(id) && index < this.userList.size()) {
			++index;
		}

		if (index > this.userList.size()) {
			return;
		}
		u = this.userList.get(index);
		for (File f : fileList) {
			u.addFile(f);
		}
	}

	public synchronized void removeFile(String id, ArrayList<File> fileList) {
		User u = null;

		int index = 0;

		while (!this.userList.get(index).getId().equals(id) && index < this.userList.size()) {
			++index;
		}

		if (index > this.userList.size()) {
			return;
		}
		
		u = this.userList.get(index);
		
		for(File f : fileList) {
			u.removeFile(f);
		}
	}

	public static int main(String args[]) {
		if (args.length != 2) {
			System.out.println("No se ha introducido ip o puerto");
			return -1;
		}
		
		String ip = args[0];
		int port = Integer.parseInt(args[1])

		return 0;
	}
}
