import java.util.Observable;

public abstract class Algorithm extends Observable implements Runnable{

	static boolean seed;
	Parameters parameters = Parameters.getInstance();
	boolean running;
	Individual best;	
	
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return running;
	}
	
	public void stop() {
		running = false;
	}
}
