package multicast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class MulticastReceiver extends Thread {

    private MulticastSocket multicastSocket;
    private String clientName;
    private byte[] buf;
    private ByteArrayInputStream in;
    private ObjectInputStream is;
    
    MulticastReceiver(MulticastSocket multiSocket, String clientName){
        this.multicastSocket = multiSocket;
        this.clientName = clientName;
        this.buf= new byte[1000];
        this.in = new ByteArrayInputStream(buf);
        try {
            this.is = new ObjectInputStream(in);
        }catch(Exception e){
            System.err.println(e);
        }
    }

    public void run() {
		while(true){

		try {
            // Build a datagram packet for response
            this.buf = new byte[1000];
            DatagramPacket recv = new DatagramPacket(buf, buf.length);
            multicastSocket.receive(recv);
            //String s = new String(buf, StandardCharsets.UTF_8);
            Message message = deserialize();
            if(message.getName() != this.clientName){
                System.out.println(message);
            }

		} catch (Exception e) {
			System.err.println(e);
			System.exit(1);
		}
		}
    }
    
    public Message deserialize() throws IOException, ClassNotFoundException {
        Message msg = null;
        try {
            msg = (Message) this.is.readObject();
        }catch(Exception e){
            System.err.println(e);
        }
        return msg;

    }
}
