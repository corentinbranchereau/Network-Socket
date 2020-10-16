/***
 * EchoClient
 * TCP client
 * Date: 13/10/20
 * Authors: BRANCHEREAU Corentin, GRAVEY Thibaut
 */
package client;

import java.io.*;
import java.net.*;

public class EchoClient {

    //Stream
    private static Socket echoSocket;
    private static DataOutputStream socOut;
    private static BufferedReader stdIn;
    private static BufferedReader socIn;

    private static String name;

    /**
     *  main method
     *  accepts a connection, receives a message from client then sends an echo to the client
     **/
    public static void main(String[] args) throws IOException {
        echoSocket = null;
        socOut = null;
        stdIn = null;
        socIn = null;

        if (args.length != 3) {
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port> <Pseudo>");
            System.exit(1);
        }

        name = args[2];

        try {
            // creation socket ==> connexion
            echoSocket = new Socket(args[0],new Integer(args[1]).intValue());
            socIn = new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            //socOut= new PrintStream(echoSocket.getOutputStream());
            socOut = new DataOutputStream(echoSocket.getOutputStream());
            socOut.writeUTF(args[2]);
            socOut.flush();
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ args[0]);
            System.exit(1);
        }

        ServerListenerThread serverListenerThread = new ServerListenerThread(echoSocket,socIn,null);
        serverListenerThread.start();

        String line;
        while (true) {
            line=stdIn.readLine();
            if (line==null || line.equals(".")) break;
            sendMessage(line);
        }
        sendDisconnection();

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

    public static void sendDisconnection(){
        int protocolType = 1;

        try {
            socOut.writeInt(protocolType);
            socOut.flush();
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ echoSocket.getInetAddress());
            System.exit(1);
        }
    }
}


