package com.leopoldmarx.thegameoflife.grid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultListModel;

/**
 * Represents an entire Grid of the Game of Life.
 * Can generate future generations.
 * 
 * @author Leopold Marx
 */
public class Grid implements Serializable {
	
	private static final long serialVersionUID = -1723253648442129097L;
	
	private int width;
	private int height;
	private int resolution;
	private Integer generations;
	
	private ArrayList<Square> array = new ArrayList<>();

	private transient ArrayList<Square> nextArray = array;
	
	private boolean toroidalArray;
	
	public Grid() {
		width = 0;
		height = 0;
		generations = 0;
		resolution = 20;
		toroidalArray = false;
		array = new ArrayList<>();
		nextArray = array;
	}
	
	public Grid(int x, int y) {
		this.width  = x;
		this.height = y;
		this.generations = 0;
		this.resolution = 20;
		this.toroidalArray = false;
		this.array = new ArrayList<>();
		this.nextArray = this.array;
	}
	
	/**
	 * Adds a Square at values x and y to the array.
	 * 
	 * @param x Horizontal value of where the Square would be stored.
	 * @param y Vertical value of where the Square would be stored.
	 */
	public void addSquare(int x, int y) {
		//Sorts array
		Collections.sort(array);
		
		//Checks if the Square is already in the array.
		//If not, it adds that Square
		if (Collections.binarySearch(array, new Square(x, y)) < 0)
			array.add(new Square(x, y));
	}
	
	/**
	 * Adds Square to next Array at values x and y.
	 * 
	 * @param x Horizontal value of where the Square would be stored.
	 * @param y Vertical value of where the Square would be stored.
	 */
	private void addSquareToNextArray(int x, int y) {
		Collections.sort(nextArray);
		if (Collections.binarySearch(nextArray, new Square(x, y)) < 0)
			nextArray.add(new Square(x, y));
	}
	
	/**
	 * Deletes Square from the array at x and y.
	 * 
	 * @param x Horizontal value of where the square would be deleted.
	 * @param y Vertical value of where the Square would be deleted.
	 */
	public void deleteSquare(int x, int y) {
		Collections.sort(array);
		int i = Collections.binarySearch(array, new Square(x, y));
		if (i >= 0) 
			array.remove(i);
	}
	
	/**
	 * Deletes Square from nextArray at x and y.
	 * 
	 * @param x Horizontal value of where the square would be deleted.
	 * @param y Vertical value of where the Square would be deleted.
	 */
	private void deleteSquareFromNextArray(int x, int y) {
		Collections.sort(nextArray);
		int i = Collections.binarySearch(nextArray, new Square(x, y));
		if (i >= 0) 
			nextArray.remove(i);
	}
	
	/**
	 * Gets value of the Grid at values x and y
	 * 
	 * @param x Horizontal value of where the square would be found.
	 * @param y Vertical value of where the Square would be found.
	 * @return if there is a Square at x and y
	 */
	public boolean getValue(int x, int y) {
		Collections.sort(array);
		if (Collections.binarySearch(array, new Square(x, y)) >= 0)
			return true;
		return false;
	}

	/**
	 * Generates next frame for the Game of Life.
	 */
	@SuppressWarnings("unchecked")
	public void nextGeneration() {
		
		//Sorts the array before hand so both array and nextArray are sorted
		Collections.sort(array);
		
		//Clones  array  to nextArray to  not confuse  next
		//generation's values with this generation's values
		nextArray = (ArrayList<Square>) array.clone();
		
		//Cycles through all values of the grid 
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				int count = 0;
				
				int xLow, xHigh;
				int yLow, yHigh;
				
				//Sets range for search to normal
				if (toroidalArray) {
					xLow = -1;
					xHigh = 1;
					yLow = -1;
					yHigh = 1;
				}
				
				//Sets range to only be within range
				else {
					if (y == 0) {
						yLow    = 0;
						yHigh = 1;
					}
					else if(y == height - 1) {
						yLow   = -1;
						yHigh = 0; 
					}
					else {
						yLow   = -1;
						yHigh = 1;
					}
					
					if (x == 0) {
						xLow    = 0;
						xHigh = 1;
					}
					else if (x == width - 1) {
						xLow   = -1;
						xHigh = 0;
					}
					else{
						xLow   = -1;
						xHigh = 1;
					}
				}
				
				//Loops  through  the  squares  around x, y
				//indicated by xLow, xHigh, yLow, and yHigh
				for (int tempY = yLow; tempY <= yHigh; tempY++) {
					for (int tempX = xLow; tempX <= xHigh; tempX++) {
						
						int xValue = x + tempX;
						int yValue = y + tempY;
						
						//Return false if tempX and tempY are both 0
						if (tempX + tempY != 0 || (tempX != 0 && tempY != 0)) {
							
							//Wraps the position to the other side
							//if out  of bounds  if  toroidalArray
							if (toroidalArray) {
								if (xValue == -1) xValue = width - 1;
								else if (xValue == width) xValue = 0;
								
								if (yValue == -1) yValue = height - 1;
								else if (yValue == height) yValue = 0;
							}
							
							//Searches if the square exists in the array
							int value = Collections.binarySearch(array, new Square(xValue, yValue));
							if (value >= 0) count++;
						}
					}
				}
				
				//Adds square to nextArray if it does not exist anymore
				if (count == 3)
					addSquareToNextArray(x, y);
				
				//Deletes square from nextArray if it exist
				if (count <  2 || count > 3)
					deleteSquareFromNextArray(x, y);
			}
		}
		
		array = nextArray;
	}
	
