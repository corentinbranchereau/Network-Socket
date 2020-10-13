/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package client;

import server.ChatObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerListenerThread
		extends Thread {

	private Socket socket;

	//Stream
	private BufferedReader socIn;

	ServerListenerThread(Socket s, BufferedReader socIn) {
		this.socket = s;
		this.socIn = socIn;
	}

	public void run() {
		while(true){
			try {
				String line = socIn.readLine();
				System.out.println(line);
			} catch (IOException e) {
				System.err.println("Couldn't get I/O for "
						+ "the connection to:"+ socket.getInetAddress());
				System.exit(1);
			}
		}
	}
}
