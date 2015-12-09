package game;
import java.net.InetAddress;

public class Player {

	String userName;
	int port;
	InetAddress ip;
	
	boolean isLive = true;
	
	public Player(String userName, InetAddress ip, int port){
		this.userName = userName;
		this.ip = ip;
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public InetAddress getIp() {
		return ip;
	}

	public void setIp(InetAddress ip) {
		this.ip = ip;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	
	
	
}
