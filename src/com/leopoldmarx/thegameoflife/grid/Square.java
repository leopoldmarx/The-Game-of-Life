package com.leopoldmarx.thegameoflife.grid;

public class Square {

	private int x;
	private int y;
	
	public Square(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj == this) return true;
		if (!(obj instanceof Square)) return false;
		
		Square s = (Square) obj;
		return Integer.compare(x, s.x) == 0
				&& Integer.compare(y, s.y) == 0;
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
