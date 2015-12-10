package game;
import net.Client;
import net.Server;
import net.ClientInterface;

public class CoreEngine implements ClientInterface {
	
	public static int TARGET_TPS = 10;
	public static float dt = (float) ((1.0 / TARGET_TPS) * 10);
	private static volatile boolean running = false;
	
	final int port = 12753;
	
	public static void main(String[] args){
		CoreEngine game = new CoreEngine();
		game.start();
	}
	
	protected synchronized void start(){
		running = true;
		Client client = new Client("DGD", "localhost", port, this);
		client.start();
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
		
	}

	@Override
	public void loginAccept() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loginFailed(String reason) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void roomCreated() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void roomJoined(String colorID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameBegin(boolean isQuestioneer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void questionReceived(String question) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void answerReceived(String nameOfAnswerer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startDiscussion(String[] answers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void roomFailed(String reason) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void joinFailed(String reason) {
		// TODO Auto-generated method stub
		
	}
}
