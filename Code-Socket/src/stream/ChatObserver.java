/***
 * ChatObserver
 * Help to relay message
 * Date: 13/10/2020
 * Authors: BRANCHEREAU Corentin, GRAVEY Thibaut
 */

package stream;

public interface ChatObserver {

    public void onClientMessage(ClientThread client, String msg);
    
    public void onClientConnection(ClientThread client);

    public void onClientDisconnetion(ClientThread client);
}
