package client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import users.User;

public class Receptor extends Thread {

	private User user;
	private int port;
	private Semaphore sem;

	public Receptor(User user, int port, Semaphore sem) {

		this.user = user;
		this.port = port;
		this.sem = sem;
	}

	public void run() {
		try {
			Socket socket;

			socket = new Socket(user.getIp(), port);

			InputStream inStr = socket.getInputStream();
			ObjectInputStream objStr = new ObjectInputStream(inStr);

			String file = (String) objStr.readObject();

			if (!file.equals("STARTING")) {
				sem.release();
				return;
			}

			String filename = (String) objStr.readObject();

			FileOutputStream fileStr = new FileOutputStream(filename);

			DataInputStream dataStr = new DataInputStream(new BufferedInputStream(inStr));

			byte[] buffer = new byte[1024]; // buffer de bytes
			int read;

			while (true) {
				try {
					if ((read = dataStr.read(buffer)) < 0) {
						break;
					}
				} catch (IOException e) {
					System.err.println("ERROR: I/O error in file stream");
					System.out.println();
					sem.release();
					return;
				}

				// Leer bytes del buffer y escribirlos en el fichero
				try {
					fileStr.write(buffer, 0, read);
				} catch (IOException e) {
					System.err.println("ERROR: I/O error in data stream");
					System.out.println();
					sem.release();
					return;
				}
			}
			
			dataStr.close();
			fileStr.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
			sem.release();
		}

	}
}
