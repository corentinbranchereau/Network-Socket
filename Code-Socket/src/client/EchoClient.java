/***
 * EchoClient
 * Example of a TCP client 
 * Date: 10/01/04
 * Authors:
 */
package client;

import java.io.*;
import java.net.*;

public class EchoClient {

    //Stream
    private static Socket echoSocket = null;
    private static DataOutputStream socOut = null;
    private static BufferedReader stdIn = null;
    private static BufferedReader socIn = null;

    /**
     *  main method
     *  accepts a connection, receives a message from client then sends an echo to the client
     **/
    public static void main(String[] args) throws IOException {
        echoSocket = null;
        socOut = null;
        stdIn = null;
        socIn = null;

        if (args.length != 2) {
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
            System.exit(1);
        }

        try {
            // creation socket ==> connexion
            echoSocket = new Socket(args[0],new Integer(args[1]).intValue());
            socIn = new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            //socOut= new PrintStream(echoSocket.getOutputStream());
            socOut = new DataOutputStream(echoSocket.getOutputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ args[0]);
            System.exit(1);
        }

        ServerListenerThread serverListenerThread = new ServerListenerThread(echoSocket,socIn);
        serverListenerThread.start();

        String line;
        while (true) {
            line=stdIn.readLine();
            if (line.equals(".")) break;
            sendMessage(line);
        }

        socOut.close();
        socIn.close();
        stdIn.close();
        echoSocket.close();
    }

    public static void sendMessage(String msg){
        //Generer le protocole pour envoyer un message
        int protocolType = 0;

        try {
            socOut.writeInt(protocolType);
            socOut.writeUTF(msg);
            socOut.flush();
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ echoSocket.getInetAddress());
            System.exit(1);
        }
    }
}


