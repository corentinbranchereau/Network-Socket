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
	public void onClientMessage(ClientThread client, String msg) {

	}

	@Override
	public void onClientConnection(ClientThread client) {

	}

	@Override
	public void onClientDisconnetion(ClientThread client) {

	}
}

