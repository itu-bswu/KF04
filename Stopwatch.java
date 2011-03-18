
public class Stopwatch {

	private long startTime;
	
	public Stopwatch(){
		restart();
	}
	
	public double getTime(){
		return (System.currentTimeMillis()-startTime)/1000.;
	}
	
	public void restart(){
		startTime = System.currentTimeMillis();
	}
}
