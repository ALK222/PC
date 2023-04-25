package messages;

import java.util.ArrayList;

import server.File;

public class DeleteFile extends Message {

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
