package com.leopoldmarx.thegameoflife.grid;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents an entire Grid of the Game of Life.
 * Can generate future generations.
 * 
 * @author Leopold Marx
 */
public class Grid {
	
	private int width;
	private int height;
	private int resolution;
	
	private ArrayList<Square> array = new ArrayList<>();

	private ArrayList<Square> nextArray = array;
	
	private boolean toroidalArray;
	
	public Grid(int x, int y) {
		this.width  = x;
		this.height = y;
		this.resolution = 25;
		this.toroidalArray = false;
	}
	
	/**
	 * Adds a Square at values x and y to the array.
	 * 
	 * @param x Horizontal value of where the Square would be stored.
	 * @param y Vertical value of where the Square would be stored.
	 */
	public void addSquare(int x, int y) {
		Collections.sort(array);
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
		if (Collections.binarySearch(array, new Square(x, y)) > 0)
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
							//if out  of bounds  if  toroidalArrayI
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
}