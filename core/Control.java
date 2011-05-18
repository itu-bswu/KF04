package core;

import exceptions.NothingCloseException;
import gui.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import pathfinding.NoPathException;
import pathfinding.Evaluator;
import utils.Direction;
import utils.PointMethods;
import utils.Properties;
import utils.RectangleMethods;

/**
 * Control class for the Map of Denmark system.
 * 
 * @author Jakob Melnyk
 * @version 29 April - 2011
 */
public class Control {
	private static final int bikeSpeed = 15;
	private static final double MOVE_LENGTH = 0.25;
	private static final double ZOOM_LENGTH = 0.15;
	private static final String NAME = Properties.get("programName"); //Name of the window containing the map.
	private View view;
	private Model model;
	private ArrayList<Point2D.Double> pins = new ArrayList<Point2D.Double>();
	private Object currentRouteMode = null;

	/**
	 * Constructor for class Control
	 */
	public Control() {
		model = new Model();

		view = new View(NAME, model.getBounds().width/model.getBounds().height);
		repaint();
		addListeners();
	}

	/**
	 * Adds listeners to everything useful in the View. Buttons, Component resize, Key-types and Mouse Events.
	 */
	private void addListeners(){
		addComponentListeners();
		addMouseListeners();
		addGUIButtonListeners();
		addKeyboardListeners();
	}

	/**
	 * Adds listeners to the components in View.
	 */
	private void addComponentListeners(){
		view.addCanvasComponentListener(new ComponentAdapter(){

			private int oldWidth = view.getCanvasWidth();
			private int oldHeight = view.getCanvasHeight();

			@Override
			public void componentResized(ComponentEvent e){
				Rectangle2D.Double map = model.getBounds();
				int newWidth = view.getCanvasWidth();
				int newHeight = view.getCanvasHeight();

				if(oldWidth < newWidth || oldHeight < newHeight){
					RectangleMethods.fixRatioByInnerRectangle(map, new Rectangle2D.Double(0, 0, newWidth, newHeight));
				}
				else{
					RectangleMethods.fixRatioByOuterRectangle(map, new Rectangle2D.Double(0, 0, newWidth, newHeight));
				}

				oldWidth = newWidth;
				oldHeight = newHeight;

				repaint();
			}
		});
	}

	/**
	 * Adds mouseListeners in View.
	 */	
	private void addMouseListeners(){
		//Listener for "mouse zoom"
		view.addCanvasMouseListener(new MouseAdapter(){
			private Point a_mouseZoom = null;
			private Point b_mouseZoom = null;

			@Override
			public void mousePressed(MouseEvent e){
				if(e == null) return; //Attempt to catch null pointer from weird mouse events.
				a_mouseZoom = e.getPoint();
				PointMethods.pointOutOfBounds(a_mouseZoom, new Rectangle(0, 0, view.getCanvasWidth(), view.getCanvasHeight()));
			}

			@Override
			public void mouseReleased(MouseEvent e){
				if(a_mouseZoom == null || e == null) return; //Attempt to catch null pointer from weird mouse events.
				b_mouseZoom = e.getPoint();
				PointMethods.pointOutOfBounds(b_mouseZoom, new Rectangle(0, 0, view.getCanvasWidth(), view.getCanvasHeight()));

				if(Math.abs(b_mouseZoom.x - a_mouseZoom.x) < view.getCanvasWidth()/100 
						|| Math.abs(b_mouseZoom.y - a_mouseZoom.y) < view.getCanvasHeight()/100){ 
					return; //Prevents the user from zooming in too much.
				}

				model.updateBounds(RectangleMethods.mouseZoom(a_mouseZoom, b_mouseZoom, model.getBounds(), 
						new Rectangle(0, 0, view.getCanvasWidth(), view.getCanvasHeight())));

				repaint();
			}



			@Override
			public void mouseClicked(MouseEvent e){
				boolean remove = false; // Will be set to true, if a pin needs to be removed. 
				Point2D.Double tempPin = null; //The pin to be removed, if anything. 
				for(Point2D.Double pin : pins){ 
					//Runs through all the current pins to check if there are any pins close to the clicked point.
					Point tempPoint = PointMethods.UTMToPixel(pin, model.getBounds(), 
							new Rectangle(0, 0, view.getCanvasWidth(), view.getCanvasHeight()));
					if(Math.abs(tempPoint.x - e.getX()) < 7 && Math.abs(tempPoint.y - e.getY()) < 7){
						tempPin = pin;
						remove = true;
					}
				}

				if(remove){ //If true, removes the pin, clears the calculated path and calculates a new one, if necessary.
					pins.remove(tempPin);
					model.clearPath();
					if(pins.size() > 1){
						for(int i = 0; i < pins.size() - 1; i++){
							findPath(i, i + 1);
						}
					}
				}
				else{ //Else it adds a pin and calculates the newest distance.
					pins.add(PointMethods.pixelToUTM(e.getPoint(), model.getBounds(), 
							new Rectangle(0, 0, view.getCanvasWidth(), view.getCanvasHeight())));	
					if(pins.size() > 1){
						findPath(pins.size()-2, pins.size()-1);
					}
				}
				repaint();
			}

			// Display the name of the closest road
			@Override
			public void mouseMoved(MouseEvent e){
				//Set label to closest road
				Point2D.Double p = PointMethods.pixelToUTM(e.getPoint(), model.getBounds(), 
						new Rectangle(0, 0, view.getCanvasWidth(), view.getCanvasHeight()));
				String roadName = model.getClosestRoadname(p);
				// Fix Danish characters.
				roadName = roadName.replace('ø', '¿');
				roadName = roadName.replace('Ø', '¯');
				roadName = roadName.replace('æ', '¾');
				roadName = roadName.replace('Æ', '®');
				roadName = roadName.replace('å', 'Œ');
				roadName = roadName.replace('Å', '');
				roadName = roadName.replace('é', 'Ž');
				view.setLabel(roadName);
			}
		});
	}

