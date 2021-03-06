package tcp.client;

import java.io.*;
import java.net.*;

/**
 * EchoClient
 * TCP tcp.client
 * Date: 13/10/20
 * @author BRANCHEREAU Corentin
 * @author GRAVEY Thibaut
 */
public class EchoClient {

    //Stream
    /**
     * The TCP socket for client side of the chat system
     */
    private static Socket echoSocket;
    /**
     * Data output for the client socket
     */
    private static DataOutputStream socOut;
    /**
     * Buffered Reader for standard input stream from the terminal (System.in)
     */
    private static BufferedReader stdIn;
    /**
     * Buffered reader for input of the client socket
     */
    private static BufferedReader socIn;

    /**
     * Name of the TCP client
     */
    private static String name;

    /**
     *  Initialize the socket with the TCP tcp.server,
     *  wait for user's input and send it to the tcp.server.
     *  It also launched a ServerListenerThread.
     * @see ServerListenerThread
     * @param args wait for 3 parameters : host, port, name
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

    /**
     *  Send a message using the output stream of the socket
     *  @param msg the message before protocol generation
     **/
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

    /**
     *  Send a disconnection message to prevent the tcp.server with right protocol
     **/
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


