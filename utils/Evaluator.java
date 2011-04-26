package utils;

import dataobjects.KrakEdge;

public abstract class Evaluator<T> {

	public abstract float evaluate(T item);
	
	public static Evaluator<KrakEdge> TRAVEL_TIME = new Evaluator<KrakEdge>(){

		@Override
		public float evaluate(KrakEdge item) {
			return item.DRIVETIME;
		}
	};
	
	public static Evaluator<KrakEdge> DISTANCE = new Evaluator<KrakEdge>(){

		@Override
		public float evaluate(KrakEdge item) {
			return item.length;
		}
	};
}
