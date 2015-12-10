package net;

public interface ClientInterface {

	public void loginAccept();
	public void loginFailed(String reason);
	public void roomCreated();
	public void roomFailed(String reason);
	public void roomJoined(String colorID);
	public void joinFailed(String reason);
	public void gameBegin(boolean isQuestioneer);
	public void questionReceived(String question);
	public void answerReceived(String nameOfAnswerer);
	public void startDiscussion(String[] answers);
	
}
