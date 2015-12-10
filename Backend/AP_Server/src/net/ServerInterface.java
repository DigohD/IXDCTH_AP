package net;

import game.Player;

public interface ServerInterface {
	
	public void loginRequest(Player player);
	public void createRoom(Player player, String roomName);
	public void joinRoom(Player player, String roomName);
	public void questionSent(Player player, String question);
	public void answerSent(Player player, String answer);
	public void discussionDone(Player player);
	
}
