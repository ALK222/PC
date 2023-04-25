package client;

import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import users.User;

public class Receptor extends Thread {

	private Socket socket;
	private String fileName;
	private FileInputStream in;
	private ObjectOutputStream out;
	private ServerSocket serverSocket;

	private Semaphore receptorSem;

	public Receptor(User user, int i, Semaphore receptorSem) {

		this.serverSocket = user;
		this.fileName = i;
		this.receptorSem = receptorSem;
	}

	public void run() {
		try {
			this.receptorSem.acquire();
			this.socket = this.serverSocket.accept();

			this.out = new ObjectOutputStream(socket.getOutputStream());
			this.in = new FileInputStream(fileName);

			byte[] bytes = new byte[8 * 1024];

			int count;
			while ((count = this.in.read(bytes)) > 0) {
				this.out.write(bytes, 0, count);
			}
			
			this.in.close();
			this.out.close();
			this.socket.close();
			this.receptorSem.release();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
