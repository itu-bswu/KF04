package core;

import data.KrakEdge;
import loader.KrakLoader;
import data.KrakNode;
import core.Map;
import graphlib.Graph;
import gui.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

/**
 * Control class for the Map system.
 * 
 * @author Jakob Melnyk
 * @version 11 March - 2011
 */
public class Control {

	private static final float MOVE_LENGTH = (float) 0.30;
	private static final float ZOOM_LENGTH = (float) 0.15;
	private static final String NAME = "Map"; //Name of the window containing the map.
	private final File dataDir = new File(".", "data"); //Where control needs to look for the nodeFile and edgeFile
	private final String nodeFile = "kdv_node_unload.txt"; //The nodes used to construct the graph
	private final String edgeFile = "kdv_unload.txt"; //The edges used to construct the graph
	private View v;
	private Map m;

	/**
	 * Contstructor for class Control
	 */
	public Control() {
		Graph<KrakEdge, KrakNode> g = null;
		try {
			g = KrakLoader.graphFromFiles(new File(dataDir, nodeFile).getAbsolutePath(), new File(dataDir, edgeFile).getAbsolutePath());
		} catch (IOException e) {
			System.out.println("A problem occured when trying to read input.");
		}
		m = new Map(g);
		v = new View(NAME, m.getBoundsWidth()/m.getBoundsHeight());
		v.repaint(m.getLines());
		addListeners();
	}

