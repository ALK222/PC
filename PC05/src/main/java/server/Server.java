package server;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.HashMap;

import users.User;

import listeners.ClientListener;
import monitors.ReadersWritersController;;

public class Server extends Thread {

	private int port;
	private ArrayList<User> userList;
	private ServerSocket socket;
	private int nextPort;
	private Map<User, ObjectOutputStream> userStreams;
	private ReadersWritersController userStreamMapController;
	private ReadersWritersController userListController;
	private ReentrantLock nextPortLock;

	public Server(int port) {
		try {
			this.port = port;
			this.userList = new ArrayList<User>();
			this.userStreams = new HashMap<User, ObjectOutputStream>();
			this.socket = new ServerSocket(this.port);
			this.nextPort = 30000;
			userStreamMapController = new ReadersWritersController();
			userListController = new ReadersWritersController();

			nextPortLock = new ReentrantLock(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ObjectOutputStream getObjectOutputStream(User user) {

		if (!userStreamMapController.requestRead())
			return null;

		ObjectOutputStream ret = this.userStreams.get(user);

		userStreamMapController.releaseRead();
		return ret;

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
		if (!userListController.requestRead())
            return true;

        for (User u : userList)
            if (u.toString().equals(id)) {
                userListController.releaseRead();
                return true;
            }

        userListController.releaseRead();

        return false;
	}

	public synchronized boolean addUser(User u, ObjectOutputStream uf) {
		if (!userStreamMapController.requestWrite())
			return false;

		this.userStreams.put(u, uf);

		userStreamMapController.releaseWrite();

		return true;
	}

	public synchronized boolean deleteUser(User user) {
		if (userStreamMapController.requestWrite())
			return false;

		userStreams.remove(user);

		userStreamMapController.releaseWrite();

		// Eliminar de la lista
		if (!userListController.requestWrite())
			return false;

		userList.remove(user);

		userListController.releaseWrite();

		return true;
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

	public synchronized User getUser(User user) {
		if (!userListController.requestRead())
            return null;

        for (User u : userList)
            if (u.toString().equals(user.toString())) {
                userListController.releaseRead();
                return u;
            }

        userListController.releaseRead();

        return user;
	}

	public synchronized File getFileWithFilename(String fileName) {

		if (!userListController.requestRead())
			return null;

		for (User user : userList)
			for (File file : user.getFileList())
				if (file.hasFilename(fileName)) {
					userListController.releaseRead();
					return file;
				}

		userListController.releaseRead();

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

		for (File f : fileList) {
			u.removeFile(f);
		}
	}

	/*
	 * LOCK FUNCTION
	 */
	public int getAndIncrementNextPort() {

		nextPortLock.lock();

		int ret = nextPort;
		nextPort++;

		// Si se llega a 31000, volver a 30000
		if (nextPort == 31000)
			nextPort = 30000;

		nextPortLock.unlock();

		return ret;

	}

	public static int main(String args[]) {
		if (args.length != 1) {
			System.out.println("Port needed");
			return -1;
		}

		String ip = args[0];
		int port = Integer.parseInt(args[1]);

		Server server = new Server(port);

		server.start();

		try {
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
}