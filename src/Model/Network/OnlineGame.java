package Model.Network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class OnlineGame extends Thread{

    InetAddress server;
    private DatagramSocket socket;
    private final static int portClient = 33333;
    private final static int portServer = 33334;
    boolean running = true;
    private byte[] buf = new byte[BUFFER_SIZE];
    private static final int BUFFER_SIZE = 8192;

    public OnlineGame (InetAddress s){
        server = s;
        try{
            socket = new DatagramSocket(portClient);
        }catch (Exception E){}
    }

    public void run(){
        //todo Lancer game CLIENT
        while (running){
            try {
                //todo buf prend input du joueur
                DatagramPacket packet = new DatagramPacket(buf, buf.length, server, portServer);
                socket.send(packet);
                buf = new byte[BUFFER_SIZE];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                //todo received VERS le jeu (nouvelles positions)
                buf = new byte[BUFFER_SIZE];


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
