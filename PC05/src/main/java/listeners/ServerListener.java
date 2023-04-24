package listeners;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import client.Client;
import client.Globals;
import client.Transmitter;
import messages.Connection;
import messages.Message;
import messages.PrepClientServer;
import messages.SendFile;
import messages.UserListConfirmation;
import java.net.ServerSocket;
import server.File;
import users.User;

public class ServerListener extends Thread {

	private Socket socket;
	private ObjectInputStream in;
	private Scanner scanner;
	private Semaphore clientSem;
	private Globals globals;
	private Client client;

	public ServerListener(Socket socket, Client client, Semaphore clientSem, Globals globals) {
		try {
			this.socket = socket;
			this.in = new ObjectInputStream(this.socket.getInputStream());
			this.scanner = new Scanner(System.in);
			this.clientSem = clientSem;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.client = client;
		this.globals = globals;
	}

	public void run() {
		while (true) {
			Message m;
			try {
				m = (Message) this.in.readObject();
				switch (m.getType()) {
				case CONNECTION_ERROR:
					connectionErrorMessage(m);
					break;
				case CONNECTION_CONFIRM:
					connectionConfirmMessage(m);
					break;
				case USER_LIST_CONFIRM:
					userListConfirmMessage((UserListConfirmation) m);
					break;
				case SEND_FILE:
					sendFile((SendFile) m);
					break;
				case REQUEST_FILE_ERROR:
					System.out.println("[SERVERLISTENER]: FileRequestError");
					this.clientSem.release();
					break;
				case ADD_FILE_CONFIRM:
					System.out.println("[SERVERLISTENER] File added");
					this.clientSem.release();
					break;

				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void sendFile(SendFile m) throws IOException {
		ServerSocket socket = new ServerSocket(0); //Con 0 busca un puerto disponible automaticamente
		socket.setReuseAddress(true);
		Message mPreparado = new PrepClientServer(m.getDestUser(), m.getOrigin(), client.getName(), client.getIp(), socket.getLocalPort(), m.getFileName());
		client.sendMessage(mPreparado);
		Transmitter emisor = new Transmitter(socket, m.getPath(), clientSem);
		emisor.start();

	}

	private void userListConfirmMessage(UserListConfirmation m) {
		System.out.println("'OyenteServidor:' se ha recibido informacion de los usuarios");
		ArrayList<User> userList = m.getUserList();
		ArrayList<ArrayList<File>> fileMatrix = m.getFileMatrix();
		for (int i = 0; i < userList.size(); i++) {

			System.out.println("ID Usuario: " + userList.get(i).getId());
			System.out.println("    Ficheros:");
			for (int j = 0; j < fileMatrix.get(i).size(); j++) {
				System.out.println("        " + (j + 1) + "." + fileMatrix.get(i).get(j).getName());
			}
		}
		this.clientSem.release();
	}

	private void connectionConfirmMessage(Message m) {
		System.out.println("[SERVERLISTENER]: Connection established");
		this.clientSem.release();

	}

	private void connectionErrorMessage(Message m) {
		System.out.println("[SERVERLISTENER]: " + m.getId() + "already exists");
		System.out.print("Client name: ");
		this.client.setName(this.scanner.nextLine());
		this.client.sendMessage(new Connection(this.client.getIp(), this.client.getServerIp(),
				this.client.getClientId(), new ArrayList<File>()));
	}

}
