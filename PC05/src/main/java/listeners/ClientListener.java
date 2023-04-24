package listeners;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messages.Connection;
import messages.ConnectionConfirm;
import messages.ConnectionError;
import messages.Message;
import server.Server;
import users.User;
import users.UserFlow;

public class ClientListener implements Runnable {

	private Server server;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public ClientListener(Socket socket, ObjectInputStream in, ObjectOutputStream out) throws IOException {

		this.server = server;
		this.in = in;
		this.out = out;
	}

	public void run() {
		ObjectOutputStream outStr;
		ObjectInputStream inStr;
		try {
			outStr = new ObjectOutputStream(out);
			inStr = new ObjectInputStream(in);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		while (true) {
			Message m;
			try {
				m = (Message) inStr.readObject();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			switch (m.getType()) {
			case CONNECTION:
				try {
					connectionMessage((Connection) m, outStr);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
	}

	private void connectionMessage(Connection m, ObjectOutputStream outStr) throws IOException {
		User user = m.getUser();

		// Si el usuario ya existe, mandar MENSAJE_USUARIO_REPETIDO a OyenteServidor
		if (server.hasUser(user.toString())) {

			Message mur = new ConnectionError(user.toString());

			try {
				outStr.writeObject(mur);
			} catch (IOException e) {
				System.err.println("ERROR: I/O error in stream");
				return;
			}

			return;

		}

		// Añadir usuario a las tablas del servidor
		if (this.server.addUser(user, outStr))
			return;

		if (this.server.addToUserList(user))
			return;

		// Mandar MENSAJE_CONFIRMACION_CONEXION a OyenteServidor
		Mensaje mcc = new MensajeConfirmacionConexion();

		try {
			objOutStr.writeObject(mcc);
		} catch (IOException e) {
			System.err.println("ERROR: I/O error in stream");
			return;
		}
	}

}
