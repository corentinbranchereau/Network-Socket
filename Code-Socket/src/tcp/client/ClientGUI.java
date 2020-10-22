package tcp.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * ClientGUI
 * A simple GUI for TCP tcp.client side chat system
 * Date: 13/10/20
 * @author BRANCHEREAU Corentin
 * @author GRAVEY Thibaut
 */
public class ClientGUI implements ActionListener, WindowListener {

    /**
     * Client state for TCP connection
     */
    private boolean connected;

    /**
     * Connection button
     */
    private JButton connect;
    /**
     * Send a message button
     */
    private JButton send;
    /**
     * Message input textfield
     */
    private JTextField tfEnter;
    /**
     * Host input textfield
     */
    private JTextField tfHost;
    /**
     * Port input textfield
     */
    private JTextField tfPort;
    /**
     * Name of the client input textfield
     */
    private JTextField tfPseudo;
    /**
     * Area to display message from the chat system
     */
    private JTextArea textArea;

    private static Socket echoSocket;
    private static DataOutputStream socOut;
    private static BufferedReader socIn;

    /**
     *  Create the entire GUI with Swing
     */
    public ClientGUI(){
        connected = false;

        //Create the frame
        JFrame frame = new JFrame("B01 Chat System");

        //Properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        GridLayout topLayout = new GridLayout(3,3);

        //Create Components
        //Top side
        JPanel panelTop = new JPanel();
        JPanel subTop = new JPanel();
        JPanel subBot = new JPanel();
        JLabel labelHost = new JLabel("Host");
        JLabel labelPort = new JLabel("Port");
        JLabel labelPseudo = new JLabel("Pseudo");
        tfHost = new JTextField(15);
        tfPort = new JTextField(15);
        tfPseudo = new JTextField(15);
        connect = new JButton("Connect");
        tfHost.setText("127.0.0.1");
        tfPort.setText("1024");
        labelHost.setHorizontalAlignment(JLabel.CENTER);
        labelPort.setHorizontalAlignment(JLabel.CENTER);
        labelPseudo.setHorizontalAlignment(JLabel.CENTER);
        subTop.setLayout(topLayout);
        subTop.add(labelHost);
        subTop.add(tfHost);
        subTop.add(labelPort);
        subTop.add(tfPort);
        subTop.add(labelPseudo);
        subTop.add(tfPseudo);
        subBot.add(connect);
        panelTop.add(BorderLayout.CENTER,subTop);
        panelTop.add(BorderLayout.SOUTH,subBot);

        //Center side
        textArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(textArea);

        //Bottom side
        JPanel panelBottom = new JPanel();
        JLabel labelEnter = new JLabel("Text Input");
        tfEnter = new JTextField(30);
        send = new JButton("Send");
        panelBottom.add(labelEnter);
        panelBottom.add(tfEnter);
        panelBottom.add(send);
        send.setEnabled(false);
        tfEnter.setEnabled(false);

        //Listener
        connect.addActionListener(this);
        send.addActionListener(this);
        tfEnter.addActionListener(this);
        tfPseudo.addActionListener(this);

        //Add Components
        frame.getContentPane().add(BorderLayout.SOUTH, panelBottom);
        frame.getContentPane().add(BorderLayout.CENTER,scroll);
        frame.getContentPane().add(BorderLayout.NORTH,panelTop);

        //Display GUI
        frame.setVisible(true);
    }

    /**
     *  Launch the GUI
     * @param args empty
     */
    public static void main(String[] args){
        new ClientGUI();
    }

