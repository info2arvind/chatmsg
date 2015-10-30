package com.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MsgServer implements Runnable{
	
	private MsgServerThread clients[] = new MsgServerThread[50];
	private ServerSocket serverSocket = null;
	private Thread thread = null;
	private int clientCount = 0;
	
	public void start(){
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void stop(){
		if(thread != null){
			thread.stop();
			thread=null;
		}
	}
	
	public MsgServer(int port){
		System.out.println("Binding to port " + port + " please wait...");
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server started : " + serverSocket);
			start();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("server accept error ");
			stop();
		}
	}

	@Override
	public void run() {
		while(thread != null){
			try{
				System.out.println("waiting for server");
				addThread(serverSocket.accept());
			}catch(Exception e){
				System.out.println("server accept error "+ e);
			}
		}
		
	}
	
	public void addThread(Socket socket){
		if(clientCount < clients.length){
			System.out.println("Client accepted :" + socket);
			clients[clientCount] = new MsgServerThread(this, socket);
			try{
				clients[clientCount].open();
				clients[clientCount].start();
				clientCount++;
			}catch(Exception e){
				e.printStackTrace();
                System.out.println("Error openning Thread "+ e);
			}
		}else{
			System.out.println("Client refuse maximum lenght "+clients.length+" reached");
		}
	}
	
	public int findClientId(int id){
		for(int i = 0 ; i<clientCount ; i++)
			if(clients[i].getID() == id)
				return i;
		return -1;
	}
	
	public synchronized void remove(int id){
		int pos = findClientId(id);
		if(pos >= 0){
			MsgServerThread toterminate = clients[pos];
			System.out.println("Removing client thread "+ id + " at pos : "+pos);
			if(pos < clientCount - 1)
				for(int i = pos+1 ; i<clientCount ; i++)
				   clients[i-1] = clients[i];
			clientCount--;
			try{
				toterminate.close();
			}catch(Exception e){
				System.out.println("Error in closing thread : "+e);
			}
			toterminate.stop();
			
		}
		
	}
	
	public synchronized void handle(int id, String input){
		if(input.equals(".bye")){
			clients[findClientId(id)].send(input);
			remove(id);
		}else{
			for(int i = 0 ; i < clientCount ; i++)
				clients[i].send(id + "  "+input);
		}
	}
	public static void main(String[] args) {
		MsgServer msgServer  = new MsgServer(6608);
	}

}
