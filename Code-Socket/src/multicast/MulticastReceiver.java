/***
 * MulticastReceiver
 * Thread that listens for messages sent to the multicast group, and print the received messages
 * Date: 13/10/20
 * @author BRANCHEREAU Corentin
 * @author GRAVEY Thibaut
 */

package multicast;

import utils.Message;
import java.net.*;

public class MulticastReceiver extends Thread {

    private MulticastSocket multicastSocket;
    private String clientName;
    
    /**
     * Constructor of MulticastReceiver
     * @param multiSocket save the Socket where the client is connected
     * @param clientName save the clientName
     */
    MulticastReceiver(MulticastSocket multiSocket, String clientName){
        this.multicastSocket = multiSocket;
        this.clientName = clientName;
    }

/**
 * method that runs indefinitely to listen for received messages, and print them when one is receive
 */
    public void run() {
		while(true){

		try {
            // Build a datagram packet for response
            byte[] buf = new byte[1000];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            multicastSocket.receive(recv);
            //String s = new String(buf, StandardCharsets.UTF_8);
            Message message = new Message(null,null,null);
            message.readMessage(buf);
            if(!message.getName().equals(this.clientName)){
                System.out.println(message);
            }

		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}
		}
    }
    

}
