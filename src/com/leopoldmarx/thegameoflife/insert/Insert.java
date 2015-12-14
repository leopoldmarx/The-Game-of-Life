package com.leopoldmarx.thegameoflife.insert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import com.leopoldmarx.thegameoflife.grid.Grid;

public class Insert implements Serializable{

	private static final long serialVersionUID = 2414287690178719804L;
	
	private ArrayList<Grid> array = new ArrayList<>();
	
	public Insert() {
		array.clear();
		Collections.addAll(array,
				glider(),
				lightweightSpaceship(),
				gospersGliderGun(),
				blinker(),
				beehive(),
				toad(),
				beacon(),
				pulsar());
	}
	
	public void restoreDefaults() {
		array.clear();
		Collections.addAll(array,
				glider(),
				lightweightSpaceship(),
				gospersGliderGun(),
				blinker(),
				beehive(),
				toad(),
				beacon(),
				pulsar());
	}
	
	public void removeInsert(Grid grid) {
		array.remove(grid);
	}
	
	public void addInsert(Grid grid) {
		array.add(grid);
	}
	
	public Grid getInsert(int pos) {
		return array.get(pos);
	}

	public Grid glider() {
		Grid g = new Grid(3, 3);
		
		g.setName("Glider");
		
		g.addSquare(0, 0);
		g.addSquare(2, 0);
		g.addSquare(1, 1);
		g.addSquare(2, 1);
		g.addSquare(1, 2);
		
		return g;
	}
	
	public Grid lightweightSpaceship() {
		Grid g = new Grid(5, 4);
		
		g.setName("Lightweight Spaceship");
		
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
	
	public Grid gospersGliderGun() {
		Grid g = new Grid(36, 9);
		
		g.setName("Gosper's Glider Gun");
		
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
	
	public Grid blinker() {
		Grid g = new Grid (1, 3);
		
		g.setName("Blinker");
		
		g.addSquare(0, 0);
		g.addSquare(0, 1);
		g.addSquare(0, 2);
		
		return g;
	}
	
	public Grid beehive() {
		Grid g = new Grid(3, 4);
		
		g.setName("Beehive");
		
		g.addSquare(1, 0);
		g.addSquare(0, 1);
		g.addSquare(0, 2);
		g.addSquare(2, 1);
		g.addSquare(2, 2);
		g.addSquare(1, 3);
		
		return g;
	}
	
	public Grid toad() {
		Grid g = new Grid (4, 2);
		
		g.setName("Toad");
		
		g.addSquare(1, 0);
		g.addSquare(2, 0);
		g.addSquare(3, 0);
		
		g.addSquare(0, 1);
		g.addSquare(1, 1);
		g.addSquare(2, 1);
		
		return g;
	}
	
	public Grid beacon() {
		Grid g = new Grid(4, 4);
		
		g.setName("Beacon");
		
		g.addSquare(0, 0);
		g.addSquare(1, 0);
		g.addSquare(0, 1);
		
		g.addSquare(3, 2);
		g.addSquare(2, 3);
		g.addSquare(3, 3);
		
		return g;
	}
	
	public Grid pulsar() {
		//TODO Pulsar
		Grid g = new Grid();
		
		g.setName("Pulsar");
		
		return g;
	}
	
	public ArrayList<Grid> getArray() {
		return array;
	}

	public void setArray(ArrayList<Grid> array) {
		this.array = array;
	}
}
