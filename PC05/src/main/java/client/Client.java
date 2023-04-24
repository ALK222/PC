package client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import listeners.ServerListener;
import messages.AddFiles;
import messages.CloseConnection;
import messages.Connection;
import messages.DeleteFile;
import messages.Message;
import messages.RequestFile;
import messages.UserList;
import server.File;
import utils.Lock;
import utils.LockRompeEmpate;

public class Client extends Thread {
	private String clientIp;
	private String id;
	private ServerListener serverListener;
	private ObjectOutputStream out;
	private String serverIp;
	private int serverPort;
	private Semaphore clientSem;
	private Scanner scanner;
	private Socket socket;
	private boolean activeConnection;

	public Client(String serverIp, int serverPort, String clientIp) {

		try {
			this.serverIp = serverIp;
			this.serverPort = serverPort;
			this.clientIp = clientIp;

			this.clientSem = new Semaphore(0);
			this.socket = new Socket(serverIp, serverPort);
			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.scanner = new Scanner(System.in);
			this.activeConnection = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("[CLIENT]: New Client created");
	}

	public void run() {
		try {
			System.out.print("[CLIENT] Client name: ");
			this.id = scanner.nextLine();

			while (this.id == null || this.id.isEmpty()) {
				System.out.println("[CLIENT] Invalid id. Client name: ");
				this.id = scanner.nextLine();
			}

			this.sendMessage(new Connection(this.clientIp, this.serverIp, id, new ArrayList<File>()));
			this.serverListener = new ServerListener(socket, this, clientSem, null);
			this.serverListener.start();

			while (this.activeConnection) {
				menu();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void menu() throws Exception {
		int option = 0;

		do {
			this.clientSem.acquire();
			option = menuAux();
			switch (option) {
			case 0:
				this.closeConnection();
				return;
			case 1:
				this.requestUserList();
				break;
			case 2:
				this.sendFileList(this.requestSendFileList());
				break;
			case 3:
				this.stopSendFileList(this.requestStopSendFileList());
				break;
			case 4:
				this.downloadFile(this.requestFile());
				break;
			}
		} while (true);
	}

	private void downloadFile(String requestFile) throws IOException {
		this.out.writeObject(new RequestFile(this.clientIp, this.serverIp, this.id, requestFile));		
	}

	private String requestFile() {
		System.out.print("[CLIENT] File to download: ");
		return scanner.nextLine();
	}

	private int menuAux() {
		int op;

		System.out.print(
				"0.- Close Connection\n1.- User List\n2.- Share Files\n3.- Stop Sharing Files\n4.- Request File\n");
		System.out.print(">>");
		String aux = scanner.nextLine();

		while (Integer.parseInt(aux) < 0 || Integer.parseInt(aux) > 4) {
			System.out.println("Invalid option");
			System.out.print(">>");
			aux = scanner.nextLine();
		}
		op = Integer.parseInt(aux);

		return op;
	}

	public void closeConnection() throws IOException {
		this.out.writeObject(new CloseConnection(this.clientIp, this.serverIp, this.id));
	}

	private void requestUserList() throws IOException {
		this.out.writeObject(new UserList(this.clientIp, this.serverIp, id));

	}

	private ArrayList<File> requestSendFileList() {
		ArrayList<File> shareFiles = new ArrayList<File>();
		System.out.println("Name of files to share (END): ");
		String f = scanner.nextLine();
		while (!f.equalsIgnoreCase("END")) {
			shareFiles.add(new File(f, f));
			f = scanner.nextLine();
		}
		return shareFiles;
	}

	private void sendFileList(ArrayList<File> requestSendFileList) throws IOException {
		this.out.writeObject(new AddFiles(this.clientIp, this.serverIp, this.id, requestSendFileList));
	}

	private ArrayList<File> requestStopSendFileList() {
		ArrayList<File> shareFiles = new ArrayList<File>();
		System.out.println("Name of files to stop sharing (END): ");
		String f = scanner.nextLine();
		while (!f.equalsIgnoreCase("END")) {
			shareFiles.add(new File(f, f));
			f = scanner.nextLine();
		}
		return shareFiles;
	}

	private void stopSendFileList(ArrayList<File> requestStopSendFileList) throws IOException {
		this.out.writeObject(new DeleteFile(this.clientIp, this.serverIp, this.id, requestStopSendFileList));

	}

	public void sendMessage(Message m) {
		try {
			this.out.writeObject(m);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getIp() {
		return this.clientIp;
	}

	public String getServerIp() {
		return this.serverIp;
	}

	public String getClientId() {
		return this.id;
	}
	
	public static int main(String args[]) {
		if (args.length != 3) {
    		System.out.println("Args: \n1.-Server IP\n2.- Server Port \n3.- Client ip");
    		return -1;
    	}
    	else {
    		String ipServidor = args[0];
    		int puertoServidor = Integer.parseInt(args[1]);
    		String ipCliente = args[2];
    		
    		Client cliente = new Client(ipServidor, puertoServidor, ipCliente);
    		cliente.run();
    	}
		return 0;
	}

}
