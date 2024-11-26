import java.net.ServerSocket;

public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(8901);
        System.out.println("Tic Tac Toe Server is Running");
        try {
            while (true) {
                ServerSidePlayer playerX = new ServerSidePlayer(listener.accept(), 'X');
                ServerSidePlayer playerO = new ServerSidePlayer(listener.accept(), 'O');
                ServerSideGameThreadLess game = new ServerSideGameThreadLess(playerX, playerO);
                game.start();
            }
        } finally {
            listener.close();
        }
    }
}

