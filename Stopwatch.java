
public class Stopwatch {

	private long startTime;
	
	public Stopwatch(){
		restart();
	}
	
	public Stopwatch(String description){
		restart();
		System.out.print(description);
	}
	
	public double getTime(){
		return (System.currentTimeMillis()-startTime)/1000.;
	}
	
	public void restart(){
		startTime = System.currentTimeMillis();
	}
	
	public void printTime(){
		System.out.printf(" ... %.2f sec\n",getTime());
	}
}
