package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import listeners.ClientListener;
import monitors.RsWsController;
import users.User;;

public class Server {

	private int port;
	private ArrayList<User> userList;
	private ServerSocket socket;
	private int nextPort;
	private Map<User, ObjectOutputStream> userStreams;
	private RsWsController userStreamMapController;
	private RsWsController userListController;
	private ReentrantLock nextPortLock;

	public Server(int port) {
		try {
			this.port = port;
			this.userList = new ArrayList<User>();
			this.userStreams = new HashMap<User, ObjectOutputStream>();
			this.socket = new ServerSocket(this.port);
			this.nextPort = 30000;
			userStreamMapController = new RsWsController();
			userListController = new RsWsController();

			nextPortLock = new ReentrantLock(true);

			System.out.println("[SERVER] Server at port: " + this.port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ObjectOutputStream getObjectOutputStream(User user) {

		if (!userStreamMapController.requestRead()) {
			return null;
		}

		ObjectOutputStream ret = this.userStreams.get(user);

		userStreamMapController.releaseRead();
		return ret;

	}

	/*
	 * USER LIST FUNCTIONS
	 */
	public boolean presentId(String id) {
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

	// NO PONER SYNCHRONIZED
	public boolean addUser(User u, ObjectOutputStream uf) {
		if (!userStreamMapController.requestWrite()) {
			return false;
		}

		this.userStreams.put(u, uf);

		userStreamMapController.releaseWrite();

		if (!userListController.requestWrite()) {
			return false;
		}
		this.userList.add(u);

		userListController.releaseWrite();
		return true;
	}

	public boolean deleteUser(User user) {
		if (!userStreamMapController.requestWrite())
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

	public ArrayList<User> getUserList() {
		if (!this.userListController.requestRead()) {
			return null;
		}
		ArrayList<User> aux = new ArrayList<User>(this.userList);
		userListController.releaseRead();
		return aux;
	}

	public ArrayList<ArrayList<File>> getFileMatrix() {

		if (!this.userStreamMapController.requestRead()) {
			return null;
		}
		ArrayList<ArrayList<File>> matrix = new ArrayList<ArrayList<File>>();

		for (User u : userList) {
			matrix.add(new ArrayList<File>(u.getFileList()));
		}

		userStreamMapController.releaseRead();

		return matrix;
	}

	public User getUser(User user) {
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

	public File getFileWithFilename(String fileName) {

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

	public void addFile(String id, ArrayList<File> fileList) {
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

	public void removeFile(String id, ArrayList<File> fileList) {
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

	public boolean hasUser(String user) {
		if (!userListController.requestRead())
			return true;

		for (User u : userList)
			if (u.getId().equals(user)) {
				userListController.releaseRead();
				return true;
			}

		userListController.releaseRead();

		return false;
	}

	public ServerSocket getServerSocket() {
		return this.socket;
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

	public void loop() throws IOException {
		while (true) { // Server loop

			Socket auxSocket = null;

			try {
				auxSocket = this.getServerSocket().accept();

			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("[SERVER] Client connected from " + auxSocket.getInetAddress().toString().substring(1));

			OutputStream outStr = null;
			InputStream inStr = null;

			try {
				outStr = auxSocket.getOutputStream();
				inStr = auxSocket.getInputStream();
			} catch (IOException e) {
				System.err.println("[SERVER] ERROR: I/O error in socket");
			}

			(new ClientListener(this, inStr, outStr)).start(); // ClientListener thread
		}
	}

	public static void main(String args[]) throws IOException {
		if (args.length != 1) {
			System.out.println("[SERVER] Port needed");
		}

		int port = Integer.parseInt(args[0]);

		Server server = new Server(port);

		try {
			server.loop();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
