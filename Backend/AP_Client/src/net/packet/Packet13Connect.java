package net.packet;

import net.Client;
import net.Server;

public class Packet13Connect extends Packet{

	private String userName;

	public Packet13Connect(byte[] data) {
		super(13);
		String[] dataArray = readData(data).split(":");
		  
		this.userName = dataArray[1];
	}
	
	public Packet13Connect(String userName) {
		super(13);
		this.userName = userName;
	}

	@Override
	public void writeData(Client client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(Server server) {
		
	}
	
	@Override
	public byte[] getData() {
		return ("13" + ":" + userName).getBytes();
	}

	public String getUserName() {
		return userName;
	}

}
