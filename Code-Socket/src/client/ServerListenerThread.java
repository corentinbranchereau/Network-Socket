/***
 * ServerListenerThread
 * Listen to server from client side
 * Date: 13/10/20
 * Authors: BRANCHEREAU Corentin, GRAVEY Thibaut
 */

package client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ServerListenerThread
		extends Thread {

	private Socket socket;

	//Stream
	private BufferedReader socIn;

	//GUI
	private JTextArea textArea;

	ServerListenerThread(Socket s, BufferedReader socIn, JTextArea textArea) {
		this.socket = s;
		this.socIn = socIn;
		this.textArea = textArea;
	}

	public void run() {
		while(true){

			try {
				String line = socIn.readLine();
				if(line==null){
					System.out.println("Server lost...");
					break;
				}
				if(textArea!=null) {
					textArea.append(line+"\n");
				} else {
					System.out.println(line);
				}
			} catch (IOException e) {
				System.err.println("Couldn't get I/O for "
						+ "the connection to:"+ socket.getInetAddress());
				System.exit(1);
			}
		}
	}
}
