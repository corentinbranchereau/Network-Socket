/***
 * EchoServer
 * TCP server
 * Date: 13/10/20
 * Authors: BRANCHEREAU Corentin, GRAVEY Thibaut
 */

package server;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EchoServerMultiThreaded implements ChatObserver {

	private List<ClientThread> clients;
	private ReadWriteLock lockClients = new ReentrantReadWriteLock();
	private List<String> history;
	private ReadWriteLock lockHistory = new ReentrantReadWriteLock();
	private BufferedWriter logOut;

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	/**
	 * main method
	 * launch the server listener thread
	 * @param EchoServer port
	 **/
	public static void main(String[] args){
		if (args.length != 1) {
			System.out.println("Usage: java EchoServer <EchoServer port>");
			System.exit(1);
		}

		EchoServerMultiThreaded echoServerMultiThreaded = new EchoServerMultiThreaded();
		echoServerMultiThreaded.start(args);
	}

	/**
	 *  constructor
	 *  open the historyLog from file and create one if not exist
	 **/
	public EchoServerMultiThreaded(){
		clients = new LinkedList<>();
		history = new ArrayList<>();

		//History Log
		BufferedReader logIn = null;
		File historyLog = new File("Code-Socket/lib/historyLog.txt");
		try {
			if(historyLog.createNewFile()){
				System.out.println("History log file not found, creation...");
			} else {
				System.out.println("History log file found, loading...");
			}
		} catch (IOException e) {
			System.out.println("An error occured with the history log file");
			e.printStackTrace();
		}

		try{
			logIn = new BufferedReader(new FileReader(historyLog));
			String line = logIn.readLine();
			lockHistory.writeLock().lock();
			while(line!=null){
				history.add(line);
				line=logIn.readLine();
			}
			lockHistory.writeLock().unlock();
		} catch (IOException e) {
			System.out.println("An error occured with the history log reader");
			e.printStackTrace();
		} finally {
			try {
				logIn.close();
				logOut = new BufferedWriter(new FileWriter(historyLog,true));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *  start the listeningSocket and accept all the client
	 *  then start the client
	 * 	@param port
	 **/
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

	/**
	 *  add the message to the history
	 * 	@param msg
	 **/
	private void addToHistory(String msg){
		try{
			lockHistory.writeLock().lock();
			history.add(msg);
			logOut.write(msg);
			logOut.newLine();
			logOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			lockHistory.writeLock().unlock();
		}
	}

	/**
	 *  when a client send a message to the server, send the message to all connected participant
	 **/
	@Override
	public void onClientMessage(ClientThread client, String msg) {
		try {
			lockClients.readLock().lock();
			LocalDateTime now = LocalDateTime.now();
			String toSend = "["+dtf.format(now)+"] "+client.getClientName()+" : "+msg;
			for (ClientThread ct : this.clients) {
				ct.sendMessage(toSend);
			}
			addToHistory(toSend);
		} finally {
			lockClients.readLock().unlock();
		}
	}

	/**
	 *  when a client connect to the server, send him the whole historyLog and add it to the client list
	 *  alert other client that a client joined the chat
	 **/
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
			LocalDateTime now = LocalDateTime.now();
			String toSend = "["+dtf.format(now)+"] "+client.getClientName()+" has joined the chat";
			for (ClientThread c : clients) {
				c.sendMessage(toSend);
			}
			addToHistory(toSend);
		} finally {
			lockClients.readLock().unlock();
		}
	}

	/**
	 *  when a client disconnect, remove it from the client list
	 *  alert other client that a client has disconnected the chat
	 **/
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
			LocalDateTime now = LocalDateTime.now();
			String toSend = "["+dtf.format(now)+"] "+client.getClientName()+" has disconnected the chat";
			for(ClientThread c : clients) {
				c.sendMessage(toSend);
			}
			addToHistory(toSend);
		} finally {
			lockClients.readLock().unlock();
		}
	}
}

