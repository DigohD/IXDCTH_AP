package net.packet;
import net.Client;
import net.Server;

public class Packet14RoomCreated extends Packet{

    public Packet14RoomCreated(byte[] data) {
        super(14);
        String[] dataArray = readData(data).split(":");
    }

    public Packet14RoomCreated(String username) {
        super(14);
    }

    @Override
    public void writeData(Server server) {
        
    }

    @Override
    public byte[] getData() {
        return ("14").getBytes();
    }

	@Override
	public void writeData(Client client) {
		
	}


}
