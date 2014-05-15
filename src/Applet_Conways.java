import javax.swing.JApplet;


public class Applet_Conways extends JApplet {

	private static final long serialVersionUID = -8966304394176505079L;

	public Applet_Conways() {
		
    }
	
	public void init(){
		final int DISPLAY_WIDTH = 1350;
		final int DISPLAY_HEIGHT = 950;
		
		setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		
        add(new Display(DISPLAY_WIDTH, DISPLAY_HEIGHT)); 
	}
}