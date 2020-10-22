package tcp.client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

/**
 * ServerListenerThread
 * Listen to tcp.server from tcp.client side
 * Date: 13/10/20
 * @author BRANCHEREAU Corentin
 * @author GRAVEY Thibaut
 */
public class ServerListenerThread
		extends Thread {

	/**
	 * The TCP socket for client side of the chat system
	 */
	private Socket socket;

	//Stream
	/**
	 * Buffered reader for input of the client socket
	 */
	private BufferedReader socIn;

	//GUI
	/**
	 * GUI area to display message from the chat system
	 */
	private JTextArea textArea;

	/**
	 *  Constructor
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
	 *  Running the current thread
	 *  and listen to the input stream of the socket and write it on the terminal or the GUI
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
