package com.chat.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



public class MsgServerThread extends Thread{
	private MsgServer server = null;
	private Socket socket = null;
	private int ID = -1;
	private DataInputStream input = null;
	private DataOutputStream output = null;
		
	public int getID() {
		return ID;
	}

	public MsgServerThread(MsgServer _server, Socket socket) {
		super();
		this.server = _server;
		this.socket = socket;
		ID = socket.getPort();
	}
	
	
	public void send(String msg){
	    try {
		    output.writeUTF(msg);
		    output.flush();

		} catch (IOException e) {
			e.printStackTrace();
			server.remove(ID);
			stop();
		}
	}
	
	public void run(){
		System.out.println("Server Thread " + ID + " running");
		while(true){
			try{
				server.handle(ID, input.readUTF());
			}catch(Exception e){
				e.printStackTrace();
				server.remove(ID);
				stop();
			}
		}
	}
	
	public void open() throws IOException{
		input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		output = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}
	
	public void close() throws IOException{
		if(socket != null)
			socket.close();
		if(input != null)
			input.close();
		if(output != null)
			output.close();
	}
	

}
