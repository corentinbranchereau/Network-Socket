/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;

public class ClientThread
		extends Thread {

	private Socket clientSocket;
	private ChatObserver chatObserver;

	//Stream
	BufferedReader socIn;
	PrintStream socOut;

	private String name;

	ClientThread(ChatObserver co, Socket s) {
		this.clientSocket = s;
		this.chatObserver = co;
		try {
			socIn = null;
			socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			socOut = new PrintStream(clientSocket.getOutputStream());
		} catch (Exception e){
			System.err.println("Error in EchoServer (ClientThread Constructor) :" + e);
		}
	}

	/**
	 * receives a request from client then sends an echo to the client
	 * @param clientSocket the client socket
	 **/
	public void run() {
		try {
			chatObserver.onClientConnection(this);
			while (true) {
				String line = socIn.readLine();
				socOut.println(line);
				//System.out.println(line);
				chatObserver.onClientMessage(this,line);
			}
		} catch (Exception e) {
			System.err.println("Error in EchoServer:" + e);
			chatObserver.onClientDisconnection(this);
		}
	}

	public void sendMessage(String msg){
		socOut.println(msg);
	}

}
