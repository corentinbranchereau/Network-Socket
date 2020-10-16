/***
 * ChatObserver
 * An interface to relay message
 * Date: 13/10/2020
 * Authors: BRANCHEREAU Corentin, GRAVEY Thibaut
 */

package server;

public interface ChatObserver {

    /**
     *  event for a new client message
     * @param client ClientThread
     * @param msg String
     **/
    void onClientMessage(ClientThread client, String msg);

    /**
     *  event for a new client connection
     * @param client ClientThread
     **/
    void onClientConnection(ClientThread client);

    /**
     *  event for client disconnection
     *  @param client ClientThread
     **/
    void onClientDisconnection(ClientThread client);
}
