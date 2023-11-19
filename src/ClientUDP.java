import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class ClientUDP {
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Entrez votre nom d'utilisateur: ");
            String username = scanner.nextLine();

            DatagramSocket clientSocket = new DatagramSocket();

            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        byte[] receiveData = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        clientSocket.receive(receivePacket);

                        String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        System.out.println(receivedMessage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            System.out.println("Connecté au serveur. Commencez à taper vos messages:");

            while (true) {
                String message = scanner.nextLine();
                String fullMessage = "[" + username + "]: " + message;

                byte[] sendData = fullMessage.getBytes();
                InetSocketAddress serverAddress = new InetSocketAddress("localhost", SERVER_PORT);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress);
                clientSocket.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
