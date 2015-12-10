package net.packet;

import net.Client;
import net.Server;

public abstract class Packet {
	
	public static enum PacketTypes{
		INVALID(-1), 
		LOGIN(10),   
		LOGINACCEPT(11), 
		LOGINFAILED(12),
		CREATEROOM(13),
		ROOMCREATED(14),
		ROOMFAILED(15),
		JOINROOM(16),
		ROOMJOINED(17),
		JOINFAILED(18),
		GAMEBEGIN(19),
		SENDQUESTION(20),
		QUESTIONRECEIVED(21),
		SENDANSWER(22),
		ANSWERRECEIVED(23),
		STARTDISCUSSION(24),
		DONEDISCUSSING(25);
		
		private int packetID;
		
		private PacketTypes(int packetID){
			this.packetID = packetID;
		}
		
		public int getID(){
			return packetID;
		}
	}
	
	public byte packetID;
	
	public Packet(int packetID){
		this.packetID = (byte) packetID;
	}
	
	public abstract void writeData(Client client);
	public abstract void writeData(Server server);
	public abstract byte[] getData();
	
	public String readData(byte[] data){
		String message = new String(data).trim();
		return message.substring(2);
	}
	
	public static PacketTypes lookupPacket(int id){
		for(PacketTypes p : PacketTypes.values()){
			if(p.getID() == id) return p;
		}
		return PacketTypes.INVALID;
	}

}
