package com.leopoldmarx.thegameoflife.driver;

import com.leopoldmarx.thegameoflife.file.FileManager;
import com.leopoldmarx.thegameoflife.insert.Insert;
import com.leopoldmarx.thegameoflife.view.ViewMain;

public class Program {

	private static Program instance;
	
	private Insert insert;
	
	private FileManager fileManager;
	
	public static Program getInstance()
	{
		if (instance == null) instance = new Program();
		return instance;
	}
	
	public Insert getInsert() {
		return insert;
	}
	
	public void setInsert(Insert insert) {
		this.insert = insert;
	}
	
	public FileManager getFileManager() {
		return fileManager;
	}
	
	private Program() {
		
		fileManager = new FileManager();
		insert = fileManager.openInsert();
		
		ViewMain vm = new ViewMain();
		
		new Thread(vm).start();
	}
}
