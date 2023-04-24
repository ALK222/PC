package messages;

import java.util.ArrayList;

import server.File;

public class DeleteFile extends Message {

	private ArrayList<File> fileList;

	public DeleteFile(String origin, String destination, String id, ArrayList<File> fileList) {
		super(origin, destination, id);
		this.type = MessageType.DELETE_FILE;
		this.fileList = fileList;
	}

	public ArrayList<File> getFileList() {
		return this.fileList;
	}

}
