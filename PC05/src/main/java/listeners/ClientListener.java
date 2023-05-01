package listeners;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import messages.AddFiles;
import messages.AddFileConfirmation;
import messages.CloseConnection;
import messages.CloseConnectionConfirm;
import messages.Connection;
import messages.ConnectionConfirm;
import messages.ConnectionError;
import messages.DeleteFile;
import messages.Message;
import messages.DeleteFileConfirm;
import messages.PrepClientServer;
import messages.PrepServerClient;
import messages.RequestFile;
import messages.RequestFileError;
import messages.SendFile;
import messages.UserList;
import messages.UserListConfirmation;
import server.File;
import server.Server;
import users.User;

public class ClientListener extends Thread {

	private Server server;
	private InputStream in;
	private OutputStream out;

	public ClientListener(Server server, InputStream in, OutputStream out) throws IOException {

		this.server = server;
		this.in = in;
		this.out = out;
	}

	@Override
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

		User user = null;

		while (true) {
			Message m;
			try {
				m = (Message) inStr.readObject();
			} catch (Exception e) {
				return;
			}

			switch (m.getType()) {
			case CONNECTION:
				try {
					user = ((Connection) m).getUser();
					connectionMessage((Connection) m, outStr, user);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case USER_LIST:
				userListMessage((UserList) m, outStr);
				break;
			case ADD_FILE:
				addFileMessage((AddFiles) m, outStr, user);
				break;
				
			case DELETE_FILE:
				deleteFileMessage((DeleteFile) m, outStr, user);
			case CLOSE_CONNECTION:
				closeConnectionMessage((CloseConnection) m, outStr);
				break;
			case REQUEST_FILE:
				requestFileMessage((RequestFile) m, outStr);
				break;
			case PREPAIRING_CLIENT_SERVER:
				prepClientServerMessage((PrepClientServer) m, outStr, user);
				break;
			default:
				System.err.print("[CLIENTLISTENER] ERROR\n");
				break;
			}
		}
	}

	private void addFileMessage(AddFiles m, ObjectOutputStream outStr, User user) {
		if (user == null) {
			System.err.println("[CLIENTLISTENER] No user found");
			return;
		}
		
		this.server.addFile(user.getId(), m.getFileList());
		
		AddFileConfirmation mafc = new AddFileConfirmation(m.getDestination(), m.getOrigin());
		
		try {
			outStr.writeObject(mafc);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private void deleteFileMessage(DeleteFile m, ObjectOutputStream outStr, User user) {
		if (user == null) {
			System.err.println("[CLIENTLISTENER] No user found");
			return;
		}
		
		this.server.removeFile(user.getId(), m.getFileList());
		
		DeleteFileConfirm mafc = new DeleteFileConfirm(m.getDestination(), m.getOrigin());
		
		try {
			outStr.writeObject(mafc);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	private void prepClientServerMessage(PrepClientServer m, ObjectOutputStream outStr, User user) {
		if (user == null) {
			System.err.println("[CLIENTLISTENER] No user found");
			return;
		}

		User dest = this.server.getUser(m.getUserDest());

		if (dest == null) {
			return;
		}

		ObjectOutputStream outStr2 = this.server.getObjectOutputStream(dest);

		if (outStr2 == null) {
			return;
		}

		Message mpsc = new PrepServerClient(m.getDestination(), m.getOrigin(), dest, m.getPort());
		try {
			outStr2.writeObject(mpsc);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private void connectionMessage(Connection m, ObjectOutputStream outStr, User user) throws IOException {
		user = m.getUser();

		if (server.hasUser(user.getId())) {

			Message mur = new ConnectionError(m.getDestination(), m.getOrigin(), user.getId());

			try {
				outStr.writeObject(mur);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			return;

		}
		if (!this.server.addUser(user, outStr))
			return;

		Message mcc = new ConnectionConfirm(m.getDestination(), m.getOrigin());

		try {
			outStr.writeObject(mcc);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	private void userListMessage(UserList m, ObjectOutputStream outStr) {
		ArrayList<User> userList = this.server.getUserList();
		ArrayList<ArrayList<File>> fileMatrix = this.server.getFileMatrix();
		if (userList == null) {
			return;
		}
		if (fileMatrix == null) {
			return;
		}

		Message mul = new UserListConfirmation(m.getDestination(), m.getOrigin(), userList, fileMatrix);

		try {
			outStr.reset();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			outStr.writeObject(mul);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	private void closeConnectionMessage(CloseConnection m, ObjectOutputStream outStr) {
		if (!this.server.deleteUser(m.getUser())) {
			return;
		}
		
		System.out.println("CLIENTLISTENER: connection ended");

		Message mcc = new CloseConnectionConfirm(m.getDestination(), m.getOrigin());

		try {
			outStr.writeObject(mcc);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private void requestFileMessage(RequestFile m, ObjectOutputStream outStr) {
		String filename = m.getFileName();
		File file = this.server.getFileWithFilename(filename);

		if (file == null) {
			RequestFileError mfe = new RequestFileError(m.getDestination(), m.getOrigin());

			try {
				outStr.writeObject(mfe);

			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		} else {
			ObjectOutputStream userOut = this.server.getObjectOutputStream(file.getUser());

			if (userOut == null) {
				return;
			}

			SendFile msf = new SendFile(m.getDestination(), m.getOrigin(), file, m.getUser(), this.server.getAndIncrementNextPort());

			try {
				outStr.writeObject(msf);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}

}
