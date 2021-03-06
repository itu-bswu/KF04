package gui;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

/**
 * View
 * Acts as a view in the Model-View-Controller (MVC) architectural pattern, 
 * responsible for the frame that visualizes the roads (Lines that are given),
 * with controls shown as a panel to the left.
 * @author Emil Juul Jacobsen
 */
public class View extends JFrame{

	public static final int START_WIDTH = 400;

	private static final long serialVersionUID = 1L;

	private Canvas canvas;
	private JLabel infobar;
	
	// navigation
	private JButton upButton;
	private JButton leftButton;
	private JButton downButton;
	private JButton rightButton;
	private JButton zoomInButton;
	private JButton zoomOutButton;

	private JButton clearMarkersButton;
	private JRadioButton carChoice;
	private JRadioButton bikeChoice;

	// names of stats
	private JLabel routeTotalDistLabel;
	private JLabel routeTimeLabel;
	
	// values of stats
	private JLabel routeTotalDistValue;
	private JLabel routeTimeValue;

	/**
	 * Creates the frame with the given header title and the initially wanted ratio for the canvas.
	 * @param header The title for the frame.
	 * @param startRatio The initial ratio of the canvas component.
	 */
	public View(String header, double startRatio){

		super(header);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		canvas = new Canvas();

		createContent();
		canvasListener();

		this.setSize(800,600);
		this.setVisible(true);

		canvas.setPreferredSize(new Dimension(canvas.getWidth(), (int)(canvas.getWidth()/startRatio)));
		pack();
	}

	/**
	 * The private MouseListener that draws a rectangle around a marked area of the map.
	 */
	private void canvasListener() {
		MouseAdapter m = new MouseAdapter(){

			private Point start = null;

			@Override
			public void mousePressed(MouseEvent e){
				start = e.getPoint();
			}

			@Override
			public void mouseDragged(MouseEvent e){
				Point end = e.getPoint();
				Graphics2D g = (Graphics2D) canvas.getGraphics();
				
				// Remove any previously drawn rectangles
				g.drawImage(canvas.getImage(), 0, 0, null);

				// draw the rectangle the right way (if not done this way it will be filled)
				if(start.x < end.x && start.y < end.y){
					// pulled right down
					g.setColor(new Color(0,0,1,0.33f));
					g.fillRect(start.x, start.y, end.x - start.x, end.y - start.y);
					g.setColor(Color.BLUE);
					g.drawRect(start.x, start.y, end.x - start.x, end.y - start.y);
				}else if(start.x < end.x && start.y > end.y){
					// pulled right up
					g.setColor(new Color(0,0,1,0.33f));
					g.fillRect(start.x, end.y, end.x - start.x,start.y - end.y);
					g.setColor(Color.BLUE);
					g.drawRect(start.x, end.y, end.x - start.x,start.y - end.y);
				}else if(start.x > end.x && start.y < end.y){
					// pulled left down
					g.setColor(new Color(0,0,1,0.33f));
					g.fillRect(end.x, start.y, start.x - end.x, end.y - start.y);
					g.setColor(Color.BLUE);
					g.drawRect(end.x, start.y, start.x - end.x, end.y - start.y);
				}else{
					// pulled left up
					g.setColor(new Color(0,0,1,0.33f));
					g.fillRect(end.x, end.y, start.x - end.x, start.y - end.y);
					g.setColor(Color.BLUE);
					g.drawRect(end.x, end.y, start.x - end.x, start.y - end.y);
				}

				g.dispose();
			}

			@Override
			public void mouseReleased(MouseEvent e){
				// repaint without any rectangles
				canvas.repaint();
				start = null;
			}
		};

		this.addCanvasMouseListener(m);
		canvas.addMouseMotionListener(m);
	}

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
	 * Adds an ActionListener to the Clear Markers-button in the Route Options-panel.
	 * @param a
	 */
	public void addClearMarkersListener(ActionListener a){
		clearMarkersButton.addActionListener(a);
	}

	/**
	 * Adds an ActionListener to all the route mode RadioButtons.
	 * @param a 
	 */
	public void addRouteModeListener(ActionListener a){
		carChoice.addActionListener(a);
		bikeChoice.addActionListener(a);
	}

