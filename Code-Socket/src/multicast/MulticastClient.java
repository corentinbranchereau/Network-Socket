/***
 * MulticastClient
 * UDP Client that connects to a multicast group
 * Date: 13/10/20
 * @author BRANCHEREAU Corentin
 * @author GRAVEY Thibaut
 */

package multicast;

import java.net.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;


public class MulticastClient {

   private static InetAddress groupAddr;
   private static int groupPort;
   private static String clientName;
   private static BufferedReader stdIn;

/**
   * main method
   * connects to a multicast group and launchs two threads, one to receive and one to send messages
   * @see MulticastReceiver the thread that listens for other client's messages
   * @see MulticastSender the thread that listens for user input, to send messages
   * @param args await 3 String params: an IP address, the port, and the client Name. 
   * 
   **/
public static void main(String[] args){
    try{
    // Group IP address
    
    // Create a multicast socket
        if (args.length != 3) {
            System.out.println("Usage: java MulticastClient <EchoServer host> <EchoServer port> <Name>");
            System.exit(1);
        }
        //Peuplement des variables
        groupAddr = InetAddress.getByName(args[0]);
        groupPort = Integer.parseInt(args[1]);
        clientName = args[2];
        stdIn = new BufferedReader(new InputStreamReader(System.in));
    
        MulticastSocket multiSocket = new MulticastSocket(groupPort);
        // Join the group
        multiSocket.joinGroup(groupAddr);

        MulticastSender multicastSender = new MulticastSender(multiSocket,stdIn,groupAddr,groupPort,clientName);
        multicastSender.start();

        MulticastReceiver multicastReceiver = new MulticastReceiver(multiSocket,clientName);
        multicastReceiver.start();

        // OK, I'm done talking - leave the group
        //multiSocket.leaveGroup(groupAddr); 
        //multiSocket.close();

    }catch(Exception e){
        System.out.println(e);
    }

}

}
