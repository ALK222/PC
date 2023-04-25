package client;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import server.File;

public class Transmitter extends Thread {

	private int port;
	private File f;

	private Semaphore transmitterSem;

	public Transmitter(int port, File f) {

		this.port = port;
		this.f = f;
	}

	public void run() {
		
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(this.port);
			Socket socket;
			
			socket = serverSocket.accept();
			
			OutputStream outStr;			
			
			outStr = socket.getOutputStream();
			
			ObjectOutputStream objStr = new ObjectOutputStream(outStr);
			
			FileInputStream fileStr = new FileInputStream(this.f.getPath());
			
			fileStr = new FileInputStream(f.getPath());
			
			objStr.writeObject(this.f.toString());
			
			DataOutputStream dataStr = new DataOutputStream(new BufferedOutputStream(outStr));
			
			byte[] buffer = new byte[1024];
			int read = 0;
			
			while(true) {
				try {
					if((read = fileStr.read(buffer)) < 0) {
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				dataStr.write(buffer,0,read);
			}
			dataStr.close();
			fileStr.close();

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

}
