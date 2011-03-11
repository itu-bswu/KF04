import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JFrame;


public class View extends JFrame{
	
	// felter
	JComponent canvas;

	public View(String header){
		createCanvas();
		createContent();
		
		this.pack();
		this.setVisible(true);
	}

	// Emils
	private void createCanvas() {
		// TODO Auto-generated method stub
		
	}

	// Filips
	private void createContent() {
		Container outer = this.getContentPane();
		
		// add components
	}
}
