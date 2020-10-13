/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.net.*;
import java.util.LinkedList;
import java.util.List;

public class EchoServerMultiThreaded implements ChatObserver {

	List<ClientThread> clients;

	/**
	 * main method
	 * @param EchoServer port
	 *
	 **/
	public static void main(String[] args){
		if (args.length != 1) {
			System.out.println("Usage: java EchoServer <EchoServer port>");
			System.exit(1);
		}

		EchoServerMultiThreaded echoServerMultiThreaded = new EchoServerMultiThreaded();
		echoServerMultiThreaded.start(args);

	}

	public EchoServerMultiThreaded(){
		clients = new LinkedList<>();
	}

	private void start(String[] args){
		ServerSocket listenSocket;

		try {
			listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
			System.out.println("Server ready...");
			while (true) {
				Socket clientSocket = listenSocket.accept();
				System.out.println("Connexion from:" + clientSocket.getInetAddress());
				ClientThread ct = new ClientThread(this,clientSocket);
				ct.start();
				clients.add(ct);
			}
		} catch (Exception e) {
			System.err.println("Error in EchoServer:" + e);
		}
	}

	@Override
	public synchronized void onClientMessage(ClientThread client, String msg) {
		for(ClientThread ct : this.clients){
			if( ct != client ){
				ct.sendMessage(msg);
			}
		}
	}

	@Override
	public synchronized void onClientConnection(ClientThread client) {
		client.sendMessage("vous êtes connecté");
		this.clients.add(client);
		for(ClientThread c : clients) {
			c.sendMessage("new client has joined the chat");
		}
		
	}

	@Override
	public synchronized void onClientDisconnection(ClientThread client) {
		client.sendMessage("vous êtes déconnecté");
		this.clients.remove(client);
		for(ClientThread c : clients) {
			c.sendMessage("new client has disconnected the chat");
		}
	}
}

