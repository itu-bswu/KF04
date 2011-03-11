import java.awt.Container;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * The frame that visualizes the roads (lines that are given), with controlls to the left.
 * 
 * @author Emil
 *
 */
public class View extends JFrame{
	
	// felter
	Canvas canvas;

	/**
	 * Creates the frame with the given header title and an initial set of lines to be drawn.
	 * @param header The title for the frame.
	 * @param first_lines The initial lines to be shown.
	 */
	public View(String header, Line[] first_lines){
		createCanvas(first_lines);
		createContent();
		
		this.pack();
		this.setVisible(true);
	}
	
	/**
	 * Repaints the entire frame, with the new lines to be shown.
	 * @param l The new array of lines.
	 */
	public void repaint(Line[] l){
		canvas.updateLines(l);
	}

	// Emils
	private void createCanvas(Line[] l) {
		canvas = new Canvas(l);
	}

	// Filips
	private void createContent() {
		Container outer = this.getContentPane();
		
		// add components
	}
	
	/**
	 * The canvas that displays lines.
	 * @author Emil
	 *
	 */
	private class Canvas extends JComponent{
		private static final long serialVersionUID = 1L;

		private Line[] lines;
		
		public Canvas(Line[] lines){
			this.lines = lines;
		}

		public void updateLines(Line[] lines){
			this.lines = lines;
			this.repaint();
		}

		@Override
		public void paint(Graphics g){
			for(Line l : lines){
				// TODO Skrive kode for individuel farve
				g.drawLine((int)(l.getStartPoint().x+this.getWidth()), 
						(int)(l.getStartPoint().y+this.getHeight()),
						(int)(l.getEndPoint().x+this.getWidth()),
						(int)(l.getEndPoint().y+this.getHeight()));
			}
		}
	}
}
