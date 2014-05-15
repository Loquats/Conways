import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class Main {

	public static void main(String[] args) {
		// Bring up a JFrame with squares to represent the cells
		
		final int DISPLAY_WIDTH = 1350;
		final int DISPLAY_HEIGHT = 950;
		
		JFrame f = new JFrame();
		f.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		
		Display display = new Display(DISPLAY_WIDTH, DISPLAY_HEIGHT);   // uses Display class
		JScrollPane p = new JScrollPane();
		
		f.setLayout(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("Conway's Game of Life");
		f.add(display);    // displays Display class
		f.add(p);
		f.setVisible(true);

	}
}