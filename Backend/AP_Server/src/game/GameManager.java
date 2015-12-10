package game;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import net.packet.*;

import net.Server;
import net.ServerInterface;

public class GameManager implements ServerInterface {

	private ArrayList<Group> groups = new ArrayList<Group>();
	private HashMap<InetAddress, Group> playerMap = new HashMap<InetAddress, Group>();
	
	private Server server;
	
	public GameManager(Server server){
		this.server = server;
	}
	
	public void tick(float dt){
		
	}
	
	@Override
	public void loginRequest(Player player){
		if(playerMap.containsKey(player.ip)){
			server.sendData(new Packet12LoginFailed("Already logged in").getData(), player.ip, player.port);
		}else{
			playerMap.put(player.ip, null);
			server.sendData(new Packet11LoginAccept(player.userName).getData(), player.ip, player.port);
		}
	}

	@Override
	public void createRoom(Player player, String roomName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void joinRoom(Player player, String roomName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void questionSent(Player player, String question) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void answerSent(Player player, String answer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discussionDone(Player player) {
		// TODO Auto-generated method stub
		
	}

}
