package utils;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;


public class TestLineSimplification {

	//Test main
	public static void main(String[] args) {
		
		Line2D.Double[] path = {
			new Line2D.Double(0,0,10,2),
			new Line2D.Double(10,2,20,3),
			new Line2D.Double(20,3,30,-5),
			new Line2D.Double(30,-5,40,70),
			new Line2D.Double(40,70,50,4),
			new Line2D.Double(50,4,60,-2),
			new Line2D.Double(60,-2,70,4),
			new Line2D.Double(70,4,80,-8),
			new Line2D.Double(80,-8,90,0),
		};
		 
		/*Line2D.Double[] simplePath = new LineSimplification(path).getSimplePath();
		
		for(int i = 0; i<simplePath.length;i++) {
			System.out.println(simplePath[i].x1 + "," + simplePath[i].y1);
		}*/
		
	}
	
	//Fields
	Line2D.Double[] path;
	
	public TestLineSimplification(Line2D.Double[] path) {
		this.path = path;
	}
	
	/**
	 * 
	 * @return
	 */
	
	/*
	public Link getSimplePath() {
		
		Line2D.Double simplePath = new Line2D.Double[];
			
			new Line2D.Double(path[0].x1,path[0].y1,path[path.length-1].x1,path[path.length-1].y1);
		
		
		
		return simplePath;
	}*/
	
}
