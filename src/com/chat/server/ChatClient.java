package com.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient implements Runnable{
	
	private Socket socket;
	private Thread thread;
	private DataInputStream input;
	private DataOutputStream output;
	private ChatClientThread client;
	
	public ChatClient(String servername, int port){
		System.out.println("Establishing connection");
		try{
			socket = new Socket(servername, port);
			System.out.println("system connected "+socket);
			start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
			while(thread !=null){
				try {
					output.writeUTF(input.readLine());
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Error in getting data");
					stop();
				}
			}
		
	}
	
	public void start(){
		input = new DataInputStream(System.in);
		try {
			output = new DataOutputStream(socket.getOutputStream());
			if(thread == null){
				client = new ChatClientThread(this, socket);
				thread = new Thread(this);
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop(){
		if(thread != null){
			thread.stop();
			thread=null;
		}else{
			
				try {
					if(socket != null)
					    socket.close();
					if(input != null)
						input.close();
					if(output != null)
						output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		client.close();
		
	}
	
	public void handle(String msg){
		if(msg.equals(".bye")){
			stop();
			
		}else{
			System.out.println(msg);
		}
	}
    public static void main(String[] args) {
		new ChatClient("localhost", 6606);
	}
}
