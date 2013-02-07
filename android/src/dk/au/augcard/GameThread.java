package dk.au.augcard;

public class GameThread extends Thread{
	
	//Game objects:
	private Ball ball;
	private Envirrorment center;
	private boolean running = true;
	
	//time
	long prevTime;
	long currTime;
	
	//game area limits
	public static final float UPPERLIMITX = 200;
	public static final float LOWERLIMITX = -200;
	public static final float UPPERLIMITY = 150;
	public static final float LOWERLIMITY = -150;
	
	//score
	
	
	/**
	 * 
	 * @param ball
	 * @param paddle1
	 * @param paddle2
	 */	
	public GameThread(Ball ball,Envirrorment e) {
		this.ball = ball; 
		this.center = e;
		setDaemon(true);
		start();
	}
	
	@Override
	public synchronized void run() {
		super.run();
		setName("GameThread");
		prevTime = System.nanoTime();
		long td;
		yield();
		ball.reset();
		boolean collision = false;
		while(running) {
			currTime = System.nanoTime();
			td = currTime - prevTime;
			prevTime = currTime;
			
			center.update(td);			
			
			//update all position
			ball.update(td);
			yield();
		}
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	
}
