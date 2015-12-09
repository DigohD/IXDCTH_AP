package net.packet;
import net.Client;
import net.Server;

public class Packet11LoginAccept extends Packet{

	private String username;

    public Packet11LoginAccept(byte[] data) {
        super(11);
        String[] dataArray = readData(data).split(":");
        this.username = dataArray[1];
    }

    public Packet11LoginAccept(String username) {
        super(11);
        this.username = username;
    }

    @Override
    public void writeData(Server server) {
        
    }

    @Override
    public byte[] getData() {
        return ("11" + ":" + this.username).getBytes();
    }

    public String getUsername() {
        return username;
    }

	@Override
	public void writeData(Client client) {
		
	}


}
