package game;

import java.util.ArrayList;

public class Group {

	private String name;
	
	private ArrayList<Player> players = new ArrayList<Player>();

	public Group(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	
	
	
}