	/**
	 * Adds a MouseListener to the canvas component.
	 * @param m The MouseListener for the canvas.
	 */
	public void addCanvasMouseListener(MouseAdapter m){
		canvas.addMouseListener(m);
		canvas.addMouseMotionListener(m);
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
	 * Adds a KeyListener to all focusable components in the view, this
	 * ensures that we receive info when a key is pressed no matter what
	 * component is focused at that time.
	 */
	@Override
	public void addKeyListener(KeyListener k){
		super.addKeyListener(k);
		canvas.addKeyListener(k);
		upButton.addKeyListener(k);
		leftButton.addKeyListener(k);
		downButton.addKeyListener(k);
		rightButton.addKeyListener(k);
		zoomInButton.addKeyListener(k);
		zoomOutButton.addKeyListener(k);
		clearMarkersButton.addKeyListener(k);
		carChoice.addKeyListener(k);
		bikeChoice.addKeyListener(k);
	}
	
	/**
	 * Tells if the Car Choice is selected.
	 * @return true if selected.
	 */
	public boolean isCarChoiceSelected(){
		return carChoice.isSelected();
	}

	/**
	 * Tells if the Bike Choice is selected.
	 * @return true if selected.
	 */
	public boolean isBikeChoiceSelected(){
		return bikeChoice.isSelected();
	}

	/**
	 * Adds a pin to the canvas, a pin will be displayed at this position
	 * of the screen until clearMarks() is called.
	 * @param p The point in pixel to display a pin.
	 */
	public void addPin(Point p){
		canvas.addPin(p);
	}

	/**
	 * Removes all pins currently in place.
	 */
	public void clearMarkers(){
		canvas.clearMarkers();
	}

	/**
	 * Adds a separete collection of lines to be displayed along with
	 * the regular roads.
	 * @param route
	 */
	public void addRoute(Collection<Line> route){
		canvas.updateRoute(route);
	}

	/**
	 * Removes any route currently stored.
	 */
	public void clearRoute(){
		canvas.updateRoute(null);
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

	/**
	 * Changes the text at the bottom of the window.
	 * @param text The new text
	 */
	public void setLabel(String text){
		infobar.setText(text);
	}

	/**
	 * Displays a dialog on top of the view to display a given message.
	 * @param message The message to display at the center of the dialog.
	 * @param header The text to display as the header of the dialog.
	 */
	public void displayDialog(String message, String header){
		JOptionPane.showMessageDialog(this, message, header, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Updates the route statistics on the screen.
	 * @param routeDistance The total length of the route (in kilometers)
	 * @param routeTime The total time to travel the route (in hours)
	 */
	public void setRouteInfo(double routeDistance, double routeTime) {
		routeTotalDistValue.setText(String.format("%.1f km", routeDistance));

		// displaying the travel time with right precision (routeTime is given as minutes)
		if(routeTime > 60){
			routeTimeValue.setText(String.format("%.0f h %.0f min", routeTime / 60,routeTime % 60));
		}else{
			routeTimeValue.setText(String.format("%.0f minutes", routeTime));
		}
	}

	/**
	 * Creates the tree of components that the window consists of. This includes the different buttons and labels
	 * around.
	 */
	private void createContent() {
		// creating objects
		Container outer = this.getContentPane();
		JPanel menuPanel = new JPanel();
		JPanel navigationPanel = new JPanel();
		JPanel routeOptions = new JPanel();
		JPanel routeInfo = new JPanel();
		JPanel routeInfoGrid = new JPanel();

		upButton = new JButton("^");
		leftButton = new JButton("<");
		downButton = new JButton("v");
		rightButton = new JButton(">");
		zoomInButton = new JButton("+");
		zoomOutButton = new JButton("-");

		clearMarkersButton = new JButton("Clear Markers");
		carChoice = new JRadioButton("Car");
		bikeChoice = new JRadioButton("Bike");

		infobar = new JLabel(" ",SwingConstants.CENTER);

		// labels for route info
		routeTotalDistLabel	= new JLabel("Total Distance: ");
		routeTimeLabel 		= new JLabel("Travel Time: ");

		routeTotalDistValue = new JLabel();
		routeTimeValue		= new JLabel();

		// layouts & borders
		outer.setLayout(new BorderLayout());
		navigationPanel.setLayout(new GridBagLayout());
		menuPanel.setLayout(new GridBagLayout());
		navigationPanel.setBorder(BorderFactory.createTitledBorder("Navigation"));
		routeOptions.setBorder(BorderFactory.createTitledBorder("Route Options"));
		routeInfo.setBorder(BorderFactory.createTitledBorder("Route Information"));
		routeInfoGrid.setLayout(new GridLayout(0,2));

		carChoice.setSelected(true);
		ButtonGroup bg = new ButtonGroup();
		bg.add(carChoice);
		bg.add(bikeChoice);

		routeTotalDistLabel.setForeground(Color.GRAY);
		routeTimeLabel.setForeground(Color.GRAY);

		routeTotalDistValue.setHorizontalAlignment(JLabel.RIGHT);
		routeTimeValue.setHorizontalAlignment(JLabel.RIGHT);

		// adding structure
		outer.add(canvas,BorderLayout.CENTER);
		outer.add(menuPanel,BorderLayout.WEST);
		outer.add(infobar,BorderLayout.SOUTH);

		routeOptions.add(clearMarkersButton);
		routeOptions.add(carChoice);
		routeOptions.add(bikeChoice);

		routeInfo.add(routeInfoGrid);
		routeInfoGrid.add(routeTotalDistLabel);
		routeInfoGrid.add(routeTotalDistValue);
		routeInfoGrid.add(routeTimeLabel);
		routeInfoGrid.add(routeTimeValue);

		// gridbag adding for menuPanel
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		menuPanel.add(navigationPanel,c);

		c.gridy = 1;
		menuPanel.add(routeOptions,c);

		c.weighty = 1;
		c.gridy = 2;
		menuPanel.add(routeInfo,c);

		// nested gridbag for the navigation buttons
		setupNavigation(navigationPanel);
	}

	/**
	 * Creates the complicated navigation panel, this is done using a GridBagLayout.
	 * @param nav The panel to make the buttons in.
	 */
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
	 * Canvas
	 * The canvas that displays the map visualization itself (and pins & routes)
	 * @author Emil
	 */
	private static class Canvas extends JComponent{
		private static final long serialVersionUID = 1L;

		private BufferedImage pin_img;
		private BufferedImage img = null;
		private Collection<Line> route = null;
		private List<Point> pins = new ArrayList<Point>();

		public Canvas(){
			try{
				pin_img = ImageIO.read(new File("src","gfx/pin.png"));
			}catch(IOException e){
				e.printStackTrace();
			}
			makeAlpha(pin_img,Color.WHITE);
		}

		private void makeAlpha(BufferedImage img, Color color) {
			for(int i = 0; i < img.getHeight(); i++) {  
				for(int j = 0; j < img.getWidth(); j++) {  
					if(img.getRGB(j, i) == color.getRGB()) {  
						img.setRGB(j, i, 0x8F1C1C);
					}  
				}  
			}  
		}

		/**
		 * Gives the off-screen Image that is contained within the canvas.
		 * @return The off-screen Image.
		 */
		public Image getImage(){
			return img;
		}

		/**
		 * Removes any stored pins.
		 */
		public void clearMarkers() {
			pins.clear();
		}

		/**
		 * Adds the given pin to the collection.
		 * @param p A point where the pin should be placed.
		 */
		public void addPin(Point p) {
			pins.add(p);
		}

		/**
		 * Updates the canvas with a new Set of Lines and repaints using them.
		 * @param lines The Set of Lines to be drawn.
		 */
		public void updateLines(Collection<Line> lines){
			drawOffScreen(lines);
			this.repaint();
		}

		/**
		 * Updates the stored route with the given.
		 * @param route The new route.
		 */
		public void updateRoute(Collection<Line> route){
			this.route = route;
		}

		/**
		 * Draws the given Lines on the off-screen image for later display on the canvas.
		 * @param lines The lines that should be visible.
		 */
		public void drawOffScreen(Collection<Line> lines){
			if(getWidth() > 0 && getHeight() > 0){
				img = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
				Graphics2D g = (Graphics2D) img.getGraphics();

				// draw background
				g.setColor(getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());

				// Anti-aliasing
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// draw lines
				drawLines(g,lines,0.0,true);
				drawLines(g,lines,-1.0,false);

				// draw the route
				if(route != null){
					drawLines(g,route,-1.0,false);
				}

				// draw pins and write their number
				for(int index = 0 ; index < pins.size() ; index++){
					g.setFont(new Font("Arial", Font.BOLD, 16));
					g.setColor(Color.BLACK);
					g.drawImage(pin_img, pins.get(index).x - pin_img.getWidth()/2-2, pins.get(index).y - pin_img.getHeight()-3, null);
					String number = String.valueOf(index+1);
					g.drawString(number, pins.get(index).x-6-(number.length()-1)*4, pins.get(index).y-14);
				}

				g.dispose();
			}
		}
		
		private void drawLines(Graphics2D g, Collection<Line> lines, double ThicknessAddition, boolean darker) {			
			for(Line l : lines){
				if(darker){
					g.setColor(l.getLineColor().darker().darker());
				}
				else{
					g.setColor(l.getLineColor());
				}
				
				g.setStroke(new BasicStroke((float) (l.getSize()*Math.max(1.0,l.getThickness()*this.getWidth() + ThicknessAddition)),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
				g.drawLine((int)(l.getStartPoint().x*this.getWidth()), 
						(int)(l.getStartPoint().y*this.getHeight()),
						(int)(l.getEndPoint().x*this.getWidth()),
						(int)(l.getEndPoint().y*this.getHeight()));
			}
		}

		/**
		 * Paints the off-screen Image on the Canvas.
		 */
		@Override
		public void paint(Graphics g){
			if(img != null){
				g.drawImage(img, 0, 0, null);
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
		x.add(new Line(new Point2D.Double(0.25,0.25),new Point2D.Double(0.75,0.75),Color.YELLOW,0.01,1));
		x.add(new Line(new Point2D.Double(0.75,0.25),new Point2D.Double(0.25,0.75),Color.BLUE,0.02,1));

		final View v = new View("X marks the spot", 1.0);

		v.addPin(new Point((int)(0.25*v.getCanvasWidth()),(int)(0.25*v.getCanvasHeight())));
		v.addPin(new Point((int)(0.75*v.getCanvasWidth()),(int)(0.75*v.getCanvasHeight())));
		v.addPin(new Point((int)(0.75*v.getCanvasWidth()),(int)(0.25*v.getCanvasHeight())));
		v.addPin(new Point((int)(0.25*v.getCanvasWidth()),(int)(0.75*v.getCanvasHeight())));

		v.repaint(x);

		// testing dialog message
		v.addCanvasMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				v.displayDialog("x="+e.getX()+", y="+e.getY(), "Mouse Clicked!");
			}
		});
	}
}
