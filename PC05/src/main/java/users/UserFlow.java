package users;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UserFlow {

	private String id;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public UserFlow(String id, ObjectInputStream in, ObjectOutputStream out) {
		this.id = id;
		this.in = in;
		this.out = out;
	}
	
	public String getId() {
		return this.id;
	}
	
	public ObjectInputStream getIn() {
		return this.in;
	}
	
	public ObjectOutputStream getOut() {
		return this.out;
	}
}
