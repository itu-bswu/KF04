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

	/**
	 * Constructor for class Control
	 */
	public Control() {
		model = new Model();
		view = new View(NAME, model.getBoundsWidth()/model.getBoundsHeight());
		view.repaint(model.getLines());
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

				view.repaint(model.getLines());
			}
		});
	}

	/**
	 * Adds mouseListeners in View.
	 */	
	private void addMouseListeners(){
		//Listener for "mouse zoom"
		view.addCanvasMouseListener(new MouseAdapter(){
			private Point a = null;
			private Point b = null;

			@Override
			public void mousePressed(MouseEvent e){
				if(e == null) return; //Attempt to catch null pointer from weird mouse events.
				a = e.getPoint();
				PointMethods.pointOutOfBounds(a, view);
			}

			@Override
			public void mouseReleased(MouseEvent e){
				if(a == null || e == null) return; //Attempt to catch null pointer from weird mouse events.
				b = e.getPoint();
				PointMethods.pointOutOfBounds(b, view);

				if(Math.abs(b.x - a.x) < view.getCanvasWidth()/100 
						|| Math.abs(b.y - a.y) < view.getCanvasHeight()/100){ 
					return; //Prevents the user from zooming in too much.
				}
				model.updateBounds(RectangleMethods.mouseZoom(a, b, model, view));
				view.repaint(model.getLines());
			}

			// Display the name of the closest road
			@Override
			public void mouseMoved(MouseEvent e){
				// set label to closest road
				Point2D.Double p = PointMethods.pixelToUTM(e.getPoint(), model, view);
				String roadName = model.getClosestRoad(p);
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
					view.repaint(model.getLines());
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
				view.repaint(model.getLines());
			}});
		//Listener for "move-down" button.
		view.addDownListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double move = RectangleMethods.move(model.getBounds(), MOVE_LENGTH, Direction.SOUTH);
				model.updateBounds(move);
				view.repaint(model.getLines());
			}});
		//Listener for "move-left" button.
		view.addLeftListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double move = RectangleMethods.move(model.getBounds(), MOVE_LENGTH, Direction.WEST);
				model.updateBounds(move);
				view.repaint(model.getLines());
			}});
		//Listener for "move-right" button.
		view.addRightListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double move = RectangleMethods.move(model.getBounds(), MOVE_LENGTH, Direction.EAST);
				model.updateBounds(move);
				view.repaint(model.getLines());
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
				view.repaint(model.getLines());
			}});
		//Listener for "zoom-out" button.
		view.addOutListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				//Constructs a new rectangle using the maps bounds and the ZOOM_LENGTH variable.
				Rectangle2D.Double p = RectangleMethods.zoomRectangle(ZOOM_LENGTH, false, model.getBounds());
				model.updateBounds(p);
				view.repaint(model.getLines());
			}});
	}
}