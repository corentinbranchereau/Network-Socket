package multicast;

import utils.Message;
import java.io.BufferedReader;
import java.net.*;
import java.util.Date;

/***
 * MulticastSender
 * Thread that listens for user input, and send a message to the multicast group and the user presses Enter
 * Date: 13/10/20
 * @author BRANCHEREAU Corentin
 * @author GRAVEY Thibaut
 */
public class MulticastSender extends Thread {

    /**
     * Multicast group address
     */
    private InetAddress groupAddr;
    /**
     * Multicast group port
     */
    private int groupPort;
    /**
     * Multicast Socket for UDP protocol
     */
    private MulticastSocket multicastSocket;
    /**
     * Standard input stream from the terminal (System.in)
     */
    private BufferedReader stdIn;
    /**
     * Name of the multicast client
     */
    private String clientName;

    /**
     * Constructor of class MulticastSender
     * @param multiSocket socket where user is connected
     * @param stdIn buffer to read user input
     * @param groupAddr multicast address
     * @param groupPort multicast port 
     * @param clientName name of the client
     */
    MulticastSender(MulticastSocket multiSocket,BufferedReader stdIn, InetAddress groupAddr, int groupPort, String clientName) {
        this.multicastSocket=multiSocket;
        this.stdIn = stdIn;
        this.groupAddr = groupAddr;
        this.groupPort = groupPort;
        this.clientName = clientName;
    }

    /**
     *  Method that runs indefinitely to listen for user input , and send message when user presses Enter
     *  Stops when user enters '.'  
     */
    public void run() {
        while(true){

            try {
                String msg = stdIn.readLine();

                //build the message object
                Message message = new Message(msg,this.clientName,new Date());
                message.sendMessage(this.multicastSocket,this.groupAddr, this.groupPort);
                if(msg.equals(".")){
                    System.out.println("disconnection");
                    multicastSocket.leaveGroup(groupAddr);
                    multicastSocket.close();
                    break;
                }


            } catch (Exception e) {
                System.err.println(e);
                System.exit(1);
            }
        }
        System.exit(1);
    }
}
