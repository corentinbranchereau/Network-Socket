package multicast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Message implements Serializable {
    private String message;
    private String name;
    private String date;

    Message(String msg,String name,String date){
        this.message = msg;
        this.name = name;
        this.date = date;
    }

    public String toString(){
        return name+": "+message;
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

    public Message readMessage(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return (Message) is.readObject();
    }
}
