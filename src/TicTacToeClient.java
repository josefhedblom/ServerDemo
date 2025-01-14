import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A client for the TicTacToe game, modified and extended from the
 * class presented in Deitel and Deitel "Java How to Program" book.
 * I made a bunch of enhancements and rewrote large sections of the
 * code. In particular I created the TTTP (Tic Tac Toe Protocol)
 * which is entirely text based. Here are the strings that are sent:
 *
 * Client -> Server        Server -> Client
 * ----------------------  ----------
 * MOVE <n> (0 <= n <= 8)  WELCOME <char> (char in {X, O})
 * QUIT                    VALID_MOVE
 *                         OTHER_PLAYER_MOVED <n>
 *                         VICTORY
 *                         DEFEAT
 *                         TIE
 *                         MESSAGE <text>
 *
 */
public class TicTacToeClient {

    private JFrame frame = new JFrame("Tic Tac Toe");
    private JLabel messageLabel = new JLabel("");

    private Square[] board = new Square[9];
    private Square currentSquare = new Square();

    private static int PORT = 8901;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private boolean blocked = false;

    /**
     * Constructs the client by connecting to a server, laying out the
     * GUI and registering GUI listeners.
     */
    public TicTacToeClient(String serverAddress) throws Exception {

        socket = new Socket(serverAddress, PORT);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Layout GUI
        messageLabel.setBackground(Color.lightGray);
        frame.getContentPane().add(messageLabel, "South");

        JPanel boardPanel = new JPanel();
        boardPanel.setBackground(Color.black);
        boardPanel.setLayout(new GridLayout(3, 3, 2, 2));
        for (int i = 0; i < board.length; i++) {
            final int j = i;
            board[i] = new Square();
            board[i].addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    currentSquare = board[j];
                    //System.out.println("sending MOVE "+j+" "+ i);
                    if (!blocked)
                        out.println("MOVE " + j);}});

            boardPanel.add(board[i]);
        }
        frame.getContentPane().add(boardPanel, "Center");
    }

    /**
     * The main thread of the client will listen for messages
     * from the server. The first message will be a "WELCOME"
     * message in which we receive our mark. Then we go into a
     * loop listening for "VALID_MOVE", "OPPONENT_MOVED", "VICTORY",
     * "DEFEAT", "TIE", "OPPONENT_QUIT or "MESSAGE" messages,
     * and handling each message appropriately. The "VICTORY",
     * "DEFEAT" and "TIE" ask the user whether or not to play
     * another game. If the answer is no, the loop is exited and
     * the server is sent a "QUIT" message. If an OPPONENT_QUIT
     * message is recevied then the loop will exit and the server
     * will be sent a "QUIT" message also.
     */
    public void play() throws Exception {
        String response;
        char mark = 'S';
        char opponentMark = 'P';
        try {
            response = in.readLine();
            if (response.startsWith("WELCOME")) {
                mark = response.charAt(8);
                if (mark == 'O') blocked = true;
                opponentMark = (mark == 'X' ? 'O' : 'X');
                frame.setTitle("Tic Tac Toe - Player " + mark);
            }
            while (true) {
                response = in.readLine();
                if (response.startsWith("VALID_MOVE")) {
                    messageLabel.setText("Valid move, please wait");
                    currentSquare.setText(String.valueOf(mark));
                    currentSquare.repaint();
                    blocked = true;
                } else if (response.startsWith("OPPONENT_MOVED")) {
                    int loc = Integer.parseInt(response.substring(15));
                    board[loc].setText(String.valueOf(opponentMark));
                    board[loc].repaint();
                    messageLabel.setText("Opponent moved, your turn");
                    blocked = false;
                } else if (response.startsWith("VICTORY")) {
                    messageLabel.setText("You win");
                    break;
                } else if (response.startsWith("DEFEAT")) {
                    messageLabel.setText("You lose");
                    break;
                } else if (response.startsWith("TIE")) {
                    messageLabel.setText("You tied");
                    break;
                } else if (response.startsWith("MESSAGE")) {
                    messageLabel.setText(response.substring(8));
                }
            }
            out.println("QUIT");
        }
        finally {
            socket.close();
        }
    }

    private boolean wantsToPlayAgain() {
        int response = JOptionPane.showConfirmDialog(frame,
                "Want to play again?",
                "Tic Tac Toe is Fun Fun Fun",
                JOptionPane.YES_NO_OPTION);
        frame.dispose();
        return response == JOptionPane.YES_OPTION;
    }

    /**
     * Graphical square in the client window. Each square is
     * a white panel containing. A client calls setText() to fill
     * it with an X or O.
     */
    class Square extends JPanel {
        JLabel label = new JLabel();

        public Square() {
            setBackground(Color.white);
            add(label);
        }

        public void setText(String s) {
            label.setText(s);
        }
    }

    /**
     * Runs the client as an application.
     */
    public static void main(String[] args) throws Exception {
        while (true) {
            String serverAddress = (args.length == 0) ? "localhost" : args[1];
            TicTacToeClient client = new TicTacToeClient(serverAddress);
            client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.frame.setSize(240, 160);
            client.frame.setVisible(true);
            client.frame.setResizable(true);
            client.play();
            if (!client.wantsToPlayAgain()) {
                break;
            }
        }
    }
}