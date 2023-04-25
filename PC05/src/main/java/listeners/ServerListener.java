package listeners;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import client.Client;
import client.Globals;
import client.Receptor;
import client.Transmitter;
import messages.Connection;
import messages.Message;
import messages.PrepClientServer;
import messages.PrepServerClient;
import messages.SendFile;
import messages.UserListConfirmation;
import java.net.ServerSocket;
import server.File;
import users.User;

public class ServerListener extends Thread {

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Client client;

	public ServerListener(Client client, ObjectInputStream in, ObjectOutputStream out) {

		this.in = in;
		this.out = out;

		this.client = client;
	}

	public void run() {
		
		while (true) {
			Semaphore sem = this.client.getSem();
			Message m;
			try {
				m = (Message) this.in.readObject();
				switch (m.getType()) {
				case CONNECTION_ERROR:
					connectionErrorMessage(m, sem);
					break;
				case CONNECTION_CONFIRM:
					connectionConfirmMessage(m, sem);
					break;
				case USER_LIST_CONFIRM:
					userListConfirmMessage((UserListConfirmation) m, sem);
					break;
				case SEND_FILE:
					sendFile((SendFile) m, sem);
					break;
				case REQUEST_FILE_ERROR:
					System.out.println("[SERVERLISTENER]: FileRequestError");
					sem.release();
					break;
				case ADD_FILE_CONFIRM:
					System.out.println("[SERVERLISTENER] File added");
					sem.release();
					break;
				case PREPAIRING_SERVER_CLIENT:
					prepServerClientMessage((PrepServerClient) m, sem);
					break;
					
				case CLOSE_CONNECTION_CONFIRM:
					System.out.println("[SERVERLISTENER]: Connection ended");
					return;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void prepServerClientMessage(PrepServerClient m, Semaphore sem) {
		new Receptor(m.getUser(), m.getPort(), sem).start();
		
	}

	private void sendFile(SendFile m, Semaphore sem) throws IOException {
		Message mpcs = new PrepClientServer(this.client.getIp(), m.getDestUser().getIp(), this.client.getIp(), m.getPort(), this.client.getUser());
		
		try {
			this.out.writeObject(mpcs);
		} catch (Exception e) {
			e.printStackTrace();
			client.interrupt();
			sem.release();
			return;
		}
		
		new Transmitter(m.getPort(), m.getFileName()).start();
	}

	private void userListConfirmMessage(UserListConfirmation m, Semaphore sem) {
		System.out.println("[SERVERLISTENER]: user information:");
		ArrayList<User> userList = m.getUserList();
		ArrayList<ArrayList<File>> fileMatrix = m.getFileMatrix();
		for (int i = 0; i < userList.size(); i++) {

			System.out.println("User ID: " + userList.get(i).getId());
			
			System.out.println("    Files:");
			for (int j = 0; j < fileMatrix.get(i).size(); j++) {
				System.out.println("" + (j + 1) + ".-" + fileMatrix.get(i).get(j).getName());
			}
		}
		sem.release();
	}

	private void connectionConfirmMessage(Message m, Semaphore sem) {
		System.out.println("[SERVERLISTENER]: Connection established");
		sem.release();

	}

	private void connectionErrorMessage(Message m, Semaphore sem) {
		System.out.println("[SERVERLISTENER]: Error in connection");
		
		this.client.interrupt();
		sem.release();
		
	}

}
