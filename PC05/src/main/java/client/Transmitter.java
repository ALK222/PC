package client;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import server.File;

public class Transmitter extends Thread {

	private int port;
	private File f;

	public Transmitter(int port, File f) {

		this.port = port;
		this.f = f;
	}

	public void run() {

		ServerSocket serverSocket = null;
		FileInputStream fileStr = null;
		DataOutputStream dataStr = null;
		try {
			serverSocket = new ServerSocket(this.port);
			Socket socket;

			socket = serverSocket.accept();

			OutputStream outStr;

			outStr = socket.getOutputStream();

			ObjectOutputStream objStr = new ObjectOutputStream(outStr);

			fileStr = new FileInputStream(this.f.toString());

			objStr.writeObject("STARTING"); // Start """"Flag""""

			objStr.writeObject(this.f.toString());

			dataStr = new DataOutputStream(new BufferedOutputStream(outStr));

			byte[] buffer = new byte[1024];
			int read = 0;

			while (true) {
				try {
					if ((read = fileStr.read(buffer)) < 0) {
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				dataStr.write(buffer, 0, read);
			}
			
			dataStr.close();
			fileStr.close();
			serverSocket.close();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				dataStr.close();
				fileStr.close();
				serverSocket.close();
			} catch (IOException e1) {
			}
			return;
		}

		

	}

}
