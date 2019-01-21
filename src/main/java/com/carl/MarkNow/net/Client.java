package com.carl.MarkNow.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.carl.MarkNow.model.Text;

public class Client {
	
	private Text text;
	private Boolean isConnected;
	private Socket clientSocket;
	private DataOutputStream out;
	private DataInputStream in;
	private Thread recieveThread;  
	public Client(Text t) {
		text = t;
		text.setClient(this);
		isConnected = false;
		clientSocket = null;
		out = null;
		in = null;
		recieveThread = null;
	}
	
	public void connectToServer(String serverIp, int serverPort) throws UnknownHostException, IOException {
		try {
			isConnected = true;
			clientSocket = new Socket(serverIp, serverPort);
			out = new DataOutputStream(clientSocket.getOutputStream());
			in = new DataInputStream(clientSocket.getInputStream());
			recieveThread = new RecieveThread();
			recieveThread.start();
		} catch(IOException e) {
			isConnected = false;
			e.printStackTrace();
		}
	}
	
	public void disconnect() throws IOException {
		if (isConnected) {
			isConnected = false;
			try {
				in.close();
				out.close();
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public void commit() {
		if (isConnected) {
			try {
				out.writeUTF(text.getTextContent());
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class RecieveThread extends Thread {

		public void run() {
			while(isConnected) {
				try {
					String s = in.readUTF();
					text.updateFromServer(s);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}

}
