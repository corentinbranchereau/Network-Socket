package tcp.server;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * EchoServerMultiThread
 * TCP tcp.server
 * Date: 13/10/20
 * @author BRANCHEREAU Corentin
 * @author GRAVEY Thibaut
 */
public class EchoServerMultiThreaded implements ChatObserver {

	/**
	 * List of all connected TCP Clients
	 */
	private List<ClientThread> clients;
	/**
	 * Lock (mutex) for mutual exclusion (thread-safety) on the client list
	 */
	private ReadWriteLock lockClients = new ReentrantReadWriteLock();
	/**
	 * History of all interaction with the server (connection, disconnection and message)
	 */
	private List<String> history;
	/**
	 * Lock (mutex) for mutual exclusion (thread-safety) on the history
	 */
	private ReadWriteLock lockHistory = new ReentrantReadWriteLock();
	/**
	 * Standard output stream on the history log file for external storage
	 */
	private BufferedWriter logOut;
	/**
	 * Date Time formatter for message displaying
	 */
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	/**
	 * Launch the tcp.server listener thread
	 * @param args port from the tcp.server
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
	 *  Open the historyLog from 'historyLog.text' (create one if not exist)
	 *  then load thread-safely the historyLog in list
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
	 *  Start the listeningSocket and accept all the tcp.client
	 *  then start a ClientThread per tcp.client
	 * 	@see ClientThread
	 * 	@param args first argument is the server port
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
	 *  Thread-safely add a message to the history
	 * 	@param msg the message
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
	 *  When a tcp.client send a message to the tcp.server, send the message to all connected participant
	 * @param msg the message to send
	 * @param client the tcp.client thread involved in the communication
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
	 *  When a tcp.client connect to the tcp.server, send him the whole historyLog and thread-safely add it
	 *  to the tcp.client list, then alert other tcp.client that a tcp.client joined the chat
	 **/
	@Override
	public void onClientConnection(ClientThread client) {
		//tcp.client.sendMessage("vous êtes connecté en tant que " + tcp.client.getClientName());
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
	 *  When a tcp.client disconnect, thread-safely remove it from the tcp.client list then,
	 *  alert other tcp.client that a tcp.client has disconnected the chat
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