    /**
     *  Action performed from the different listeners, for example
     *  listening to button and text input to interact with socket and GUI
     * @param e linked event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source instanceof JButton){
            JButton toCompare = (JButton) source;
            if(toCompare==connect){
                if(!(tfPseudo.getText().isEmpty() || tfPseudo.getText().isBlank())){
                    System.out.println("Connect");
                    if(!connected) {
                        startSocket(tfHost.getText(), tfPort.getText(), tfPseudo.getText());
                    } else {
                        sendDisconnection();
                    }
                } else {
                    infosBox("You can't enter the chat with an empty pseudo.","Pseudo");
                }

            } else if (toCompare==send){
                if(tfEnter.getText().isEmpty()){
                    infosBox("You can't send an empty message","Empty message");
                } else {
                    System.out.println("Send Message : " + tfEnter.getText());
                    sendMessage(tfEnter.getText());
                    tfEnter.selectAll();
                    tfEnter.replaceSelection("");
                }
            }
        } else if (source instanceof JTextField){
            JTextField toCompare = (JTextField) source;
            if(toCompare==tfEnter){
                System.out.println("Enter in Text Input !");
                sendMessage(tfEnter.getText());
                tfEnter.selectAll();
                tfEnter.replaceSelection("");
            } else if(toCompare==tfPseudo){
                System.out.println("Enter in Pseudo Input !");
                if(!(tfPseudo.getText().isEmpty() || tfPseudo.getText().isBlank())){
                    System.out.println("Connect");
                    if(!connected) {
                        startSocket(tfHost.getText(), tfPort.getText(), tfPseudo.getText());
                    } else {
                        sendDisconnection();
                    }
                } else {
                    infosBox("You can't enter the chat with an empty pseudo.","Pseudo");
                }
            }
        }
    }

    /**
     *  Start the tcp.client socket for tcp.server communication
     * @param host adress from the host tcp.server
     * @param port port from the host tcp.server
     * @param name tcp.client name
     */
    public void startSocket(String host, String port, String name){
        boolean error = false;
        try {
            // creation socket ==> connexion
            echoSocket = new Socket(host,new Integer(port).intValue());
            socIn = new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            socOut = new DataOutputStream(echoSocket.getOutputStream());
            socOut.writeUTF(name);
            socOut.flush();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + host);
            infosBox("Don't know about host : "+host,"Host");
            error = true;
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ host);
            infosBox("Couldn't get I/O for the connection to : "+host,"I/O");
            error = true;
        }

        if(!error) {
            ServerListenerThread serverListenerThread = new ServerListenerThread(echoSocket, socIn, textArea);
            serverListenerThread.start();

            connected = true;
            tfHost.setEnabled(false);
            tfPort.setEnabled(false);
            tfPseudo.setEnabled(false);
            connect.setText("Disconnect");
            send.setEnabled(true);
            tfEnter.setEnabled(true);
        }
    }

    /**
     *  Send a message using the output stream of the socket
     *  @param msg the message before the generation of the protocol
     */
    private void sendMessage(String msg){
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
     *  Send a disconnection message to prevent the TCP tcp.server
     */
    public void sendDisconnection(){
        int protocolType = 1;

        try {
            socOut.writeInt(protocolType);
            socOut.flush();

            socOut.close();
            socIn.close();
            echoSocket.close();
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ echoSocket.getInetAddress());
            System.exit(1);
        }

        tfHost.setEnabled(true);
        tfPort.setEnabled(true);
        tfPseudo.setEnabled(true);
        connect.setText("Connect");
        send.setEnabled(false);
        tfEnter.setEnabled(false);

        connected = false;
    }

    /**
     *  Alert the tcp.client from a bad GUI input with a popup
     */
    private void infosBox(String infoMessage, String titleBar){
        JOptionPane.showMessageDialog(null,infoMessage,"Error : "+titleBar,JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Not used
     */
    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     *  Close application when window is closing
     */
    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("Fermeture de l'application, socket connecté : "+connected);
        if(connected){
            sendDisconnection();
        }
    }

    /**
     * Not used
     */
    @Override
    public void windowClosed(WindowEvent e) {

    }

    /**
     * Not used
     */
    @Override
    public void windowIconified(WindowEvent e) {

    }

    /**
     * Not used
     */
    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    /**
     * Not used
     */
    @Override
    public void windowActivated(WindowEvent e) {

    }

    /**
     * Not used
     */
    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
