package net.packet;
import net.Client;
import net.Server;

public class Packet10Login extends Packet{
	
	private String userName;

	public Packet10Login(byte[] data) {
		super(10);
	    String[] dataArray = readData(data).split(":");
		this.userName = dataArray[1];
	}
	
	public Packet10Login(String userName) {
		super(10);
		this.userName = userName;
	}

	@Override
	public void writeData(Client client) {
		
	}

	@Override
	public void writeData(Server server) {
	
	}
	
	@Override
	public byte[] getData(){
		return ("10" + ":" + userName).getBytes();
	}
	

	public String getUserName() {
		return userName;
	}

}
