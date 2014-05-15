 import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

// Note that the JComponent is set up to listen for mouse clicks
// and mouse movement.  To achieve this, the MouseListener and
// MousMotionListener interfaces are implemented and there is additional
// code in init() to attach those interfaces to the JComponent.


public class Display extends JComponent implements MouseListener, MouseMotionListener {									/// the actual damn thing
	public static final int ROWS = 80;
	public static final int COLS = 100;
	
	public static Cell[][] cell = new Cell[ROWS][COLS];                 // array of cell objects in row*column (makes an array of cell class objects call "cell")
	private final int X_GRID_OFFSET = 10; // 10 pixels from left
	private final int Y_GRID_OFFSET = 10; // 10 pixels from top
	private final int CELL_WIDTH = 10;
	private final int CELL_HEIGHT = 10;

	// Note that a final field can be initialized in constructor
	private final int DISPLAY_WIDTH;   
	private final int DISPLAY_HEIGHT;
	private JButton startStop;
	private JButton clear;
	private JButton next;
	private JButton faster;
	private JButton slower;
	
	private boolean paintloop = false;
	private int TIME_BETWEEN_REPLOTS = 100;

	public Display(int width, int height) {
		DISPLAY_WIDTH = width;
		DISPLAY_HEIGHT = height;
		init();
	}


	public void init() {
		setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		initCells();

		addMouseListener(this);
		addMouseMotionListener(this);

		startStop = new StartButton();
		startStop.setBounds(1150, 150, 100, 36);
		add(startStop);
		startStop.setVisible(true);
		
		clear = new ClearButton();
		clear.setBounds(1150, 200, 100, 36);
		add(clear);
		clear.setVisible(true);
		
		next = new NextButton();
		next.setBounds(1150, 250, 100, 36);
		add(next);
		next.setVisible(true);
		
		faster = new FasterButton();
		faster.setBounds(1150, 350, 100, 36);
		add(faster);
		faster.setVisible(true);	
		
		slower = new SlowerButton();
		slower.setBounds(1150, 400, 100, 36);
		add(slower);
		slower.setVisible(true);
		
		repaint();
	}


	public void paintComponent(Graphics g) {

		g.setColor(Color.BLACK);
		drawGrid(g);
		drawCells(g);
		g.setColor(Color.BLACK);
		g.drawString("Milliseconds between ticks: " + TIME_BETWEEN_REPLOTS, 1150, 100);
	//	drawButtons();

		if (paintloop) {
			try {
				Thread.sleep(TIME_BETWEEN_REPLOTS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			nextGeneration();
			
		}
		repaint();
	}


	public void initCells() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				cell[row][col] = new Cell(row, col);
			}
		}
		
		cell[36][22].setAlive(true); // sample use of cell mutator method
		cell[36][23].setAlive(true); // sample use of cell mutator method
		cell[36][24].setAlive(true); // sample use of cell mutator method
		
		cell[2][1].setAlive(true); //diamond
		cell[1][2].setAlive(true);
		cell[2][3].setAlive(true);
		cell[3][2].setAlive(true);
		
		cell[5][6].setAlive(true); //blinker
		cell[5][7].setAlive(true);
		cell[5][8].setAlive(true);
		
		
		
		cell[7][1].setAlive(true); //box
		cell[7][2].setAlive(true);
		cell[8][1].setAlive(true);
		cell[8][2].setAlive(true);
		
		cell[11][2].setAlive(true); //glider
		cell[12][3].setAlive(true);
		cell[13][1].setAlive(true);
		cell[13][2].setAlive(true);
		cell[13][3].setAlive(true);
		
