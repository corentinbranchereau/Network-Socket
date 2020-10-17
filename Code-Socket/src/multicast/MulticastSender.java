package multicast;

import utils.Message;

import java.io.BufferedReader;
import java.net.*;
import java.util.Date;

public class MulticastSender extends Thread {

    private InetAddress groupAddr;
    private int groupPort;
    private MulticastSocket multicastSocket;
    private BufferedReader stdIn;
    private String clientName;
    
    MulticastSender(MulticastSocket multiSocket,BufferedReader stdIn, InetAddress groupAddr, int groupPort, String clientName) {
        this.multicastSocket=multiSocket;
        this.stdIn = stdIn;
        this.groupAddr = groupAddr;
        this.groupPort = groupPort;
        this.clientName = clientName;
	}

	public void run() {
		while(true){

			try {
                String msg = stdIn.readLine();
                
                //build the message object
                Message message = new Message(msg,this.clientName,new Date());
                message.sendMessage(this.multicastSocket,this.groupAddr, this.groupPort);
				// Build a datagram packet for a message
                // to send to the group
               

			} catch (Exception e) {
				System.err.println(e);
				System.exit(1);
			}
		}
    }
}
