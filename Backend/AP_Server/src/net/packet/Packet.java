package net.packet;

import net.Client;
import net.Server;

public abstract class Packet {
	
	public static enum PacketTypes{
		INVALID(-1), 
		LOGIN(10),   
		ACCEPT(11), 
		DISCONNECT(12),
		CONNECT(13);
		
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
