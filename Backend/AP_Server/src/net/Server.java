package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import game.Player;
import net.packet.Packet;
import net.packet.Packet10Login;
import net.packet.Packet11LoginAccept;

public class Server implements Runnable{
	
	private DatagramSocket socket;
	private Thread thread;
	
	private HashMap<String, Player> clientsMap = new HashMap<String, Player>();
	
	private boolean running = false;
	
	public Server(int port){
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
		
		switch(type){
			case INVALID:
				break;
			case LOGIN:
				Packet10Login packet00 = new Packet10Login(data);
				handleLogin(packet00, address, port);
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
		addConnection(player, packet);
	}
	
	private void addConnection(Player client, Packet packet) {
    	boolean alreadyConnected = false;
    	//loop through all the connected players 
        for (String name : clientsMap.keySet()) {
        	Player p = clientsMap.get(name);
            if (name.equalsIgnoreCase(client.getUserName())) {
                if (p.getIp() == null) {
                    p.setIp(client.getIp());
                }
                if (p.getPort() == -1) {
                    p.setPort(client.getPort());
                }
                alreadyConnected = true;
            } else {
                // relay to the current connected player that there is a new
                // player
                sendData(packet.getData(), p.getIp(), p.getPort());

                // relay to the new player that the currently connect player
                // exists
                packet = new Packet10Login(p.getUserName());
                sendData(packet.getData(), client.getIp(), client.getPort());
            }
        }
        if (!alreadyConnected) {
        	System.out.println("Player: " + client.getUserName() + " joined server succesfully");
            clientsMap.put(client.getUserName(), client);
        }
        sendData(new Packet11LoginAccept(client.getUserName()).getData(), client.getIp(), client.getPort());
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

	public void sendDataToAllClients(byte[] data) {
		for(String name : clientsMap.keySet()){
			Player p = clientsMap.get(name);
			sendData(data, p.getIp(), p.getPort());
		}	
	}

	public HashMap<String, Player> getClientsMap() {
		return clientsMap;
	}

}
