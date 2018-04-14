package Model.Network;

//import org.aspectj.org.eclipse.jdt.internal.core.SourceType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Matthieu Le Boucher
 */

public class Client{
    private int port;
    static JFrame jFrame;
    private DatagramSocket socket;
    private List<String> nomServer = new ArrayList<>(0);
    private HashMap<String,InetAddress> addressServer = new HashMap<>(0);
    private static final String SERVERS = "getServers";
    private static final int BUFFER_SIZE = 8192;
    private static final int CLIENT_PORT  = 13584;
    private static final int SERVER_PORT  = 13594;
    private static final long DELTATIME = 1000;
    private byte[] buf = new byte[BUFFER_SIZE];

    public Client() {
        try {
            socket = new DatagramSocket(CLIENT_PORT);
            socket.setBroadcast(true);
            this.port = CLIENT_PORT;
            foundServers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void foundServers() throws Exception {
            System.out.println("1");
            buf = SERVERS.getBytes();
            final InetAddress[] address = {InetAddress.getByName("255.255.255.255")};
            final DatagramPacket packet = new DatagramPacket(buf, buf.length, address[0], SERVER_PORT);
            System.out.println("2");
            socket.send(packet);
            System.out.println("3");
            long begin = System.currentTimeMillis();
            Thread search = new Thread(){
                @Override
                public void run() {
                    super.run();
                    while (true){
                        try {
                            socket.receive(packet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        address[0] = packet.getAddress();

                        String received = new String(packet.getData(), 0, packet.getLength());
                        nomServer.add(received);
                        addressServer.put(received,address[0]);
                    }
                }
            };
            search.start();
            while (begin + DELTATIME > System.currentTimeMillis()){
                System.out.println("44444");
            }
            search.stop();
            System.out.println("5");
        }


    public void afficheServers(){
        jFrame.removeAll();
        JPanel jPanel = new JPanel(new GridLayout(nomServer.size()+1,1));
        JButton search = new JButton("Chercher les serveurs");
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    foundServers();
                    afficheServers();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        jPanel.add(search);
        for(int i = 0; i < nomServer.size(); ++i){
            JButton go = new JButton(nomServer.get(i));
            final int fI = i+1;
            go.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    accessServer(addressServer.get(nomServer.get(fI)));
                }
            });
            jPanel.add(go);

            System.out.println("Nom du serveur : "+nomServer.get(i));
            System.out.println("Adresse du serveur : "+addressServer.get(nomServer.get(i)));
            System.out.println();
        }
        System.out.println("NB Serv : "+nomServer.size());
        jFrame.add(jPanel);
        jPanel.validate();
        jFrame.validate();
        jFrame.setVisible(true);
    }

    public void accessServer(InetAddress server){
        System.out.println("Access server "+server);
        OnlineGame og = new OnlineGame(server);
        og.start();
    }

    public void openWindows() {
        jFrame = new JFrame();
        jFrame.setTitle("Client Side");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setBounds(0, 0, 300, 1000);
        afficheServers();
    }
}
