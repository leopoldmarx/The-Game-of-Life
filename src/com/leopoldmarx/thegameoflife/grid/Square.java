package com.leopoldmarx.thegameoflife.grid;

/**
 * Stores x and y values in a position of a grid
 * 
 * @author Leopold Marx
 */
public class Square implements Comparable<Square> {

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

	@Override
	public int compareTo(Square s) {
		if (this.getX() < s.getX())
			return -1;
		else if (this.getX() > s.getX())
			return 1;
		else {
			if (this.getY() < s.getY())
				return -1;
			else if (this.getY() > s.getY())
				return 1;
			else return 0;
		}
	}
}