	/**
	 * Adds listeners to the keyboard.
	 */
	private void addKeyboardListeners(){
		view.addKeyListener(new KeyAdapter(){
			Rectangle2D.Double temp = null;

			@Override
			public void keyReleased(KeyEvent e) {	
				switch (e.getKeyCode()){
				case KeyEvent.VK_ESCAPE: //MaxZoom functionality.
					temp = model.originalBounds();
					RectangleMethods.fixRatioByOuterRectangle(temp, model.getBounds());
					model.updateBounds(temp);
					repaint();
					break;

				case KeyEvent.VK_UP: //Move up.
					temp = newBounds(model.getBounds(), MOVE_LENGTH, Direction.NORTH);
					model.updateBounds(temp);
					repaint();
					break;

				case KeyEvent.VK_DOWN: //Move down.
					temp = newBounds(model.getBounds(), MOVE_LENGTH, Direction.SOUTH);
					model.updateBounds(temp);
					repaint();
					break;

				case KeyEvent.VK_LEFT: //Move left.
					temp = newBounds(model.getBounds(), MOVE_LENGTH, Direction.WEST);
					model.updateBounds(temp);
					repaint();
					break;

				case KeyEvent.VK_RIGHT: //Move right.
					temp = newBounds(model.getBounds(), MOVE_LENGTH, Direction.EAST);
					model.updateBounds(temp);
					repaint();
					break;

				case KeyEvent.VK_C: //Clear pins.
					clearPins();
					break;

				case KeyEvent.VK_I: //Zoom in.
					temp = newBounds(model.getBounds(), ZOOM_LENGTH, Direction.IN);
					model.updateBounds(temp);
					repaint();
					break;

				case KeyEvent.VK_O: //Zoom out
					temp = newBounds(model.getBounds(), ZOOM_LENGTH, Direction.OUT);
					model.updateBounds(temp);
					repaint();
					break;
				}
			}
		});
	}

	/**
	 * Adds listeners to the GUI buttons.
	 */
	private void addGUIButtonListeners(){
		addMoveGUIButtonListeners();
		addZoomGUIButtonListeners();
		addClearPinButtonListener();
		addCarBikeRadioButtonListener();
	}

