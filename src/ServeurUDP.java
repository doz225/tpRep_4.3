import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ServeurUDP {
    private static final int PORT = 1234;
    private static List<InetSocketAddress> clients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(PORT);
            System.out.println("Serveur UDP démarré sur le port " + PORT);

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                InetSocketAddress clientAddress = new InetSocketAddress(receivePacket.getAddress(), receivePacket.getPort());

                if (!clients.contains(clientAddress)) {
                    clients.add(clientAddress);
                    System.out.println("Nouveau client connecté: " + clientAddress);
                }

                broadcastMessage(message, clientAddress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void broadcastMessage(String message, InetSocketAddress sender) {
        for (InetSocketAddress client : clients) {
            if (!client.equals(sender)) {
                sendMessage(message, client);
            }
        }
    }

    private static void sendMessage(String message, InetSocketAddress client) {
        try {
            DatagramSocket socket = new DatagramSocket();
            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, client);
            socket.send(sendPacket);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
