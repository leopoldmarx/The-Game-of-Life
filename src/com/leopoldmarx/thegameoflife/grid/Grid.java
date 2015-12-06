package com.leopoldmarx.thegameoflife.grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grid {

	private int height;
	private int width;
	private int resolution;
	
	private ArrayList<Square> array = new ArrayList<>();

	private ArrayList<Square> nextArray = array;
	
	private boolean toroidalArray;
	
	public Grid(int x, int y) {
		this.width  = x;
		this.height = y;
		this.resolution = 25;
	}
	
	public void addSquare(int x, int y) {
		boolean contains = false;
		for (int i = 0; i < array.size() && !contains; i++)
			if(array.get(i).getX() == x 
					&& array.get(i).getY() == y)
				contains = true;
		
		if (!contains)
			array.add(new Square(x, y));		
	}
	
	public void addSquare(Square square) {
		boolean contains = false;
		for (int i = 0; i < array.size() && !contains; i++)
			if(array.get(i).getX() == square.getX() 
					&& array.get(i).getY() == square.getY())
				contains = true;
		
		if (!contains)
			array.add(square);				
	}
	
	public void deleteSquare(int x, int y) {
		boolean complete = true;
		for (int i = 0; i < array.size() && complete; i++)
			if(array.get(i).getX() == x 
					&& array.get(i).getY() == y) {
				array.remove(i);
				complete = false;
			}
	}
	
	public void deleteSquare(Square square) {
		boolean complete = true;
		for (int i = 0; i < array.size() && complete; i++)
			if(array.get(i).getX() == square.getX()
					&& array.get(i).getY() == square.getY()) {
				array.remove(i);
				complete = false;
			}
	}
	
	public void deleteSquareFromNextArray(int x, int y) {
		boolean contains = false;
		for (int i = 0; i < nextArray.size() && !contains; i++)
			if(nextArray.get(i).getX() == x 
					&& nextArray.get(i).getY() == y) {
				nextArray.remove(i);
				contains = true;
			}
	}
	
	public boolean getValue(int x, int y) {
		boolean contains = false;
		for (int i = 0; i < array.size() && !contains; i++)
			if (array.get(i).equals(new Square(x, y)))
				contains = true;
		
		return contains;
	}
	
	public void nextGeneration() {
		
		nextArray = (ArrayList<Square>) array.clone();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				
				if (getValue(x, y)){
					if (!adjacentSquaresCount(new Square(x, y)))
						deleteSquareFromNextArray(x, y);
				}
				
				else {
					int count = 0;
					
					int xLow, xHigh;
					int yLow, yHigh;
					
					if (toroidalArray) {
						xLow = -1;
						xHigh = 1;
						yLow = -1;
						yHigh = 1;
					}
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
					
					for (int tempY = yLow; tempY <= yHigh; tempY++) {
						for (int tempX = xLow; tempX <= xHigh; tempX++) {
							
							int xValue = x + tempX;
							int yValue = y + tempY;
							
							if (tempX + tempY != 0 || (tempX != 0 && tempY != 0)) {
								if (toroidalArray){
									if (xValue == -1) xValue = width - 1;
									else if (xValue == width - 1) xValue = 0;
									
									if (yValue == -1) yValue = height - 1;
									else if (yValue == height - 1) yValue = 0;
								}
								
								for (Square s : array)
									if (s.getX() == xValue && s.getY() == yValue)
										count++;
							}
						}
					}
					
					if (count == 3)
						nextArray.add(new Square(x, y));
				}
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

	private boolean adjacentSquaresCount(Square square) {
		
		int x = square.getX();
		int y = square.getY();
		
		int count = 0;
		
		int xLow, xHigh;
		int yLow, yHigh;
		
		if (toroidalArray) {
			xLow = -1;
			xHigh = 1;
			yLow = -1;
			yHigh = 1;
		}
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
		for (int tempY = yLow; tempY <= yHigh; tempY++) {
			for (int tempX = xLow; tempX <= xHigh; tempX++) {
				int xValue = x + tempX;
				int yValue = y + tempY;
				
				if (tempX + tempY != 0 || (tempX != 0 && tempY != 0)) {
					if (toroidalArray) {
						if (xValue == -1) xValue = width - 1;
						else if (xValue == width - 1) xValue = 0;
						
						if (yValue == -1) yValue = height - 1;
						else if (yValue == height - 1) yValue = 0;
					}
					
					for (Square s : array)
						if (s.getX() == xValue && s.getY() == yValue) 
							count++;
				}
			}
		}
		
		if      (count <  2 || count > 3) return false;
		else                              return true;
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
