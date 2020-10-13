/***
 * EchoClient
 * Example of a TCP client 
 * Date: 10/01/04
 * Authors:
 */

package stream;

public interface ChatObserver {

    public void onClientMessage(ClientThread client, String msg);
    
    public void onClientConnection(ClientThread client);

    public void onClientDisconnetion(ClientThread client);
}
