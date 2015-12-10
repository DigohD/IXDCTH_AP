package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import game.GameManager;
import game.Group;
import game.Player;
import net.packet.Packet;
import net.packet.Packet10Login;
import net.packet.Packet11LoginAccept;
import net.packet.Packet13CreateRoom;

public class Server implements Runnable{
	
	private DatagramSocket socket;
	private Thread thread;
	
	private HashMap<InetAddress, Player> clientsMap = new HashMap<InetAddress, Player>();
	
	private boolean running = false;
	
	private GameManager gm;
	
	public Server(int port, GameManager gm){
		this.gm = gm;
		
		System.out.println("Server created");
		
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void start(){
		System.out.println("Server started..");
		running = true;
		thread = new Thread(this, "server-thread");
		thread.start();
	}
	
	public void stop(){
		System.out.println("Server closing..");
		running = false;
		
		System.out.println("Server closed..");
		try {
			thread.join();
			System.out.println("Threads joined..");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			socket.setSoTimeout(10);
            while(running) {
            	byte[] data = new byte[1024];
				DatagramPacket packet = new DatagramPacket(data, data.length);
                try {
                	socket.receive(packet);
                }catch (SocketTimeoutException e) {
                    continue;
                }catch (IOException e) {
                    e.printStackTrace();
                }
                
                //System.out.println("REcieve Packet: " + new String(packet.getData()));
                parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
            }
        }catch (SocketException e1) {
			e1.printStackTrace();
		} finally {
            socket.close();
        }
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		
		//System.out.println(message.substring(0, 2));
		Packet.PacketTypes type = Packet.lookupPacket(Integer.parseInt(message.substring(0, 2)));
		Packet packet = null;
		switch(type){
			case INVALID:
				break;
			case LOGIN:
				packet = new Packet10Login(data);
				handleLogin((Packet10Login) packet, address, port);
				break;
			case CREATEROOM:
				packet = new Packet13CreateRoom(data);
				handleCreateRoom((Packet13CreateRoom) packet, address, port);
				break;
			default:
				break;
		}
	}
	
	/*private void handleDisconnect(Packet12Disconnect packet, InetAddress address, int port){
		System.out.println("DISCONNECT PACKET RECEIVED");
		if(clientsMap.containsKey(packet.getUserName())){
			clientsMap.get(packet.getUserName()).setLive(false);
			System.out.println("Player: " + clientsMap.get(packet.getUserName()).getUserName() + " has disconnected");
			clientsMap.remove(packet.getUserName());
		}
		sendDataToAllClients(packet.getData());
	}*/
	
	private void handleLogin(Packet10Login packet, InetAddress address, int port){
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getUserName() + " has connected");
		
		String name = packet.getUserName();
		Player player = new Player(name, address, port);
		gm.loginRequest(player);
	}
	
	private void handleCreateRoom(Packet13CreateRoom packet, InetAddress address, int port){
		System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getRoomName() + " room requested");
		
		String name = packet.getRoomName();
		gm.createRoom(gm.getPlayerMap().get(address), name);
	}

	public void sendData(byte[] data, InetAddress ip, int port){
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		
		System.out.println("SendPacket: " + new String(packet.getData()));
		
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClientsInGroup(byte[] data, Group group) {
		for(Player p : group.getPlayers()){
			sendData(data, p.getIp(), p.getPort());
		}	
	}
	
	public void sendDataToAllClients(byte[] data) {
		for(Player p : gm.getPlayerMap().values()){
			sendData(data, p.getIp(), p.getPort());
		}	
	}

}
