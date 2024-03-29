package listeners;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import client.Client;
import client.Receptor;
import client.Transmitter;
import messages.Message;
import messages.PrepClientServer;
import messages.PrepServerClient;
import messages.SendFile;
import messages.UserListConfirmation;
import server.File;
import users.User;

public class ServerListener extends Thread {

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Client client;

	public ServerListener(Client client, ObjectInputStream in, ObjectOutputStream out) {

		this.in = in;
		this.out = out;

		this.client = client;
	}

	@Override
	public void run() {
		
		while (true) {
			Semaphore sem = this.client.getSem();
			sem.release();
			try {
				sem.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
					sem.release();
					break;
				case REQUEST_FILE_ERROR:
					System.out.println("[SERVERLISTENER]: FileRequestError");
					sleep(100);
					sem.release();
					break;
				case ADD_FILE_CONFIRM:
					System.out.println("[SERVERLISTENER] File added");
					sleep(100);
					sem.release();
					break;
				case DELETE_FILE_CONFIRM:
					System.out.println("[SERVERLISTENER] Files deleted");
					sleep(100);
					sem.release();
					break;
				case PREPAIRING_SERVER_CLIENT:
					prepServerClientMessage((PrepServerClient) m, sem);
					sem.release();
					break;
					
				case CLOSE_CONNECTION_CONFIRM:
					System.out.println("[SERVERLISTENER]: Connection ended");
					sleep(100);
					sem.release();
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
		Message mpcs = new PrepClientServer(this.client.getUser().getIp(), m.getDestUser().getIp(), m.getPort(), this.client.getUser());
		
		try {
			this.out.writeObject(mpcs);
		} catch (Exception e) {
			e.printStackTrace();
			client.terminate();
			sem.release();
			return;
		}
		
		
		new Transmitter(m.getPort(), m.getFile()).start();
	}

	private void userListConfirmMessage(UserListConfirmation m, Semaphore sem) throws Exception {
		//System.out.println("[SERVERLISTENER]: user information:");
		ArrayList<User> userList = m.getUserList();
		ArrayList<ArrayList<File>> fileMatrix = m.getFileMatrix();
		for (int i = 0; i < userList.size(); i++) {

			System.out.println("User ID: " + userList.get(i).getId());
			
			System.out.println("    Files:");
			for (int j = 0; j < fileMatrix.get(i).size(); j++) {
				System.out.println("" + (j + 1) + ".-" + fileMatrix.get(i).get(j).getName());
			}
		}
		
		sleep(100);
		sem.release();
	}

	private void connectionConfirmMessage(Message m, Semaphore sem) {
		//System.out.println("[SERVERLISTENER]: Connection established");
		sem.release();

	}

	private void connectionErrorMessage(Message m, Semaphore sem) {
		//System.out.println("[SERVERLISTENER]: Error in connection");
		
		this.client.terminate();
		sem.release();
		
	}

}
