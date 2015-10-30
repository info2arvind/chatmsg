package com.chat.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClientThread extends Thread{
	
	private Socket socket;
	private ChatClient server;
	private DataInputStream input;
	
	public void open(){
		try {
			input = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while(true){
			try{
				server.handle(input.readUTF());
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("Listenning error" +socket);
				close();
			}
		}
		
	}
	
	public ChatClientThread(ChatClient server , Socket socket){
		this.server = server;
		this.socket = socket;
		open();
		start();

	}
	
	public void close(){
		if(input != null)
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	

}
