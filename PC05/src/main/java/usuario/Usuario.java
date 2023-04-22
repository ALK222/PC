package usuario;

import java.io.Serializable;
import java.util.ArrayList;

import mensajes.Mensaje;

public class Usuario implements Serializable {

	private static final long serialVersionUID = 1148386234085335627L;

	private String id;
	private String ip;
	private ArrayList<Mensaje> messageList;

	public Usuario(String id, String ip) {
		super();
		this.id = id;
		this.ip = ip;
		this.messageList = new ArrayList<Mensaje>();
	}

	public Usuario(String id, String ip, ArrayList<Mensaje> listaMensajes) {
		super();
		this.id = id;
		this.ip = ip;
		this.messageList = new ArrayList<Mensaje>(listaMensajes);
	}

	public Usuario(Usuario usuario) {
		this.id = usuario.id;
		this.ip = usuario.ip;
		this.messageList = new ArrayList<Mensaje>(usuario.messageList);
	}

	void addMessage(Mensaje message) {
		this.messageList.add(message);
	}

	void removeMessage(Mensaje message) {
		int index = -1;
		for (Mensaje m : messageList) {
			if(m.equals(message)) {
				index = messageList.indexOf(m);
			}
		}
		if(index != -1) {
			messageList.remove(index);
		}
	}

}
