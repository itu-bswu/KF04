import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
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
	
	public static final double WINDOW_RATIO = 4./3;
	public static final int START_WIDTH = 400;
	
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
	public View(String header, Collection<Line> first_lines){
		super(header);
		canvas = new Canvas(first_lines);
		createContent();
		setResizeListener();
		
		this.pack();
		this.setSize(START_WIDTH,(int) (START_WIDTH/WINDOW_RATIO));
		this.setVisible(true);
		System.out.println("finished setup");
	}
	
	private void setResizeListener() {
		this.addComponentListener(new ComponentListener(){
			
			private int last_width = START_WIDTH;
			private int last_height = (int)(START_WIDTH/WINDOW_RATIO);

			@Override
			public void componentHidden(ComponentEvent arg0) {}
			@Override
			public void componentMoved(ComponentEvent arg0) {}
			@Override
			public void componentShown(ComponentEvent arg0) {}
			
			@Override
			public void componentResized(ComponentEvent e) {
				Dimension new_dim = getSize();
				if(new_dim.width != last_width){
					setSize(new_dim.width,(int)(new_dim.width/WINDOW_RATIO));
				}else if(new_dim.height != last_height){
					setSize((int)(new_dim.height*WINDOW_RATIO),new_dim.height);
				}
				last_width = new_dim.width;
				last_height = new_dim.height;
			}
		});
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
	 * @param l The new Collection of lines.
	 */
	public void repaint(Collection<Line> l){
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
		
		upButton = new JButton("^");
		leftButton = new JButton("<");
		downButton = new JButton("v");
		rightButton = new JButton(">");
		zoomInButton = new JButton("+");
		zoomOutButton = new JButton("-");
		
		// layouts & borders
		outer.setLayout(new BorderLayout());
		navigationPanel.setLayout(new GridBagLayout());
		//navigationPanel.setMinimumSize(new Dimension(200,200));
		menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		// adding structure
		outer.add(canvas,BorderLayout.CENTER);
		outer.add(menuPanel,BorderLayout.WEST);
		menuPanel.add(navigationPanel);
		
		setupNavigation(navigationPanel);
	}
	
	private void setupNavigation(JPanel nav) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 2;
		nav.add(upButton,c);
		
		c.gridy = 4;
		nav.add(downButton,c);
		
		c.weightx = -1;
		c.gridx = 0;
		c.gridy = 2;
		nav.add(leftButton,c);
		
		c.gridx = 2;
		nav.add(rightButton,c);
		
		c.weightx = 0;
		c.gridheight = 1;
		c.gridx = 1;
		nav.add(zoomInButton,c);
		
		c.gridy = 3;
		nav.add(zoomOutButton,c);
	}

	/**
	 * The canvas that displays lines.
	 * @author Emil
	 *
	 */
	private class Canvas extends JComponent{
		private static final long serialVersionUID = 1L;

		private Collection<Line> lines;
		
		public Canvas(Collection<Line> first_lines){
			this.lines = first_lines;
		}

		public void updateLines(Collection<Line> lines){
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
		Collection<Line> x = new HashSet<Line>();
		x.add(new Line(new Point2D.Double(0.25,0.25),new Point2D.Double(0.75,0.75)));
		x.add(new Line(new Point2D.Double(0.75,0.25),new Point2D.Double(0.25,0.75)));
		
		new View("X marks the spot",x);
	}

}
