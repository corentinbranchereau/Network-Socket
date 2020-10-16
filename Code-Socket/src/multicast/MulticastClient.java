package multicast;
import java.net.*;
import java.io.IOException;


public class MulticastClient {

// Group IP address
InetAddress groupAddr = InetAddress.getByName("228.5.6.7");
Integer groupPort = 6789;
// Create a multicast socket
MulticastSocket s = new MulticastSocket(groupPort);
// Join the group

s.joinGroup(groupAddr);
// Build a datagram packet for a message
// to send to the group
String msg = "Hello";
DatagramPacket hi = new DatagramPacket(msg.getBytes(),
msg.length(), groupAddr, groupPort);
// Send a multicast message to the group
s.@send(hi);
// Build a datagram packet for response
byte[] buf = new byte[1000];
DatagramPacket recv = new
DatagramPacket(buf, buf.length);

// Receive a datagram packet response
s.receive(recv);
// OK, I'm done talking - leave the group
s.leaveGroup(groupAddr); 
}
