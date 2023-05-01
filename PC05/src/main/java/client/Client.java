package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.Enumeration;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;

import listeners.ServerListener;
import messages.AddFiles;
import messages.DeleteFile;
import messages.CloseConnection;
import messages.Connection;
import messages.Message;
import messages.RequestFile;
import messages.UserList;
import server.File;
import users.User;

public class Client {

	private User user;
	private Semaphore sem;
	private boolean terminate;
	private ObjectOutputStream objOutStr;
	private String serverIp;
	private Scanner in;

	public Client(User user, ObjectOutputStream objOutStr, String serverIp) {

		this.user = user;
		this.sem = new Semaphore(0);
		this.terminate = false;
		this.objOutStr = objOutStr;
		this.serverIp = serverIp;
		this.in = new Scanner(System.in);

		System.out.println("[CLIENT]: New Client created");
	}

	public User getUser() {
		return this.user;
	}

	public Semaphore getSem() {
		return this.sem;
	}

	public void terminate() {
		terminate = true;
	}

	public boolean isTerminated() {
		return this.terminate;
	}

	public void addFiles() throws IOException {
		System.out.println("[CLIENT] Files to share? (end with empty input)");

		System.out.print("[CLIENT] Full path to file: ");
		String filepath = in.nextLine();

		ArrayList<File> fileList = new ArrayList<File>();

		while (!filepath.isEmpty()) {

			Path path = Paths.get(filepath);

			File file = new File(filepath, path.getFileName().toString(), user);

			fileList.add(file);

			System.out.print("[CLIENT] Full path to file: ");
			filepath = in.nextLine();

		}

		if (fileList.isEmpty()) {
			return;
		}
		AddFiles maf = new AddFiles(user.getIp(), serverIp, fileList);

		objOutStr.writeObject(maf);
	}

	public void requestFile() throws IOException {
		System.out.print("[CLIENT] Filename: ");
		String filename = in.nextLine();

		System.out.println();

		RequestFile mpf = new RequestFile(user.getIp(), serverIp, filename, user);

		objOutStr.writeObject(mpf);
	}

	public void removeFiles() throws IOException {
		System.out.println("[CLIENT] Files to remove? (end with empty input)");

		System.out.print("[CLIENT] Full path to file: ");
		String filepath = in.nextLine();

		ArrayList<File> fileList = new ArrayList<File>();

		while (!filepath.isEmpty()) {

			Path path = Paths.get(filepath);

			File file = new File(filepath, path.getFileName().toString(), user);

			fileList.add(file);

			System.out.print("[CLIENT] Full path to file: ");
			filepath = in.nextLine();

		}

		if (fileList.isEmpty()) {
			return;
		}

		DeleteFile mdf = new DeleteFile(user.getIp(), serverIp, fileList);

		objOutStr.writeObject(mdf);

	}

	public void menuLoop() throws Exception {
		int option;

		do {

			// Wait for ServerListener to release the sem to open menu again
			try {
				sem.acquire();
			} catch (InterruptedException e) {
				System.err.println("[CLIENT] ERROR: (internal) client stdout semaphore interrupted");
				return;
			}

			if (this.isTerminated()) {
				throw new Exception("[CLIENT] User Already in use");
			}

			System.out.println("Options:");
			System.out.println("	1.- Get user list");
			System.out.println("	2.- Request file");
			System.out.println("	3.- Add file");
			System.out.println("	4.- Remove File");
			System.out.println("	0.- Exit");
			System.out.println();
			System.out.print("Choose an option: ");

			String line = in.nextLine();

			System.out.println();

			try { // Line is integer
				option = Integer.parseInt(line);
			} catch (NumberFormatException e) {

				System.err.println("ERROR: option must be an integer");
				System.out.println();

				option = -1;

			}

			switch (option) {

			case 1: // List users
				UserList m = new UserList(user.getIp(), serverIp);
				objOutStr.writeObject(m);

				break;

			case 2: // Request file
				requestFile();

				break;

			case 3: // Add file
				this.addFiles();
				break;

			case 4: // Remove Files
				removeFiles();
				break;

			case 0: // Close connection
				CloseConnection mcc = new CloseConnection(user.getIp(), serverIp, user);

				objOutStr.writeObject(mcc);

				break;

			case -1:

				sem.release();

				break;

			default:

				System.err.println("[CLIENT] : option " + option + " not valid");
				System.out.println();

				sem.release();

				break;

			}

		} while (option != 0);

	}

	public void run() throws Exception {
		// Connection
		Message mc = new Connection(user.getIp(), serverIp, user);

		try {
			objOutStr.writeObject(mc);
		} catch (IOException e) {
			System.err.println("[CLIENT] ERROR: I/O error in stream");
			return;
		}

		this.addFiles();

		try {
			menuLoop();
		} catch (Exception e) {
			in.close();
			throw e;
		}
		in.close();
		sem.release();

	}

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);

		System.out.print("[CLIENT] Username: ");
		String username = in.nextLine();

		// NETWORK STUFF
		Enumeration<NetworkInterface> interfaces = null;

		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
		} catch (Exception e) {
			e.printStackTrace();
			in.close();
		}
		StringBuilder sb = new StringBuilder();

		if (interfaces.hasMoreElements())
			sb.append(interfaces.nextElement().getDisplayName());

		while (interfaces.hasMoreElements()) {
			sb.append(", ");
			sb.append(interfaces.nextElement().getDisplayName());
		}

		System.out.print("[CLIENT] Select network interface (" + sb + "): ");

		String iface = in.nextLine();

		NetworkInterface ni = null;

		try {
			ni = NetworkInterface.getByName(iface);
		} catch (SocketException e) {
			System.err.println("[CLIENT] ERROR: could not retrieve names of network interfaces");
			in.close();
			return;
		}

		if (ni == null) {
			System.err.println("[CLIENT] ERROR: network interface not found");
			in.close();
			return;
		}
		Enumeration<InetAddress> en = ni.getInetAddresses();

		InetAddress i = en.nextElement();

		while (i.getClass() != Inet4Address.class)
			i = en.nextElement();

		String hostname = i.toString().substring(1);

		System.out.print("[CLIENT] Server IP: ");
		String server = in.nextLine();

		System.out.print("[CLIENT] Server port: ");
		int port = in.nextInt();
		in.nextLine();

		System.out.println();

		// END OF NETWORK STUFF

		User user = new User(username, hostname);

		Socket sock = null;

		try {
			sock = new Socket(server, port);
		} catch (IOException e) {
			System.err.println("ERROR: server not found");
			in.close();
		}

		OutputStream outStr = null;
		InputStream inStr = null;

		try {
			outStr = sock.getOutputStream();
			inStr = sock.getInputStream();
		} catch (IOException e) {
			System.err.println("ERROR: I/O error in socket");
			in.close();
		}

		ObjectOutputStream objOutStr = null;
		ObjectInputStream objInStr = null;

		try {
			objOutStr = new ObjectOutputStream(outStr);
			objInStr = new ObjectInputStream(inStr);
		} catch (IOException e) {
			System.err.println("ERROR: I/O error in stream");
			in.close();
			return;
		}

		Client client = new Client(user, objOutStr, server);

		(new ServerListener(client, objInStr, objOutStr)).start(); // ServerListener thread

		try {
			client.run(); // Client stuff
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
