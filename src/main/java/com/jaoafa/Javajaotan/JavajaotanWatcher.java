package com.jaoafa.Javajaotan;

import java.io.File;
import java.util.TimerTask;

import com.jaoafa.Javajaotan.Lib.Library;

public class JavajaotanWatcher extends TimerTask {
	long firstlength;
	String path;
	public JavajaotanWatcher(){
		System.out.println("[JavajaotanWatcher] JavajaotanWatcher start.");

		String path = System.getProperty("java.class.path");
		System.out.println("[JavajaotanWatcher] CurrentPath: " + path);
		File file = new File(path);
		firstlength = file.length();
		System.out.println("[JavajaotanWatcher] Length: " + firstlength);
	}
	@Override
	public void run(){
		String path = Library.getCurrentpath();
		File file = new File(path);
		long length = file.length();

		if(firstlength != length){
			// changed?
			System.out.println("FileSize Changed: " + firstlength + " -> " + length);
			System.exit(0);
			return;
		}
	}
}