	/**
	 * Adds listeners to everything useful in the View. Buttons, Component resize, Key-types and Mouse Events.
	 */
	private void addListeners(){
		//Listener for "move-up" button.
		v.addUpListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double old = m.getBounds();
				m.updateBounds(new Rectangle2D.Double(old.x, old.y + (1 * old.getHeight() * MOVE_LENGTH), old.width, old.height));
				v.repaint(m.getLines());
			}});
		//Listener for "move-down" button.
		v.addDownListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double old = m.getBounds();
				m.updateBounds(new Rectangle2D.Double(old.x, old.y - (1 * old.getHeight() * MOVE_LENGTH), old.width, old.height));
				v.repaint(m.getLines());
			}});
		//Listener for "move-left" button.
		v.addLeftListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double old = m.getBounds();
				m.updateBounds(new Rectangle2D.Double(old.x - (1 * old.width * MOVE_LENGTH), old.y, old.width, old.height));
				v.repaint(m.getLines());
			}});
		//Listener for "move-right" button.
		v.addRightListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double old = m.getBounds();
				m.updateBounds(new Rectangle2D.Double(old.x + (1 * old.width * MOVE_LENGTH), old.y, old.width, old.height));
				v.repaint(m.getLines());
			}});
		//Listener for "zoom-in" button.
		v.addInListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				//Constructs a new rectangle using the maps bounds and the ZOOM_LENGTH variable.
				Rectangle2D.Double old = m.getBounds();
				m.updateBounds(zoomRect(ZOOM_LENGTH, true, old));
				v.repaint(m.getLines());
			}});
		//Listener for "zoom-out" button.
		v.addOutListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				//Constructs a new rectangle using the maps bounds and the ZOOM_LENGTH variable.
				Rectangle2D.Double old = m.getBounds();
				m.updateBounds(zoomRect(ZOOM_LENGTH, false, old));
				v.repaint(m.getLines());
			}});
		//Listener for "mouse zoom"
		v.addCanvasMouseListener(new MouseAdapter(){
			private Point a = null;
			private Point b = null;
			private Rectangle2D.Double p = null;
			
			@Override
			public void mousePressed(MouseEvent e){
				a = e.getPoint();
			}
			
			@Override
			public void mouseReleased(MouseEvent e){
				if(a == null) return; //Tries to catch null pointer from weird mouse events.

				b = e.getPoint();
				if(Math.abs(b.x - a.x) < v.getCanvasWidth()/100 || Math.abs(b.y - a.y) < v.getCanvasHeight()/100) return; //Prevents the user from zooming in way too much.
				p = point2DToRectangle(pixelToUTM(a), pixelToUTM(b));
				fixRatio(p, m.getBounds());
				m.updateBounds(p);
				v.repaint(m.getLines());
			}

			// display closest road's name
			@Override
			public void mouseMoved(MouseEvent e){
				// set label to closest road
				v.setLabel(m.getClosestRoad(pixelToUTM(e.getPoint())));
			}
		});
		//
		v.addCanvasComponentListener(new ComponentAdapter(){
			private int oldWidth = v.getCanvasWidth();
			private int oldHeight = v.getCanvasHeight();

			@Override
			public void componentResized(ComponentEvent e){
				//Stopwatch timer = new Stopwatch("Adjusting to resize");
				Rectangle2D.Double map = m.getBounds();
				int newWidth = v.getCanvasWidth();
				int newHeight = v.getCanvasHeight();

				float x_adjust = (float) (map.width*(((float)newWidth - oldWidth)/oldWidth));
				float y_adjust = (float) (map.height*(((float)newHeight - oldHeight)/oldHeight));

				m.updateBounds(new Rectangle2D.Double(map.x, map.y - y_adjust, map.width + x_adjust,
						map.height + y_adjust));

				oldWidth = newWidth;
				oldHeight = newHeight;

				//timer.printTime();
				v.repaint(m.getLines());
			}
		});

		v.addKeyListener(new KeyAdapter(){

			@Override
			public void keyReleased(KeyEvent e) {
				// ESCAPE
				if(e.getKeyCode() == 27){
					Rectangle2D.Double temp = m.originalBounds();
					fixRatio(temp, m.getBounds());
					m.updateBounds(temp);
					v.repaint(m.getLines());
				}
			}
		});
	}

	/**
	 * Creates a Rectangle that is a zoomed in/out version of another.
	 * 
	 * @param factor The factor to zoom with, for example 0.2 for a 20% zoom.
	 * @param zoom True if zooming IN, else false.
	 * @param old The original view.
	 * @return The finished Rectangle
	 */
	private Rectangle2D.Double zoomRect(float factor, boolean zoom, Rectangle2D.Double old){
		if(zoom){
			return new Rectangle2D.Double(old.x + factor * old.width, //x is increased by the factor in proportion to the width
					old.y + factor * old.height, //y is increased by the factor in proportion to the height
					old.width - factor * old.width * 2, //width is decreased by the factor
					old.height - factor * old.height * 2); //height is decreased by the factor
		}
		else{
			return new Rectangle2D.Double(old.x - old.width * factor, //x is decreased by the factor in proportion to the width
					old.y - old.height * factor, //y is decreased by the factor in proportion to the height
					old.width + old.width * factor * 2, //width is increased by the factor
					old.height + old.height * factor * 2);//height is increased by the factor
		}
	}

	/**
	 * Converts a two Points to a Rectangle. Their relative location is irrelevant.
	 * @param a The first Point.
	 * @param b The second Point.
	 * @return A Rectangle with x,y in the upper left corner.
	 */
	private Rectangle2D.Double point2DToRectangle(Point2D.Double a, Point2D.Double b){
		Rectangle2D.Double p;
		if(b.x < a.x){ //If the second point is to the left of the first point, then do this 
			if(b.y < a.y){ //If the second point is above the first point, then do this.
				p = new Rectangle2D.Double(b.x, b.y, (a.x - b.x), (a.y - b.y));
			}
			else{

				p = new Rectangle2D.Double(b.x, a.y, (a.x - b.x), (b.y - a.y));
			}
		}
		else{
			if(b. y < a.y){ //If the second point is above the first point, then do this.
				p = new Rectangle2D.Double(a.x, b.y, (b.x - a.x), (a.y - b.y));
			}
			else{
				p = new Rectangle2D.Double(a.x, a.y, (b.x - a.x), (b.y - a.y));
			}
		}
		return p;
	}

	/**
	 * Prints out the amount of RAM currently used (in MegaBytes)
	 */
	private static void printRAM(){
		System.out.println("Used Memory: "+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/(1024*1024)+" mb");
	}

	/**
	 * Converts a pixel Point to a point with UTM coordinates.
	 * @param e The pixel-point from the screen.
	 * @return The UTM point.
	 */
	private Point2D.Double pixelToUTM(Point e){
		Rectangle2D.Double map = m.getBounds();
		//Inverts the y-value of the point, so that it is converted from the pixel coordinate system to the
		//UTM coordinate system
		e.y = v.getCanvasHeight() - e.y;
		// convert pixel to meters
		float x_m = (float) (map.x + (e.getX() / (float) v.getCanvasWidth()) * map.width);
		float y_m = (float) (map.y + (e.getY() / (float) v.getCanvasHeight()) * map.height);
		return new Point2D.Double(x_m, y_m);
	}

	/**
	 * Adjusts a Rectangle to have the same ratio as another Rectangle
	 * @param inner The Rectangle to adjust.
	 * @param outer The Rectangle that has the wanted ratio.
	 */
	private void fixRatio(Rectangle2D.Double inner, Rectangle2D.Double outer){
		float outer_ratio = (float) (outer.width / outer.height);
		float inner_ratio = (float) (inner.width / inner.height);
		
		if(inner_ratio < outer_ratio){
			// make wider
			float temp = (float) inner.width;
			inner.width = outer_ratio * inner.height;
			inner.x = inner.x - (inner.width - temp) / 2;
			
		}else{
			// make higher
			float temp = (float) inner.height;	
			inner.height = inner.width / outer_ratio;
			inner.y = inner.y - (inner.height - temp) / 2;
		}
		
//		float ratio = (float) (outer.width / outer.height);
//		// tall
//		if(outer.width > outer.height){
//			float temp = (float) inner.width;
//			inner.width = ratio * inner.height;
//			inner.x = inner.x - (inner.width - temp) / 2;
//		}
//		// wide
//		else{
//			float temp = (float) inner.height;	
//			inner.height = inner.width / ratio;
//			inner.y = inner.y - (inner.height - temp) / 2;
//		}
	}
}