//	public void makeModel() {
//		model.clear();
//		for (Square s : array)
//			model.addElement(s);
//	}
	
	public static Grid glider() {
		Grid g = new Grid(3, 3);
		
		g.addSquare(0, 0);
		g.addSquare(2, 0);
		g.addSquare(1, 1);
		g.addSquare(2, 1);
		g.addSquare(1, 2);
		
		return g;
	}
	
	public static Grid lightweightSpaceship() {
		Grid g = new Grid(5, 4);
		
		g.addSquare(1, 0);
		g.addSquare(2, 0);
		g.addSquare(3, 0);
		g.addSquare(4, 0);
		g.addSquare(0, 1);
		g.addSquare(4, 1);
		g.addSquare(4, 2);
		g.addSquare(0, 3);
		g.addSquare(3, 3);
		
		return g;
	}
	
	public static Grid gospersGliderGun() {
		Grid g = new Grid(36, 9);
		
		g.addSquare(0, 4);
		g.addSquare(0, 5);
		g.addSquare(1, 4);
		g.addSquare(1, 5);
		
		g.addSquare(10, 4);
		g.addSquare(10, 5);
		g.addSquare(10, 6);
		g.addSquare(11, 3);
		g.addSquare(11, 7);
		g.addSquare(12, 2);
		g.addSquare(13, 2);
		g.addSquare(12, 8);
		g.addSquare(13, 8);
		
		g.addSquare(14, 5);
		g.addSquare(15, 3);
		g.addSquare(15, 7);
		g.addSquare(16, 4);
		g.addSquare(16, 5);
		g.addSquare(16, 6);
		g.addSquare(17, 5);
		
		g.addSquare(20, 2);
		g.addSquare(20, 3);
		g.addSquare(20, 4);
		g.addSquare(21, 2);
		g.addSquare(21, 3);
		g.addSquare(21, 4);
		g.addSquare(22, 1);
		g.addSquare(22, 5);
		
		g.addSquare(24, 0);
		g.addSquare(24, 1);
		
		g.addSquare(24, 5);
		g.addSquare(24, 6);
		
		g.addSquare(34, 2);
		g.addSquare(34, 3);
		g.addSquare(35, 2);
		g.addSquare(35, 3);
		
		return g;
	}
	
	public static Grid blinker() {
		Grid g = new Grid (1, 3);
		
		g.addSquare(0, 0);
		g.addSquare(0, 1);
		g.addSquare(0, 2);
		
		return g;
	}
	
	public static Grid beehive() {
		Grid g = new Grid(3, 4);
		
		g.addSquare(1, 0);
		g.addSquare(0, 1);
		g.addSquare(0, 2);
		g.addSquare(2, 1);
		g.addSquare(2, 2);
		g.addSquare(1, 3);
		
		return g;
	}
	
	public static Grid toad() {
		Grid g = new Grid (4, 2);
		
		g.addSquare(1, 0);
		g.addSquare(2, 0);
		g.addSquare(3, 0);
		
		g.addSquare(0, 1);
		g.addSquare(1, 1);
		g.addSquare(2, 1);
		
		return g;
	}
	
	public static Grid beacon() {
		Grid g = new Grid(4, 4);
		
		g.addSquare(0, 0);
		g.addSquare(1, 0);
		g.addSquare(0, 1);
		
		g.addSquare(3, 2);
		g.addSquare(2, 3);
		g.addSquare(3, 3);
		
		return g;
	}
	
	public static Grid pulsar() {
		//TODO Pulsar
		Grid g = new Grid();
		return g;
	}
	
	public void rotate() {
		Grid g = new Grid(this.height, this.width);
		for (Square s : this.getArray())
			g.addSquare(this.height - 1 - s.getY(), s.getX());
		this.setArray(g.getArray());
		this.setWidth(g.getWidth());
		this.setHeight(g.getHeight());
	}
	
	public void flip() {
		ArrayList<Square> tempArray = new ArrayList<>();
		for (Square s : this.getArray())
			tempArray.add(new Square(this.width - 1 - s.getX(), s.getY()));
		this.setArray(tempArray);
	}
	
	public boolean isToroidalArray() {
		return toroidalArray;
	}

	public void setToroidalArray(boolean toroidalArray) {
		this.toroidalArray = toroidalArray;
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getResolution() {
		return resolution;
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}
	
	public void incrementGenerations () {
		generations++;
	}
	
	public Integer getGenerations() {
		return generations;
	}

	public void setGenerations(Integer generations) {
		this.generations = generations;
	}

	public ArrayList<Square> getArray() {
		return array;
	}

	public void setArray(ArrayList<Square> array) {
		this.array = array;
	}
}