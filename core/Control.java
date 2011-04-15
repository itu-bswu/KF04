package core;

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
import utils.Direction;
import utils.PointMethods;
import utils.RectangleMethods;

/**
 * Control class for the Map of Denmark system.
 * 
 * @author Jakob Melnyk
 * @version 11 March - 2011
 */
public class Control {

	private static final float MOVE_LENGTH = (float) 0.30;
	private static final float ZOOM_LENGTH = (float) 0.15;
	private static final String NAME = "Map"; //Name of the window containing the map.
	private View view;
	private Model model;
	private ArrayList<Point2D.Double> pins = new ArrayList<Point2D.Double>();

	/**
	 * Constructor for class Control
	 */
	public Control() {
		model = new Model();
		view = new View(NAME, (float) (model.getBounds().width/model.getBounds().height));
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
					RectangleMethods.fixRatioByInnerRectangle(map,new Rectangle2D.Double(0,0,newWidth,newHeight));
				}else{
					RectangleMethods.fixRatioByOuterRectangle(map,new Rectangle2D.Double(0,0,newWidth,newHeight));
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
				boolean remove = false;
				/*for(Point2D.Double pin : pins){
					Point tempPoint = PointMethods.UTMToPixel(pin, model, view);
					if(tempPoint.x - e.getX() < 10 && tempPoint.y - e.getY() < 10){
						pins.remove(pin);
						remove = true;
					}
				}*/
				pins.add(PointMethods.pixelToUTM(e.getPoint(), model, view));
				
				repaint();
			}

			// Display the name of the closest road
			@Override
			public void mouseMoved(MouseEvent e){
				// set label to closest road
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
			@Override
			public void keyReleased(KeyEvent e) {
				// ESCAPE
				if(e.getKeyCode() == 27){
					Rectangle2D.Double temp = model.originalBounds();
					RectangleMethods.fixRatioByOuterRectangle(temp, model.getBounds());
					model.updateBounds(temp);
					repaint();
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
	}

	/**
	 * Adds listeners to the move buttons on the GUI.
	 */
	private void addMoveGUIButtonListeners(){
		//Listener for "move-up" button.
		view.addUpListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double move = RectangleMethods.move(model.getBounds(), MOVE_LENGTH, Direction.NORTH);
				model.updateBounds(move);
				repaint();
			}});
		//Listener for "move-down" button.
		view.addDownListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double move = RectangleMethods.move(model.getBounds(), MOVE_LENGTH, Direction.SOUTH);
				model.updateBounds(move);
				repaint();
			}});
		//Listener for "move-left" button.
		view.addLeftListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double move = RectangleMethods.move(model.getBounds(), MOVE_LENGTH, Direction.WEST);
				model.updateBounds(move);
				repaint();
			}});
		//Listener for "move-right" button.
		view.addRightListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double move = RectangleMethods.move(model.getBounds(), MOVE_LENGTH, Direction.EAST);
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
				Rectangle2D.Double p = RectangleMethods.zoomRectangle(ZOOM_LENGTH, true, model.getBounds());
				model.updateBounds(p);
				repaint();
			}});
		//Listener for "zoom-out" button.
		view.addOutListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				//Constructs a new rectangle using the maps bounds and the ZOOM_LENGTH variable.
				Rectangle2D.Double p = RectangleMethods.zoomRectangle(ZOOM_LENGTH, false, model.getBounds());
				model.updateBounds(p);
				repaint();
			}});
	}
	
	/**
	 * 
	 */
	private void repaint(){
		view.clearPins();
		for(Point2D.Double pin : pins){
			Point tempPin = PointMethods.UTMToPixel(pin, model, view);
			System.out.println(tempPin);
			view.addPin(tempPin);
		}
		view.repaint(model.getLines());
	}
}