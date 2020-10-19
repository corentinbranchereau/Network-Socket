/***
 * Message
 * A class used to transfer information (ie: message, name, date) through multicast Sockets
 * Date: 13/10/20
 * @author BRANCHEREAU Corentin
 * @author GRAVEY Thibaut
 */

package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Message implements Serializable {
    private String message;
    private String name;
    private Date date;

    private transient DateFormat df = new SimpleDateFormat("[dd-MM-yyyy HH:mm:ss] ");

    /**
     * constructor of class Message
     * @param msg the content of the message 
     * @param name the name of the client
     * @param date when the message was sent
     */
    public Message(String msg,String name,Date date){
        this.message = msg;
        this.name = name;
        this.date = date;
    }

    /**
     * method toString 
     * @return String :the arguments of the class to print them
     */
    public String toString(){
        if(message.equals(".")){
            return name + " has disconnected";
        }else{
            return df.format(date)+name+" : "+message;
        }
    }

    /**
     * method getName 
     * @return String :the client name
     */
    public String getName(){
        return this.name;
    }

    /**
     * method that send the message to the multicast socket
     * @param multicastSocket the multicastSocket object
     * @param groupAddr the group address
     * @param groupPort the group port
     * @throws IOException 
     */
    public void sendMessage(MulticastSocket multicastSocket, InetAddress groupAddr, int groupPort ) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(this);
        byte[] data = out.toByteArray();
        DatagramPacket hi = new DatagramPacket(data, data.length, groupAddr, groupPort);
        multicastSocket.send(hi);
    }

    /**
     * method that populate the Message class attributes from the data received in parameters
     * @param data a byte array
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void readMessage(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        Message getMessage = (Message) is.readObject();
        this.message = getMessage.message;
        this.name = getMessage.name;
        this.date = getMessage.date;
    }
}
