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
import java.util.ArrayList;

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
			insertLocation = Paths.get(System.getenv("APPDATA") + "\\The Game of Life");

		else if (os.contains("mac"))
			insertLocation = Paths.get(System.getProperty("user.home") + "/Library/Application Support/The Game of Life");

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
	
	@SuppressWarnings("resource")
	public Insert openInsert() {
		locateInsert();
		if (Files.exists(insertLocation)) {
			
			File folder = insertLocation.toFile();
			File[] listOfFiles = folder.listFiles();
			
			ArrayList<Grid> insert = new ArrayList<>();
			try{
				
				for (File listOfFile : listOfFiles) {
				
					ObjectInputStream in = new ObjectInputStream(
							new FileInputStream(listOfFile));
					insert.add((Grid) in.readObject());
				}
				Insert i = new Insert();
				i.setArray(insert);
				return i;
			}
			catch(Exception e) {e.printStackTrace();}
		}
		else {
			insertLocation.toFile().mkdir();
			return new Insert();
		}
		return null;
	}
	
	public void saveInsert() {
		
		insertLocation.toFile().delete();
		insertLocation.toFile().mkdir();
		
		try{
			for (Grid g : Program.getInstance().getInsert().getArray()) {
				ObjectOutputStream out = new ObjectOutputStream(
						new FileOutputStream(
								insertLocation.toString() 
								+ "/" + g.getName() + ".tgof"));
				out.writeObject(g);
				out.close();
			}	
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