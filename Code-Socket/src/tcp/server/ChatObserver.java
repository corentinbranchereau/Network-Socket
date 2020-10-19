/**
 * ChatObserver
 * An interface to relay message
 * Date: 13/10/2020
 * @author BRANCHEREAU Corentin
 * @author GRAVEY Thibaut
 */

package tcp.server;

public interface ChatObserver {

    /**
     *  event for a new tcp.client message
     * @param client the tcp.client thread that we listen to
     * @param msg the message passed
     */
    void onClientMessage(ClientThread client, String msg);

    /**
     *  event for a new tcp.client connection
     * @param client the tcp.client thread that we listen to
     */
    void onClientConnection(ClientThread client);

    /**
     *  event for tcp.client disconnection
     *  @param client the tcp.client thread that we listen to
     **/
    void onClientDisconnection(ClientThread client);
}
