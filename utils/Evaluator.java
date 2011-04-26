package utils;

import dataobjects.KrakEdge;

public abstract class Evaluator<T> {

	public abstract float evaluate(T item);
	
	public static Evaluator<KrakEdge> CAR = new Evaluator<KrakEdge>(){

		@Override
		public float evaluate(KrakEdge item) {
			if(item.type == 8 || item.type == 11 || item.type == 28
					|| item.type == 48){ // Sti og gågade
				return Float.MAX_VALUE;
			}
			return item.DRIVETIME;
		}
	};
	
	public static Evaluator<KrakEdge> BIKE = new Evaluator<KrakEdge>(){

		@Override
		public float evaluate(KrakEdge item) {
			if(item.type == 1 || item.type == 2 || item.type == 11
					|| item.type == 21 || item.type == 22 || item.type == 31
					|| item.type == 32 || item.type == 41 || item.type == 42){
				return Float.MAX_VALUE;
			}
			
			return item.length;
		}
	};
}
