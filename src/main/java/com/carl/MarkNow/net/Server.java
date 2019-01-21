package com.carl.MarkNow.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	private ArrayList<ReceiveThread> receiveThreads;
	private ServerSocket serverSocket;
	
	public Server(int port) throws IOException {
		receiveThreads = new ArrayList<ReceiveThread>();
		serverSocket = new ServerSocket(port);
	}
	
	public void start() {
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				ReceiveThread receiveThread = new ReceiveThread(socket);
				receiveThreads.add(receiveThread);
				receiveThread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	private class ReceiveThread extends Thread {
		
		private Socket socket;
		private DataInputStream in;
		private DataOutputStream out;
		
		public ReceiveThread(Socket s) {
			socket = s;
			in = null;
			out = null;
		}
		
		public void run() {
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
				while(true) {
					String s = in.readUTF();
					for(ReceiveThread thread: receiveThreads) {
						if(thread != this) {
							thread.broadcast(s);
						}						
					}
				}
			} catch (IOException e) {
				try {
					socket.close();
					receiveThreads.remove(this);
				} catch (IOException e1) {
					e1.printStackTrace();
				}				
			} finally {	
				try {
					in.close();
					out.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void broadcast(String s) throws IOException {
			out.writeUTF(s);
			out.flush();
		}		
	}
}

