package utils;

import pathfinding.NotPassableException;
import dataobjects.KrakEdge;
import dataobjects.KrakNode;

/**
 * Has the responsibility to evaluate a given KrakEdge (into a number) and calculate the heuristic for
 * a KrakNode relative to a target KrakNode.
 * @author Emil
 *
 */
public abstract class Evaluator {
	// the needed methods
	public abstract float evaluate(KrakEdge item) throws NotPassableException;
	public abstract float heuristic(KrakNode item, KrakNode target);
	
	// static Evaluators for easy access.
	public static Evaluator CAR = new Evaluator(){

		@Override
		public float evaluate(KrakEdge item) throws NotPassableException {
			if(item.type == 8 || item.type == 11 || item.type == 28
					|| item.type == 48){ // Sti og gågade
				throw new NotPassableException("invalid roadtype");
			}
			return item.DRIVETIME;
		}

		@Override
		public float heuristic(KrakNode item, KrakNode target) {
			// the time it would take to travel to the target Node if there were a highway straight to it.
			float distance = (float)item.distanceTo(target);
			return distance/(1000*(110.0f/60));
		}
	};
	
	public static Evaluator BIKE = new Evaluator(){

		@Override
		public float evaluate(KrakEdge item) throws NotPassableException {
			if(item.type == 1 || item.type == 2 || item.type == 11
					|| item.type == 21 || item.type == 22 || item.type == 31
					|| item.type == 32 || item.type == 41 || item.type == 42){
				throw new NotPassableException("invalid roadtype");
			}
			
			return item.length;
		}

		@Override
		public float heuristic(KrakNode item, KrakNode target) {
			// the crows flight distance to the target Node.
			return (float)item.distanceTo(target);
		}
	};
	
	// To include everything
	public static Evaluator ANYTHING = new Evaluator(){

		@Override
		public float evaluate(KrakEdge item) throws NotPassableException {
			return 0;
		}

		@Override
		public float heuristic(KrakNode item, KrakNode target) {
			return 0;
		}
		
	};
	
	public static Evaluator DEFAULT = Evaluator.CAR;
}
