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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import pathfinding.NoPathException;
import utils.Direction;
import utils.Evaluator;
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
				PointMethods.pointOutOfBounds(a_mouseZoom, view);
			}

			@Override
			public void mouseReleased(MouseEvent e){
				if(a_mouseZoom == null || e == null) return; //Attempt to catch null pointer from weird mouse events.
				b_mouseZoom = e.getPoint();
				PointMethods.pointOutOfBounds(b_mouseZoom, view);

				if(Math.abs(b_mouseZoom.x - a_mouseZoom.x) < view.getCanvasWidth()/100 
						|| Math.abs(b_mouseZoom.y - a_mouseZoom.y) < view.getCanvasHeight()/100){ 
					return; //Prevents the user from zooming in too much.
				}
				
				model.updateBounds(RectangleMethods.mouseZoom(a_mouseZoom, b_mouseZoom, model, view));

				repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e){
				boolean remove = false; // Will be set to true, if a pin needs to be removed. 
				Point2D.Double tempPin = null; //The pin to be removed, if anything. 

				for(Point2D.Double pin : pins){ 
					//Runs through all the current pins to check if there are any pins close to the clicked point.
					Point tempPoint = PointMethods.UTMToPixel(pin, model, view);
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
					pins.add(PointMethods.pixelToUTM(e.getPoint(), model, view));
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
				Point2D.Double p = PointMethods.pixelToUTM(e.getPoint(), model, view);
				String roadName = model.getClosestRoadname(p);
				view.setLabel(roadName);
			}
		});
	}

	/**
	 * Adds listeners to the keyboard.
	 */
	private void addKeyboardListeners(){
		//Listener for maxZoom function.
		view.addKeyListener(new KeyAdapter(){
			Rectangle2D.Double temp = null;

			@Override
			public void keyReleased(KeyEvent e) {	
				switch (e.getKeyCode()){
				case KeyEvent.VK_ESCAPE:
					temp = model.originalBounds();
					RectangleMethods.fixRatioByOuterRectangle(temp, model.getBounds());
					model.updateBounds(temp);
					repaint();
					break;
					
				case KeyEvent.VK_UP:
					temp = newBounds(model.getBounds(), MOVE_LENGTH, Direction.NORTH);
					model.updateBounds(temp);
					repaint();
					break;
					
				case KeyEvent.VK_DOWN:
					temp = newBounds(model.getBounds(), MOVE_LENGTH, Direction.SOUTH);
					model.updateBounds(temp);
					repaint();
					break;

				case KeyEvent.VK_LEFT:
					temp = newBounds(model.getBounds(), MOVE_LENGTH, Direction.WEST);
					model.updateBounds(temp);
					repaint();
					break;

				case KeyEvent.VK_RIGHT:
					temp = newBounds(model.getBounds(), MOVE_LENGTH, Direction.EAST);
					model.updateBounds(temp);
					repaint();
					break;

				case KeyEvent.VK_C:
					clearPins();
					break;
					
				case KeyEvent.VK_I:
					temp = newBounds(model.getBounds(), ZOOM_LENGTH, Direction.IN);
					model.updateBounds(temp);
					repaint();
					break;
					
				case KeyEvent.VK_O:
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

	private void addClearPinButtonListener() {
		view.addClearPinsListener(new ActionListener(){

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
		view.clearPins();
		view.clearRoute();
		for(Point2D.Double pin : pins){
			Point tempPin = PointMethods.UTMToPixel(pin, model, view);
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
		//TODO: Possibly integrate better with mousezoom - possible to go out borders using a combination of zoom and movement.
		Rectangle2D.Double temp = RectangleMethods.newBounds(old, length, direction);
		switch(direction){
		case NORTH:
			if(temp.y > (model.originalBounds().y + model.originalBounds().height)){
				return old;
			}
			return temp;
		case SOUTH:
			if((temp.y + model.originalBounds().height) < model.originalBounds().y){
				return old;
			}
			return temp;
		case WEST:
			if((temp.x + model.originalBounds().width) < model.originalBounds().x){
				return old;
			}
			return temp;
		case EAST:
			if(temp.x > (model.originalBounds().x + model.originalBounds().width)){
				return old;
			}
			return temp;
		case IN:
			if(temp.width < 200 || temp.height < 200){
				return old;
			}
			else{
				return temp;				
			}
		case OUT:
			
			return temp;
		}
		return old;
	}
}