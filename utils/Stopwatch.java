package utils;

/**
 * A small utility for measuring the time passed.
 * 
 * @author Emil Juul Jacobsen
 */
public class Stopwatch {

	private long startTime;
	
	/**
	 * Creates a Stopwatch and starts the timing.
	 */
	public Stopwatch(){
		restart();
	}
	
	/**
	 * Creates a Stopwatch with a title.
	 * @param description The title of the event is printed when the Stopwatch is created.
	 */
	public Stopwatch(String description){
		restart();
		System.out.print(description+" ... ");
	}
	
	/**
	 * Gets the current time passed.
	 * @return
	 */
	public double getTime(){
		return (System.currentTimeMillis()-startTime)/1000.;
	}
	
	/**
	 * Resets the Stopwatch.
	 */
	public void restart(){
		startTime = System.currentTimeMillis();
	}
	
	/**
	 * Prints out the current time passed.
	 */
	public void printTime(){
		System.out.printf(this.toString());
	}
	
	@Override
	public String toString(){
		return String.format("%.2f sec\n",getTime());
	}
}
