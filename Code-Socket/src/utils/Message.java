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

    public Message(String msg,String name,Date date){
        this.message = msg;
        this.name = name;
        this.date = date;
    }

    public String toString(){
        return df.format(date)+name+" : "+message;
    }

    public String getName(){
        return this.name;
    }

    public void sendMessage(MulticastSocket multicastSocket, InetAddress groupAddr, int groupPort ) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(this);
        byte[] data = out.toByteArray();
        DatagramPacket hi = new DatagramPacket(data, data.length, groupAddr, groupPort);
        multicastSocket.send(hi);
    }

    public void readMessage(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        Message getMessage = (Message) is.readObject();
        this.message = getMessage.message;
        this.name = getMessage.name;
        this.date = getMessage.date;
    }
}
