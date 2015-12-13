package com.leopoldmarx.thegameoflife.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.leopoldmarx.thegameoflife.grid.Grid;

public class FileManager {

	private Path location;
	
	public FileManager() {
		location = Paths.get("");
	}
	
	public Grid openFile() {
		
		String file = location.toString();
		try{
			@SuppressWarnings("resource")
			ObjectInputStream in = new ObjectInputStream(
					new FileInputStream(file));
			return (Grid) in.readObject();
		}catch(IOException ex){
			ex.printStackTrace();
		}catch(ClassNotFoundException ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	public void saveFile(Grid grid) {
		
		String file = location.toString();
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(grid);
			out.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

	public Path getLocation() {
		return location;
	}

	public void setLocation(Path location) {
		this.location = location;
	}
}