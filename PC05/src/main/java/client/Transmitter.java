package client;

import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Transmitter extends Thread {

	private int port;
	private String fileName;
	private FileInputStream in;
	private ObjectOutputStream out;
	private ServerSocket serverSocket;

	private Semaphore transmitterSem;

	public Transmitter(int port, String fileName) {

		this.port = port;
		this.fileName = fileName;
	}

	public void run() {
		try {
			this.transmitterSem.acquire();
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
			this.transmitterSem.release();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
