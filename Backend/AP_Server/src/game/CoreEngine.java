package game;
import net.Server;

public class CoreEngine {
	
	public static int TARGET_TPS = 10;
	public static float dt = (float) ((1.0 / TARGET_TPS) * 10);
	private static volatile boolean running = false;
	
	final int port = 12753;
	
	private GameManager gm;
	
	public static void main(String[] args){
		CoreEngine game = new CoreEngine();
		game.start();
	}
	
	protected synchronized void start(){
		running = true;
		
		Server server = new Server(port, gm);
		gm = new GameManager(server);
		server.start();
		
		gameLoop();
	}
	
	public static synchronized void stop(){
		running = false;
	}
	
	private void gameLoop(){
		double currentTime = 0;
		double previousTime = System.nanoTime();
		double passedTime = 0;
		double accumulator = 0;
		double frameCounter = 0;
		final double OPTIMAL_TICK_TIME = 1.0 / TARGET_TPS;
		
		int fps = 0;
		int tps = 0;
		
		while(running){
			currentTime = System.nanoTime();
			passedTime = (currentTime - previousTime) / 1000000000.0;
			accumulator += passedTime;
			frameCounter += passedTime;
			previousTime = currentTime;
		
			while(accumulator >= OPTIMAL_TICK_TIME){
				tick(dt);
				tps++;
				accumulator -= OPTIMAL_TICK_TIME;
			}
			fps++;
	
			if(frameCounter >= 1){
				fps = 0;
				tps = 0;
				frameCounter = 0;
			}
		}
	}
	
	public void tick(float dt) {
		gm.tick(dt);
	}
}