	/**
	 * Adds listener to the button to clear all pins.
	 */
	private void addClearPinButtonListener() {
		view.addClearMarkersListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				clearPins();
			}
		});
	}

	/**
	 * Adds listeners to the move buttons on the GUI.
	 */
	private void addMoveGUIButtonListeners(){
		//Listener for "move-up" button.
		view.addUpListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double move = newBounds(model.getBounds(), MOVE_LENGTH, Direction.NORTH);
				model.updateBounds(move);
				repaint();
			}});
		//Listener for "move-down" button.
		view.addDownListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double move = newBounds(model.getBounds(), MOVE_LENGTH, Direction.SOUTH);
				model.updateBounds(move);
				repaint();
			}});
		//Listener for "move-left" button.
		view.addLeftListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double move = newBounds(model.getBounds(), MOVE_LENGTH, Direction.WEST);
				model.updateBounds(move);
				repaint();
			}});
		//Listener for "move-right" button.
		view.addRightListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double move = newBounds(model.getBounds(), MOVE_LENGTH, Direction.EAST);
				model.updateBounds(move);
				repaint();
			}});
	}

	/**
	 * Adds listeners to the zoom buttons on the GUI.
	 */
	private void addZoomGUIButtonListeners(){
		//Listener for "zoom-in" button.
		view.addInListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				//Constructs a new rectangle using the maps bounds and the ZOOM_LENGTH variable.
				Rectangle2D.Double temp = newBounds(model.getBounds(), ZOOM_LENGTH, Direction.IN);
				model.updateBounds(temp);
				repaint();
			}});
		//Listener for "zoom-out" button.
		view.addOutListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				//Constructs a new rectangle using the maps bounds and the ZOOM_LENGTH variable.
				Rectangle2D.Double temp = newBounds(model.getBounds(), ZOOM_LENGTH, Direction.OUT);
				model.updateBounds(temp);
				repaint();
			}});
	}

	/**
	 * Adds listeners for the two modes of drive (car & bike)
	 */
	private void addCarBikeRadioButtonListener(){
		view.addRouteModeListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				//If the saved route mode is not the same as the new one, save the new route mode.
				if(currentRouteMode != arg0.getSource()){ 
					model.clearPath();
					currentRouteMode = arg0.getSource();
					if(pins.size() > 1){ //Recalculates the route, so that it conforms to the new vehicle type.
						for(int i = 0; i < pins.size() - 1; i++){
							findPath(i, i + 1);
						}
					}
				}
				repaint();
			}
		});
	}

	/**
	 * Clears the path in the model and removes all the pins, then repaints the view.
	 */
	private void clearPins(){
		model.clearPath();
		pins.clear();
		repaint();
	}

	/**
	 * Clears the pins and the route from view, recalculates positions for the pins and adds them to the view.
	 * Then adds the current route to the view, repaints it and sets the route info.
	 */
	private void repaint(){
		view.clearMarkers();
		view.clearRoute();
		for(Point2D.Double pin : pins){
			Point tempPin = PointMethods.UTMToPixel(pin, model.getBounds(), 
					new Rectangle(0, 0, view.getCanvasWidth(), view.getCanvasHeight()));
			view.addPin(tempPin);
		}
		view.addRoute(model.getPath());
		view.repaint(model.getLines());
		if(view.isBikeChoiceSelected()){
			double bikeTime = (model.getRouteDistance() / bikeSpeed) * 60;
			view.setRouteInfo(model.getRouteDistance(), bikeTime);
		}
		else{
			view.setRouteInfo(model.getRouteDistance(),model.getRouteTime());
		}
	}

	/**
	 * Used to find the path between two points - opens dialog window in view if problems occur.
	 * 
	 * @param start What pin the start point is at.
	 * @param end What pin the end point is at.
	 */
	private void findPath(int start, int end){
		// getting the right selector for the path finding		
		Evaluator eval = Evaluator.DEFAULT;
		if (view.isCarChoiceSelected()) {
			eval = Evaluator.CAR;
		} else if(view.isBikeChoiceSelected()) {
			eval = Evaluator.BIKE;
		}

		try {
			model.findPath(model.getClosestNode(pins.get(start),eval), model.getClosestNode(pins.get(end),eval),eval);
		}catch(NothingCloseException e1){
			view.displayDialog("You have placed one or more of your markers too far away from a node.", "Too far away from node.");
		}catch (NoPathException e2) {
			view.displayDialog("Could not find a route between two or more of your locations.", "Could not find route.");
		}	
	}

	/**
	 * Creates a rectangle moved in a direction compared to the old rectangle.
	 * 
	 * @param old The rectangle to be moved or zoomed.
	 * @param length How far the rectangle should be moved or zoomed.
	 * @param direction Which way should the rectangle move or zoom.
	 * @return The rectangle that has been moved or zoomed.
	 */
	private Rectangle2D.Double newBounds(Rectangle2D.Double old, double length, Direction direction){
		Rectangle2D.Double temp = RectangleMethods.newBounds(old, length, direction);
		if(direction == Direction.IN){
			if(temp.height < 200 || temp.width < 200){ //Prevents user from zooming in too far.
				return old;
			}
		}
		if(temp.width > model.originalBounds().width || temp.height > model.originalBounds().height){ //Prevents user from zooming out too far
			temp = model.originalBounds();
			RectangleMethods.fixRatioByOuterRectangle(temp, model.getBounds());
			return temp;
		}
		if((temp.x + temp.width) < model.originalBounds().x || //Prevents user from going too far west.
				temp.x > (model.originalBounds().x + model.originalBounds().width)){ //Prevents user from going too far east. 
			return old;
		}
		if((temp.y + temp.height) < model.originalBounds().y ||//Prevents user from going too far south
				temp.y > (model.originalBounds().y + model.originalBounds().height)){ //Prevents user from going too far north.
			return old;
		}
		return RectangleMethods.newBounds(old, length, direction);
	}
}