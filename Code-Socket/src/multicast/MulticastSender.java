package multicast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

public class MulticastSender extends Thread {

    private InetAddress groupAddr;
    private int groupPort;
    private MulticastSocket multicastSocket;
    private BufferedReader stdIn;
    private String clientName;
    private ByteArrayOutputStream out;
    private ObjectOutputStream os;
	
    
    MulticastSender(MulticastSocket multiSocket,BufferedReader stdIn, InetAddress groupAddr, int groupPort, String clientName) {
        this.multicastSocket=multiSocket;
        this.stdIn = stdIn;
        this.groupAddr = groupAddr;
        this.groupPort = groupPort;
        this.clientName = clientName;
        this.out = new ByteArrayOutputStream();
        try{
            this.os = new ObjectOutputStream(out);
        }catch(Exception e){
            System.err.println(e);
        }
    } 

	public void run() {
		while(true){

			try {
                String msg = stdIn.readLine();
                
                //build the message object
                Message message = new Message(msg,this.clientName,null);

				// Build a datagram packet for a message
                // to send to the group
                DatagramPacket hi = new DatagramPacket(serialize(message), msg.length(), groupAddr, groupPort);
                multicastSocket.send(hi);

			} catch (Exception e) {
				System.err.println(e);
				System.exit(1);
			}
		}
    }
    
    public byte[] serialize(Object obj) throws IOException {
        try{
            this.os.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.out.toByteArray();
    }
}
