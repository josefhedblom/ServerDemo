import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Player {

    public Player() {

        // byt detta till serverns ip eller kolla om det funkar med datorns egna ip
        try (Socket socket = new Socket("172.20.204.33", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader playerIn = new BufferedReader(new InputStreamReader(System.in))) {
            String fromPlayer;
            String fromServer;

            while ((fromServer = in.readLine()) != null) {
                System.out.print(fromServer);
                fromPlayer = playerIn.readLine();
                if (fromPlayer != null){
                    out.println(fromPlayer);
                }
            }

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String myIpAdress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println("Kunde inte hämta IP-adress: " + e.getMessage());
        }
        return "127.0.0.1";
    }

    public static void main(String[] args) {
        new Player();
    }
}
