
import java.awt.Color;
import java.awt.Graphics;

public class Cell {									/// the actual damn thing
	
	//All the information stored in a Cell
	
	private int myX, myY; // x,y position on grid
	private boolean myAlive; // alive (true) or dead (false)
	private int myNeighbors; // count of neighbors with respect to x,y
	private Color myColor; // Based on alive/dead rules
	private final Color DEFAULT_ALIVE = Color.ORANGE;
	private final Color DEFAULT_DEAD = Color.GRAY;

	public Cell(int x, int y) {
		this(x, y, false, Color.GRAY);
	}
	
	public Cell(int row, int col, boolean alive, Color color) {
		myAlive = alive;
		myColor = color;
		myX = col;
		myY = row;
	}

	public boolean getAlive() {		//keep
		return myAlive;
	}
	
	public int getX() {		//keep
		return myX;
	}
	
	public int getY() {		//keep
		return myY;
	}

	public void setAlive(boolean alive) {  //mine
		myAlive = alive;
		if (alive) 
			myColor = DEFAULT_ALIVE;
		else
			myColor = DEFAULT_DEAD;
	}

	public void setColor(Color color) {  //keep
		myColor = color;
	}

	public int getNeighbors() {   //keep
		return myNeighbors;
	}
	
	public void setNeighbors(int count) {
		myNeighbors = count;
	}
	
/*
	public void calcNeighbors(Cell[][] cell) { // fix this
		
		if (myAlive)
			myNeighbors = -1;
		else myNeighbors = 0;

		for (int a = -1; a < 2; a++) {                  // checks 3x3 grid of cells and adds alives to count, including itself
			
			int neighborX = (Display.ROWS + myX + a) % Display.ROWS;
			
			for (int b = -1; b < 2; b++) {				// GET THE RIGHT DAMN VARIABLE
				
				int neighborY = (Display.COLS + myY + b) % Display.COLS;
				
				// if neighbor to be checked is within bounds of grid

				if (cell[neighborX][neighborY].getAlive()) {    		// if alive, add 1 to counter
						myNeighbors++;
				}
					
			}
		}
		
		System.out.print(myNeighbors);
	}
	*/

	public void draw(int x_offset, int y_offset, int width, int height,          // draws the cell
			Graphics g) {
		// I leave this understanding to the reader
		int xleft = x_offset + 1 + (myX * (width + 1));
		int xright = x_offset + width + (myX * (width + 1));
		int ytop = y_offset + 1 + (myY * (height + 1));
		int ybottom = y_offset + height + (myY * (height + 1));
		Color temp = g.getColor();

		g.setColor(myColor);
		g.fillRect(xleft, ytop, width, height);
	}

}