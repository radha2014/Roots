package com.tools.xslxml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOperation {

	private String filename;
	private FileOutputStream outFile;

	public FileOperation(String filename) throws FileNotFoundException {
		super();
		this.filename = filename;
		open();
	}

	public void open() throws FileNotFoundException {
		File f = new File(filename);
		outFile = new FileOutputStream(f);
	}

	public void writeToFile(String str) throws IOException {
		outFile.write(str.getBytes());
		outFile.write("\n\r".getBytes());
	}

	public void close() throws IOException {

		if (outFile != null) {
			outFile.close();
		}

	}

}
