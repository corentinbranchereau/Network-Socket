/***
 * ChatObserver
 * Help to relay message
 * Date: 13/10/2020
 * Authors: BRANCHEREAU Corentin, GRAVEY Thibaut
 */

package server;

public interface ChatObserver {

    void onClientMessage(ClientThread client, String msg);
    
    void onClientConnection(ClientThread client);

    void onClientDisconnection(ClientThread client);
}
