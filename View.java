import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
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

	public static final int START_WIDTH = 400;

	private static final long serialVersionUID = 1L;

	// felter
	private Canvas canvas;
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
	public View(String header, Collection<Line> first_lines, double startRatio){
		super(header);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		canvas = new Canvas(first_lines);
		
		createContent();
		//setResizeListener();
		canvasListener();

		this.setSize(800,600);
		this.setVisible(true);
		canvas.setSize(new Dimension(canvas.getWidth(), (int)(canvas.getWidth()/startRatio)));
		
		System.out.println("finished view setup");
	}

	private void canvasListener() {
		MouseAdapter m = new MouseAdapter(){

			private Point start = null;
			private BufferedImage img = null;

			@Override
			public void mousePressed(MouseEvent e){
				start = e.getPoint();
				img = new BufferedImage(canvas.getWidth(),canvas.getHeight(),BufferedImage.TYPE_INT_RGB);
				Graphics g = img.getGraphics();
				g.setColor(canvas.getBackground());
				g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
				canvas.paint(g);
				g.dispose();
			}

			@Override
			public void mouseDragged(MouseEvent e){
				Point end = e.getPoint();

				Graphics g = canvas.getGraphics();
				g.drawImage(img, 0, 0, null);
				// draw the rectangle the right way (else it will be filled)
				if(start.x < end.x && start.y < end.y){
					// pulled right down
					g.drawRect(start.x, start.y, end.x - start.x, end.y - start.y);
				}else if(start.x < end.x && start.y > end.y){
					// pulled right up
					g.drawRect(start.x, end.y, end.x - start.x,start.y - end.y);
				}else if(start.x > end.x && start.y < end.y){
					// pulled left down
					g.drawRect(end.x, start.y, start.x - end.x, end.y - start.y);
				}else{
					// pullef left up
					g.drawRect(end.x, end.y, start.x - end.x, start.y - end.y);
				}

				g.dispose();
			}

			@Override
			public void mouseReleased(MouseEvent e){
				Graphics g = canvas.getGraphics();
				g.drawImage(img, 0, 0, null);
				start = null;
				img = null;
				g.dispose();
			}
		};

		this.addCanvasMouseListener(m);
		canvas.addMouseMotionListener(m);
	}

//	private void setResizeListener() {
//		this.addComponentListener(new ComponentListener(){
//
//			private int last_width = START_WIDTH;
//			private int last_height = (int)(START_WIDTH/WINDOW_RATIO);
//
//			@Override
//			public void componentHidden(ComponentEvent arg0) {}
//			@Override
//			public void componentMoved(ComponentEvent arg0) {}
//			@Override
//			public void componentShown(ComponentEvent arg0) {}
//
//			@Override
//			public void componentResized(ComponentEvent e) {
//				Dimension new_dim = getSize();
//				if(new_dim.width != last_width){
//					setSize(new_dim.width,(int)(new_dim.width/WINDOW_RATIO));
//				}else if(new_dim.height != last_height){
//					setSize((int)(new_dim.height*WINDOW_RATIO),new_dim.height);
//				}
//				last_width = new_dim.width;
//				last_height = new_dim.height;
//			}
//		});
//	}

	/**
	 * Adds an ActionListener to the Up-button in the navigation-panel.
	 * @param actionListener The ActionListener for the button.
	 */
	public void addUpListener(ActionListener actionListener) {
		upButton.addActionListener(actionListener);
	}

	/**
	 * Adds an ActionListener to the Down-button in the navigation-panel.
	 * @param actionListener The ActionListener for the button.
	 */
	public void addDownListener(ActionListener actionListener) {
		downButton.addActionListener(actionListener);
	}

	/**
	 * Adds an ActionListener to the Left-button in the navigation-panel.
	 * @param actionListener The ActionListener for the button.
	 */
	public void addLeftListener(ActionListener actionListener) {
		leftButton.addActionListener(actionListener);
	}

	/**
	 * Adds an ActionListener to the Right-button in the navigation-panel.
	 * @param actionListener The ActionListener for the button.
	 */
	public void addRightListener(ActionListener actionListener) {
		rightButton.addActionListener(actionListener);
	}

	/**
	 * Adds an ActionListener to the ZoomIn-button in the navigation-panel.
	 * @param actionListener The ActionListener for the button.
	 */
	public void addInListener(ActionListener actionListener) {
		zoomInButton.addActionListener(actionListener);
	}

	/**
	 * Adds an ActionListener to the ZoomOut-button in the navigation-panel.
	 * @param actionListener The ActionListener for the button.
	 */
	public void addOutListener(ActionListener actionListener) {
		zoomOutButton.addActionListener(actionListener);
	}

	/**
	 * Adds a MouseListener to the canvas component.
	 * @param m The MouseListener for the canvas.
	 */
	public void addCanvasMouseListener(MouseListener m){
		canvas.addMouseListener(m);
	}
	
	/**
	 * Adds a ComponentListener to the canvas component. This is to get events
	 * from resizing the window and such.
	 * @param c The ComponentListener for the canvas.
	 */
	public void addCanvasComponentListener(ComponentListener c){
		canvas.addComponentListener(c);
	}

	/**
	 * Repaints the entire frame, with the new lines to be shown.
	 * @param l The new Collection of lines.
	 */
	public void repaint(Collection<Line> l){
		canvas.updateLines(l);
	}

	/**
	 * Tells the width of the canvas component in pixel.
	 * @return the width in pixel
	 */
	public int getCanvasWidth(){
		return canvas.getWidth();
	}

	/**
	 * Tells the height of the canvas component in pixel.
	 * @return the height in pixel
	 */
	public int getCanvasHeight(){
		return canvas.getHeight();
	}

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
			for(Line l : lines){
				g.setColor(l.getRoadColor());
				g.drawLine((int)(l.getStartPoint().x*this.getWidth()), 
						(int)(l.getStartPoint().y*this.getHeight()),
						(int)(l.getEndPoint().x*this.getWidth()),
						(int)(l.getEndPoint().y*this.getHeight()));
			}
			g.dispose();
		}
	}

	/**
	 * Testing the View
	 * @param args
	 */
	public static void main(String[] args){
		Collection<Line> x = new HashSet<Line>();
		x.add(new Line(new Point2D.Double(0.25,0.25),new Point2D.Double(0.75,0.75),Color.BLACK));
		x.add(new Line(new Point2D.Double(0.75,0.25),new Point2D.Double(0.25,0.75),Color.BLACK));

		new View("X marks the spot",x,1.0);
	}
}
