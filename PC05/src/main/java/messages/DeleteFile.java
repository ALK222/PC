package messages;

import java.util.ArrayList;

import server.File;

public class DeleteFile extends Message {

	private static final long serialVersionUID = 1401818229259158139L;
	private ArrayList<File> fileList;

	public DeleteFile(String origin, String destination, ArrayList<File> fileList) {
		super(origin, destination);
		this.type = MessageType.DELETE_FILE;
		this.fileList = fileList;
	}

	public ArrayList<File> getFileList() {
		return this.fileList;
	}

}
