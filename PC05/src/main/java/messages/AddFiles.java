package messages;

import java.util.ArrayList;

import server.File;

public class AddFiles extends Message {

	private ArrayList<File> fileList;

	public AddFiles(String origin, String destination, ArrayList<File> fileList) {
		super(origin, destination);
		this.type = MessageType.ADD_FILE;
		this.fileList = fileList;
	}

	public ArrayList<File> getFileList() {
		return this.fileList;
	}

}
