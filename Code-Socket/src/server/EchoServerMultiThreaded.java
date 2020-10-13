/***
 * EchoServer
 * TCP server
 * Date: 13/10/20
 * Authors: BRANCHEREAU Corentin, GRAVEY Thibaut
 */

package server;

import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EchoServerMultiThreaded implements ChatObserver {

	List<ClientThread> clients;
	ReadWriteLock lockClients = new ReentrantReadWriteLock();
	List<String> history;
	ReadWriteLock lockHistory = new ReentrantReadWriteLock();

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss - ");

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
		history = new ArrayList<>();
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
			}
		} catch (Exception e) {
			System.err.println("Error in EchoServer:" + e);
		}
	}

	@Override
	public void onClientMessage(ClientThread client, String msg) {
		try {
			lockClients.readLock().lock();
			lockHistory.writeLock().lock();
			LocalDateTime now = LocalDateTime.now();
			String toSend = dtf.format(now)+client.getClientName()+" : "+msg;
			for (ClientThread ct : this.clients) {
				ct.sendMessage(toSend);
			}
			history.add(toSend);
		} finally {
			lockHistory.writeLock().unlock();
			lockClients.readLock().unlock();
		}
	}

	@Override
	public void onClientConnection(ClientThread client) {
		//client.sendMessage("vous êtes connecté en tant que " + client.getClientName());
		try{
			lockHistory.readLock().lock();
			for(String oldMessage : history){
				client.sendMessage(oldMessage);
			}
		} finally {
			lockHistory.readLock().unlock();
		}

		try {
			lockClients.writeLock().lock();
			this.clients.add(client);

		} finally {
			lockClients.writeLock().unlock();
		}

		try{
			lockClients.readLock().lock();
			lockHistory.writeLock().lock();
			LocalDateTime now = LocalDateTime.now();
			String toSend = dtf.format(now)+client.getClientName()+" has joined the chat";
			for (ClientThread c : clients) {
				c.sendMessage(toSend);
			}
			history.add(toSend);
		} finally {
			lockHistory.writeLock().unlock();
			lockClients.readLock().unlock();
		}
	}

	@Override
	public void onClientDisconnection(ClientThread client) {

		try{
			lockClients.writeLock().lock();
			this.clients.remove(client);
		} finally {
			lockClients.writeLock().unlock();
		}

		try {
			lockClients.readLock().lock();
			lockHistory.writeLock().lock();
			LocalDateTime now = LocalDateTime.now();
			String toSend = dtf.format(now)+client.getClientName()+" has disconnected the chat";
			for(ClientThread c : clients) {
				c.sendMessage(toSend);
			}
			history.add(toSend);
		} finally {
			lockHistory.writeLock().unlock();
			lockClients.readLock().unlock();
		}
	}
}

