package net.packet;
import net.Client;
import net.Server;

public class Packet12Disconnect extends Packet{
	
	private String userName;
	
	public Packet12Disconnect(byte[] data) {
		super(12);
		
		String[] dataArray = readData(data).split(":");
		
		this.userName = dataArray[1];
	}

	public Packet12Disconnect(String userName) {
		super(12);
		this.userName = userName;
	}

	@Override
	public void writeData(Client client) {
		
	}

	@Override
	public void writeData(Server server) {
		
	}

	@Override
	public byte[] getData() {
		return ("12" + ":" + userName).getBytes();
	}

	public String getUserName() {
		return userName;
	}

}
