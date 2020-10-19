/**
 * ServerListenerThread
 * Listen to tcp.server from tcp.client side
 * Date: 13/10/20
 * @author BRANCHEREAU Corentin
 * @author GRAVEY Thibaut
 */

package tcp.client;

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

	/**
	 *  constructor
	 * 	@param s the socket used by the tcp.server
	 * 	@param socIn the BufferedReader for the socket's input stream
	 * 	@param textArea the JTextArea for displaying the chat if Client's GUI is used
	 *  accepts a connection, receives a message from tcp.client then sends an echo to the tcp.client
	 */
	ServerListenerThread(Socket s, BufferedReader socIn, JTextArea textArea) {
		this.socket = s;
		this.socIn = socIn;
		this.textArea = textArea;
	}

	/**
	 *  running the current thread
	 *  listen to the input stream of the socket and write it on the terminal or the GUI
	 */
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
					textArea.setCaretPosition(textArea.getDocument().getLength());
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
