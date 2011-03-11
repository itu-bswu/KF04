import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The frame that visualizes the roads (lines that are given), with controlls to the left.
 * 
 * @author Emil
 *
 */
public class View extends JFrame{
	
	// felter
	Canvas canvas;
	private JButton upButton;
	private JButton leftButton;
	private JButton downButton;
	private JButton rightButton;
	private JButton zoomInButton;
	private JButton zoomOutButton;

	/**
	 * Creates the frame with the given header title and an initial set of lines to be drawn.
	 * @param header The title for the frame.
	 * @param first_lines The initial lines to be shown.
	 */
	public View(String header, Line[] first_lines){
		super(header);
		canvas = new Canvas(first_lines);
		createContent();
		
		this.pack();
		this.setSize(300, 300);
		this.setVisible(true);
		System.out.println("finished setup");
	}
	
	/**
	 * Repaints the entire frame, with the new lines to be shown.
	 * @param l The new array of lines.
	 */
	public void repaint(Line[] l){
		canvas.updateLines(l);
	}

	// Filips
	private void createContent() {
		Container outer = this.getContentPane();
		outer.setLayout(new GridLayout(1,2));
		outer.add(canvas);
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(2,1));
		outer.add(menuPanel);
		JPanel navigationPanel = new JPanel();
		menuPanel.add(navigationPanel);
		navigationPanel.setLayout(new BorderLayout());
		upButton = new JButton("^");
		navigationPanel.add(upButton,BorderLayout.NORTH);
		leftButton = new JButton(">");
		navigationPanel.add(leftButton,BorderLayout.EAST);
		downButton = new JButton("v");
		navigationPanel.add(downButton,BorderLayout.SOUTH);
		rightButton = new JButton("<");
		navigationPanel.add(rightButton,BorderLayout.WEST);
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(2,1));
		zoomInButton = new JButton("+");
		centerPanel.add(zoomInButton);
		zoomOutButton = new JButton("-");
		centerPanel.add(zoomOutButton);
		navigationPanel.add(centerPanel,BorderLayout.CENTER);
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
			System.out.println("painting...");
			g.setColor(Color.BLACK);
			for(Line l : lines){
				// TODO Skrive kode for individuel farve
				g.drawLine((int)(l.getStartPoint().x*this.getWidth()), 
						(int)(l.getStartPoint().y*this.getHeight()),
						(int)(l.getEndPoint().x*this.getWidth()),
						(int)(l.getEndPoint().y*this.getHeight()));
			}
		}
	}
	
	/**
	 * Testing the View
	 * @param args
	 */
	public static void main(String[] args){
		new View("X marks the spot",new Line[]{new Line(new Point2D.Double(0.25,0.25),new Point2D.Double(0.75,0.75)),
				new Line(new Point2D.Double(0.75,0.25),new Point2D.Double(0.25,0.75))});
	}
}
