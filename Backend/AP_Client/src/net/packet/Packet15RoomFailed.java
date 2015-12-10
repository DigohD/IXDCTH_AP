package net.packet;
import net.Client;
import net.Server;

public class Packet15RoomFailed extends Packet{

	private String reason;

    public Packet15RoomFailed(byte[] data) {
        super(15);
        String[] dataArray = readData(data).split(":");
        this.reason = dataArray[1];
    }

    public Packet15RoomFailed(String username) {
        super(15);
        this.reason = username;
    }

    @Override
    public void writeData(Server server) {
        
    }

    @Override
    public byte[] getData() {
        return ("15" + ":" + this.reason).getBytes();
    }

    public String getReason() {
        return reason;
    }

	@Override
	public void writeData(Client client) {
		
	}


}
