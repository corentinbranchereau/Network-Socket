package multicast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class MulticastReceiver extends Thread {

    private MulticastSocket multicastSocket;
    private String clientName;
    
    MulticastReceiver(MulticastSocket multiSocket, String clientName){
        this.multicastSocket = multiSocket;
        this.clientName = clientName;
    }

    public void run() {
		while(true){

		try {
            // Build a datagram packet for response
            byte[] buf = new byte[1000];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            multicastSocket.receive(recv);
            //String s = new String(buf, StandardCharsets.UTF_8);
            Message message = new Message(null,null,null);
            Message m2 = message.readMessage(buf);
            if(!m2.getName().equals(this.clientName)){
                System.out.println(m2);
            }

		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}
		}
    }
    

}
