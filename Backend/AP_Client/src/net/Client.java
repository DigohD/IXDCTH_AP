package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import game.Player;
import net.packet.Packet;
import net.packet.Packet10Login;
import net.packet.Packet11LoginAccept;
import net.packet.Packet12Disconnect;
import net.packet.Packet13Connect;

public class Client implements Runnable{
	
	private InetAddress ip;
	private DatagramSocket socket;
	private Thread thread;
	
	private int port;
	private int packetNumber = 0;
	
	private volatile boolean running = false;
	public boolean disconnected = false;
	
	public boolean connected = false;
	
	private HashMap<String, Player> clientsMap = new HashMap<String, Player>();
	private HashMap<Byte, Integer> byteToIntMap = new HashMap<Byte, Integer>();
	
	public LinkedList<Integer> pixels = new LinkedList<Integer>();
	
	private Packet10Login loginPacket;
	private String userName;
	
	public Client(String userName, String ip, int port){
		this.userName = userName;
		this.port = port;
		
		try {
			this.ip = InetAddress.getByName(ip);
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void start(){
		running = true;
		thread = new Thread(this, "client-thread");
		thread.start();
		loginPacket = new Packet10Login(userName);
		sendData(loginPacket.getData());
	}
	
	public synchronized void stop(){
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(!connected){
			loginPacket = new Packet10Login(userName);
			sendData(loginPacket.getData());
			
			byte[] data = new byte[1024];
        	DatagramPacket packet = new DatagramPacket(data, data.length);
               
            try{
                socket.setSoTimeout(1000);
                //System.out.println("BEFORE RECEIVE, !conected");
                socket.receive(packet);
                //System.out.println("AFTER RECEIVE, !connected");
            }catch(SocketTimeoutException e){
            	continue;
            } catch (IOException e) {	
            	e.printStackTrace();
            }
            	parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
       }
	
        while(running) {
        	byte[] data = new byte[1024];
        	DatagramPacket packet = new DatagramPacket(data, data.length);
               
            try{
                socket.setSoTimeout(1000);
               // System.out.println("BEFORE RECEIVE");
                socket.receive(packet);
                //System.out.println("AFTER RECEIVE");
            }catch(SocketTimeoutException e){
            	continue;
            } catch (IOException e) {	
            	e.printStackTrace();
            }
            	parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
            }
           socket.close();
	}

	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		
		Packet.PacketTypes type = Packet.lookupPacket(Integer.parseInt(message.substring(0, 2)));
		Packet packet = null;
		
		switch(type){
			case LOGIN:
				packet = new Packet10Login(data);
				handleLogin(packet, address, port);
				break;
			case LOGINACCEPT:
				connected = true;
				System.out.println("ACCEPT PACKET RECEIVED");
				packet = new Packet11LoginAccept(data);
				handleLoginAccept(packet, address, port);
				break;
			/*case CONNECT:
				System.out.println("CONNECT PACKET RECEIVED");
				packet = new Packet13Connect(data);
				handleConnect(packet, address, port);
				break;
			case DISCONNECT:
				packet = new Packet12Disconnect(data);
				handleDisconnect(packet, address, port);
				break;*/
			default:
				break;
		}
	}

	private void handleLogin(Packet packet, InetAddress address, int port) {
		Packet10Login p = (Packet10Login) packet;
		
		if(!(p.getUserName().equalsIgnoreCase(this.userName))){
			clientsMap.put(p.getUserName(), new Player(p.getUserName(), address, port));
		}
		else System.out.println(p.getUserName() + " has joined!");
	}
	
	private void handleDisconnect(Packet packet, InetAddress address, int port) {
		Packet12Disconnect p = (Packet12Disconnect) packet;
		if(clientsMap.containsKey(p.getUserName())){
			clientsMap.get(p.getUserName()).setLive(false);
			clientsMap.remove(p.getUserName());
		}
	}
	
	private void handleConnect(Packet packet, InetAddress address, int port){
		System.out.println("CONNECTED");
	}
	
	private void handleLoginAccept(Packet packet, InetAddress address, int port){
		System.out.println("ACCEPTED BY SERVER");
	}
	
	public void sendData(byte[] data){
		if(!disconnected){
			DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public HashMap<String, Player> getClientsMap() {
		return clientsMap;
	}

	public String getUserName() {
		return userName;
	}

}
