package utils;

import pathfinding.NotPassableException;
import dataobjects.KrakEdge;
import dataobjects.KrakNode;

public abstract class Evaluator {

	public abstract double evaluate(KrakEdge item) throws NotPassableException;
	public abstract double heuristic(KrakNode item, KrakNode target);
	
	public static Evaluator CAR = new Evaluator(){

		@Override
		public double evaluate(KrakEdge item) throws NotPassableException {
			
			if((item.type == 8 || item.type == 11 || item.type == 28
					|| item.type == 48) || item.direction == 0){ // Sti og g�gade
				throw new NotPassableException("invalid roadtype");
			}
			return item.DRIVETIME;
		}

		@Override
		public double heuristic(KrakNode item, KrakNode target) {
			double distance = item.distanceTo(target);
			return distance/(1000*(110.0f/60));
		}
	};
	
	public static Evaluator BIKE = new Evaluator(){

		@Override
		public double evaluate(KrakEdge item) throws NotPassableException {
			if((item.type == 1 || item.type == 2 || item.type == 11
					|| item.type == 21 || item.type == 22 || item.type == 31
					|| item.type == 32 || item.type == 41 || item.type == 42 || item.direction == 0)){
				throw new NotPassableException("invalid roadtype");
			}
			
			return item.length;
		}

		@Override
		public double heuristic(KrakNode item, KrakNode target) {
			return item.distanceTo(target);
		}
	};
	
	// To include everything
	public static Evaluator ANYTHING = new Evaluator(){

		@Override
		public double evaluate(KrakEdge item) throws NotPassableException {
			return 0;
		}

		@Override
		public double heuristic(KrakNode item, KrakNode target) {
			return 0;
		}
		
	};
	
	public static Evaluator DEFAULT = Evaluator.CAR;
}
