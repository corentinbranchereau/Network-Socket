/**
 * ClientThread
 * Socket on a Thread to listen the tcp.client from Server side
 * Date: 13/10/20
 * @author BRANCHEREAU Corentin
 * @author GRAVEY Thibaut
 */

package tcp.server;

import java.io.*;
import java.net.*;

public class ClientThread
		extends Thread {

	private Socket clientSocket;
	private ChatObserver chatObserver;

	//Stream
	DataInputStream socIn;
	PrintStream socOut;

	private String name;

	/**
	 *  constructor
	 * 	@param co observer to prevent Client Thread
	 * 	@param s the tcp.client socket that we listen to
	 *	@see ClientThread
	 **/
	ClientThread(ChatObserver co, Socket s) {
		this.clientSocket = s;
		int numero = (int) (Math.random() * 100);
		this.name = Integer.toString(numero);
		this.chatObserver = co;
		try {
			socIn = null;
			//socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));*
			socIn = new DataInputStream(clientSocket.getInputStream());
			socOut = new PrintStream(clientSocket.getOutputStream());
		} catch (Exception e){
			System.err.println("Error in EchoServer (ClientThread Constructor) :" + e);
		}
	}

	/**
	 * 	listen to the tcp.client socket input in order to format the message with protocol
	 * 	then call a onClientMessage or onClientDisconnection event from the ChatObserver
	 * 	depends on protocolType
	 * @see ChatObserver
	 **/
	public void run() {
		try {
			this.name=socIn.readUTF();
			chatObserver.onClientConnection(this);
			while (true) {
				//String line = socIn.readLine();
				//socOut.println(line);
				//System.out.println(line);
				int protocolType = socIn.readInt();
				switch (protocolType){
					case 0 :
						String msg = socIn.readUTF();
						System.out.println("Receive is from "+clientSocket.toString()+" with protocolType = "+protocolType+" and message : '"+msg+"'");
						chatObserver.onClientMessage(this,msg);
						break;
					case 1 :
						System.out.println("Receive is from "+clientSocket.toString()+" with protocolType = "+protocolType+" --> Disconnection");
						chatObserver.onClientDisconnection(this);
						break;
				}
				if(protocolType==1) break;
			}
		} catch (Exception e) {
			System.err.println("Error in EchoServer:" + e);
			chatObserver.onClientDisconnection(this);
		}
	}

	/**
	 *  send a message to the tcp.client
	 *  @param msg
	 **/
	public void sendMessage(String msg){
		System.out.println("Envoi du msg '"+msg+"' sur socout par "+clientSocket.toString());
		socOut.println(msg);
	}

	/**
	 *  getter for the tcp.client name
	 * @return the name of the tcp.client
	 **/
	public String getClientName(){
		return this.name;
	}

}
