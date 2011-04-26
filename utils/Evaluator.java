package utils;

import core.NotPassableException;
import dataobjects.KrakEdge;

public abstract class Evaluator<T> {

	public abstract float evaluate(T item) throws NotPassableException;
	
	public static Evaluator<KrakEdge> CAR = new Evaluator<KrakEdge>(){

		@Override
		public float evaluate(KrakEdge item) throws NotPassableException {
			if(item.type == 8 || item.type == 11 || item.type == 28
					|| item.type == 48){ // Sti og gågade
				throw new NotPassableException("invalid roadtype");
			}
			return item.DRIVETIME;
		}
	};
	
	public static Evaluator<KrakEdge> BIKE = new Evaluator<KrakEdge>(){

		@Override
		public float evaluate(KrakEdge item) throws NotPassableException {
			if(item.type == 1 || item.type == 2 || item.type == 11
					|| item.type == 21 || item.type == 22 || item.type == 31
					|| item.type == 32 || item.type == 41 || item.type == 42){
				throw new NotPassableException("invalid roadtype");
			}
			
			return item.length;
		}
	};
}
