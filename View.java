import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
/**
 * The frame that visualizes the roads (lines that are given), with controlls to the left.
 * 
 * @author Emil
 *
 */
public class View extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
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
	
	public void addUpListener(ActionListener actionListener) {
		upButton.addActionListener(actionListener);
	}
	
	public void addDownListener(ActionListener actionListener) {
		downButton.addActionListener(actionListener);
	}
	
	public void addLeftListener(ActionListener actionListener) {
		leftButton.addActionListener(actionListener);
	}
	
	public void addRightListener(ActionListener actionListener) {
		rightButton.addActionListener(actionListener);
	}
	
	public void addInListener(ActionListener actionListener) {
		zoomInButton.addActionListener(actionListener);
	}
	
	public void addOutListener(ActionListener actionListener) {
		zoomOutButton.addActionListener(actionListener);
	}

	/**
	 * Repaints the entire frame, with the new lines to be shown.
	 * @param l The new array of lines.
	 */
	public void repaint(Line[] l){
		canvas.updateLines(l);
	}
	
	/**
	 * Tells the ratio of the windows resolution.
	 * @return The screen's width divided by the screen's height.
	 */
	public double getRatio(){
		return this.getWidth()/this.getHeight();
	}

	// Filips
	private void createContent() {
		// creating objects
		Container outer = this.getContentPane();
		JPanel menuPanel = new JPanel();
		JPanel navigationPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		
		upButton = new JButton("^");
		leftButton = new JButton(">");
		downButton = new JButton("v");
		rightButton = new JButton("<");
		zoomInButton = new JButton("+");
		zoomOutButton = new JButton("-");
		
		// layouts & borders
		outer.setLayout(new BorderLayout());
		navigationPanel.setLayout(new BorderLayout());
		navigationPanel.setMaximumSize(new Dimension(150,150));
		centerPanel.setLayout(new GridLayout(2,1));
		
		// adding structure
		outer.add(canvas,BorderLayout.CENTER);
		outer.add(menuPanel,BorderLayout.WEST);
		menuPanel.add(navigationPanel);
		
		navigationPanel.add(upButton,BorderLayout.NORTH);
		navigationPanel.add(leftButton,BorderLayout.EAST);
		navigationPanel.add(downButton,BorderLayout.SOUTH);
		navigationPanel.add(rightButton,BorderLayout.WEST);
		centerPanel.add(zoomInButton);
		centerPanel.add(zoomOutButton);
		navigationPanel.add(centerPanel,BorderLayout.CENTER);
		
		// Boxlayout is a bitch and needs to go last
		menuPanel.setLayout(new BoxLayout(menuPanel,BoxLayout.Y_AXIS));
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
