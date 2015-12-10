package net.packet;
import net.Client;
import net.Server;

public class Packet12LoginFailed extends Packet{

	private String reason;

    public Packet12LoginFailed(byte[] data) {
        super(12);
        String[] dataArray = readData(data).split(":");
        this.reason = dataArray[1];
    }

    public Packet12LoginFailed(String username) {
        super(12);
        this.reason = username;
    }

    @Override
    public void writeData(Server server) {
        
    }

    @Override
    public byte[] getData() {
        return ("12" + ":" + this.reason).getBytes();
    }

    public String getReason() {
        return reason;
    }

	@Override
	public void writeData(Client client) {
		
	}


}
