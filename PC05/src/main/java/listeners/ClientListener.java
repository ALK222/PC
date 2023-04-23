package listeners;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


import messages.Message;
import sever.Server;

public class ClientListener implements Runnable {
	
	private Server server;
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
public ClientListener(Socket socket, Server server) throws IOException {
		
		this.socket = socket;
		this.server = server;
		this.in = new ObjectInputStream(this.socket.getInputStream());
		this.out = new ObjectOutputStream(this.socket.getOutputStream());
	}

	public void run() {
		try {
			while(true) {
				Message m = (Message) this.in.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
