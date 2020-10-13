/***
 * ClientGUI
 * A simple GUI for client side chat system
 * Date: 13/10/20
 * Authors: BRANCHEREAU Corentin, GRAVEY Thibaut
 */

package client;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ClientGUI implements ActionListener, WindowListener {

    private JButton connect;
    private JButton send;
    private JTextField tfEnter;

    public ClientGUI(){
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
        JTextField tfHost = new JTextField(15);
        JTextField tfPort = new JTextField(15);
        JTextField tfPseudo = new JTextField(15);
        connect = new JButton("Connect");
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
        JTextArea ta = new JTextArea();

        //Bottom side
        JPanel panelBottom = new JPanel();
        JLabel labelEnter = new JLabel("Text Input");
        tfEnter = new JTextField(30);
        send = new JButton("Send");
        panelBottom.add(labelEnter);
        panelBottom.add(tfEnter);
        panelBottom.add(send);

        //Listener
        connect.addActionListener(this);
        send.addActionListener(this);
        tfEnter.addActionListener(this);

        //Add Components
        frame.getContentPane().add(BorderLayout.SOUTH, panelBottom);
        frame.getContentPane().add(BorderLayout.CENTER,ta);
        frame.getContentPane().add(BorderLayout.NORTH,panelTop);

        //Display GUI
        frame.setVisible(true);
    }

    public static void main(String[] args){
        ClientGUI clientGUI = new ClientGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source instanceof JButton){
            JButton toCompare = (JButton) source;
            if(toCompare==connect){
                System.out.println("Connect");
            } else if (toCompare==send){
                System.out.println("Send !");
            }
        } else if (source instanceof JTextField){
            JTextField toCompare = (JTextField) source;
            if(toCompare==tfEnter){
                System.out.println("Enter in Text Input !");
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