		cell[71][2].setAlive(true); //glider
		cell[72][3].setAlive(true);
		cell[73][1].setAlive(true);
		cell[73][2].setAlive(true);
		cell[73][3].setAlive(true);
	}


	public void togglePaintLoop() {
		paintloop = !paintloop;
	}


	public void setPaintLoop(boolean value) {
		paintloop = value;
	}


	void drawGrid(Graphics g) {                               // draws the display grid
		for (int row = 0; row <= ROWS; row++) {
			g.drawLine(X_GRID_OFFSET,
					Y_GRID_OFFSET + (row * (CELL_HEIGHT + 1)), X_GRID_OFFSET
					+ COLS * (CELL_WIDTH + 1), Y_GRID_OFFSET
					+ (row * (CELL_HEIGHT + 1)));
		}
		for (int col = 0; col <= COLS; col++) {
			g.drawLine(X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET,
					X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET
					+ ROWS * (CELL_HEIGHT + 1));
		}
	}

	
	void drawCells(Graphics g) {
		// Have each cell draw itself
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				// The cell cannot know for certain the offsets nor the height
				// and width; it has been set up to know its own position, so
				// that need not be passed as an argument to the draw method
				cell[row][col].draw(X_GRID_OFFSET, Y_GRID_OFFSET, CELL_WIDTH,
						CELL_HEIGHT, g);
			}
		}
	}
	

	private void nextGeneration() {
		
		calcNeighbors(cell);
		
		for (int i = 0; i < cell.length; i++) {                
			for (int j = 0; j < cell[i].length; j++) {
				
				if (cell[i][j].getAlive()) {
					if (cell[i][j].getNeighbors() == 2 || cell[i][j].getNeighbors() == 3)
						cell[i][j].setAlive(true);
					else cell[i][j].setAlive(false);
				}
		
				else if (cell[i][j].getNeighbors() == 3)
					cell[i][j].setAlive(true);
			}
		}
		repaint();
	}


	public void calcNeighborsNoWRAP(Cell[][] cell) {			// checks neighbors w/o wrap
		
		for (int i = 0; i < cell.length; i++) {                
			for (int j = 0; j < cell[i].length; j++) {
				
				if (cell[i][j].getAlive())
					cell[i][j].setNeighbors(-1);				// if cell is alive, subtract one from count b/c it will be counted later
				else cell[i][j].setNeighbors(0);
				
				for (int a = -1; a < 2; a++) {                  // checks 3x3 neighbors
					for (int b = -1; b < 2; b++) {
						
						int neighborX = i + a;
						int neighborY = j + b;
						
						// if neighbor to be checked is within bounds of array
						if (0 <= neighborX && neighborX < cell.length && 0 <= neighborY && neighborY < cell[i].length) {
							
							if (cell[neighborX][neighborY].getAlive()) {    				// if alive, add 1 to counter
								cell[i][j].setNeighbors(cell[i][j].getNeighbors() + 1);
							}	
						}
					}
				}
			}
		}
	}
	
	public void calcNeighbors(Cell[][] cell) {  				// checks neighbors WITH WRAP
		
		for (int i = 0; i < cell.length; i++) {
			for (int j = 0; j < cell[i].length; j++) {
				
				
				if (cell[i][j].getAlive())
					cell[i][j].setNeighbors(-1);				// if cell is alive, subtract one from count b/c it will be counted later
				else cell[i][j].setNeighbors(0);
				
				for (int a = -1; a < 2; a++) {                  // checks 3x3 neightbors
					
					int neighborX = (COLS + cell[i][j].getX() + a) % COLS;
					
					for (int b = -1; b < 2; b++) {
						
						int neighborY = (ROWS + cell[i][j].getY() + b) % ROWS;
						if (cell[neighborY][neighborX].getAlive()) {    		// if alive, add 1 to counter
							cell[i][j].setNeighbors(cell[i][j].getNeighbors() + 1);
						}
							
					}
				}
			}
		}
	}
	
	public void clearCells(Cell[][] cell) {
		
		for (int i = 0; i < cell.length; i++) {                
			for (int j = 0; j < cell[i].length; j++) {			// cycles through the entire array of cells
				
				cell[i][j].setAlive(false);
			}
		}
		repaint();
		
	}


	public void mouseClicked(MouseEvent arg0) {  //
		int x = arg0.getX();
		int y = arg0.getY();
		
		x = (int) (x - X_GRID_OFFSET - 1)/(CELL_WIDTH + 1);					// coordinates in cell[][]
		y = (int) (y - Y_GRID_OFFSET - 2)/(CELL_HEIGHT + 1);
		
		if (x > -1 && x < COLS && y > -1 && y < ROWS){
			if (cell[y][x].getAlive())
				cell[y][x].setAlive(false);
			else
				cell[y][x].setAlive(true);
				
		}
		
		repaint();
	}


	public void mouseEntered(MouseEvent arg0) {

	}


	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {

	}


	public void mouseReleased(MouseEvent arg0) {
		
	}


	public void mouseDragged(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		
		x = (int) (x - X_GRID_OFFSET - 1)/(CELL_WIDTH + 1);					// coordinates in cell[][]
		y = (int) (y - Y_GRID_OFFSET - 2)/(CELL_HEIGHT + 1);
		
		if (x > -1 && x < COLS && y > -1 && y < ROWS){
				cell[y][x].setAlive(true);
		}
		
		repaint();

	}


	public void mouseMoved(MouseEvent arg0) {
		
	}
	

	private class StartButton extends JButton implements ActionListener {
		StartButton() {
			super("Start");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {

			if (this.getText().equals("Start")) {
				togglePaintLoop();
				setText("Stop");
				nextGeneration();
			} else {
				togglePaintLoop();
				setText("Start");
			}
			repaint();
		}
	}
	
	private class ClearButton extends JButton implements ActionListener {
		ClearButton() {
			super("Clear");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {

			
			if (startStop.getText().equals("Stop")) {
			togglePaintLoop();	
			startStop.setText("Start");
			}

			clearCells(cell);
			repaint();
		}
	}
	
	private class NextButton extends JButton implements ActionListener {
		NextButton() {
			super("Next");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {

			if (startStop.getText().equals("Start")) {
				nextGeneration();
				repaint();
			}
		}
	}
	
	private class FasterButton extends JButton implements ActionListener {
		FasterButton() {
			super("Faster");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {

			if(TIME_BETWEEN_REPLOTS - 10 > 0){
				TIME_BETWEEN_REPLOTS -= 10;
			}
		}
	}
	
	private class SlowerButton extends JButton implements ActionListener {
		SlowerButton() {
			super("Slower");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {

			TIME_BETWEEN_REPLOTS += 10;
		}
	}
	
}
