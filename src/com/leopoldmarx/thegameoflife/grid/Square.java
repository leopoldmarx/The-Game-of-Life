package com.leopoldmarx.thegameoflife.grid;

/**
 * Stores x and y values in a position of a grid
 * 
 * @author Leopold Marx
 */
public class Square {

	private int x;
	private int y;
	
	public Square(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int xPosition) {
		this.x = xPosition;
	}

	public int getY() {
		return y;
	}

	public void setY(int yPosition) {
		this.y = yPosition;
	}
}
