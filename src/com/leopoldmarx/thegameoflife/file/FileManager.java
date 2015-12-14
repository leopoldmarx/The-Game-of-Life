package com.leopoldmarx.thegameoflife.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.leopoldmarx.thegameoflife.driver.Program;
import com.leopoldmarx.thegameoflife.grid.Grid;
import com.leopoldmarx.thegameoflife.insert.Insert;

public class FileManager {

	private Path location;
	private Path insertLocation;
	
	public FileManager() {
		location = Paths.get("");
		insertLocation = Paths.get("");
	}
	
	public void locateInsert() {
		
		String os = System.getProperty("os.name").toLowerCase();
		
		if (os.contains("win"))
			insertLocation = Paths.get(System.getenv("APPDATA") + "\\The Game of Life\\Insert.inrt");

		else if (os.contains("mac"))
			insertLocation = Paths.get(System.getProperty("user.home") + "/Library/Application Support/The Game of Life/Insert.inrt");

		else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) 
			insertLocation = Paths.get(System.getProperty("user.home"));

		else if (os.contains("sunos"))
			System.out.println("not sure");
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
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(file));
			out.writeObject(grid);
			out.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public Insert openInsert() {
		locateInsert();
		if (Files.exists(insertLocation)) {
			String file = insertLocation.toString();
			try{
				@SuppressWarnings("resource")
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream(file));
				return (Insert) in.readObject();
			}catch(IOException ex){
				ex.printStackTrace();
			}catch(ClassNotFoundException ex){
				ex.printStackTrace();
			}
		}
		else
			return new Insert();
		
		return null;
	}
	
	public void saveInsert() {
		String file = insertLocation.toString()
				.replace("/Insert.inrt", "")
				.replace("\\Insert.inrt", "");
		if (Files.exists(Paths.get(file))) {
			try{
				ObjectOutputStream out = new ObjectOutputStream(
						new FileOutputStream(
								insertLocation.toFile()));
				out.writeObject(Program.getInstance().getInsert());
				out.close();
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}
		else {
			File f = new File(file);
			f.mkdir();
			
			saveInsert();
		}
	}

	public Path getLocation() {
		return location;
	}

	public void setLocation(Path location) {
		this.location = location;
	}
}