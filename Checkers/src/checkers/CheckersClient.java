import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import sun.nio.ch.Net;

import java.io.IOException;
import java.util.Scanner;

public class CheckersClient {
    Client client;
    public static Board board;
    public static Piece.Color playerColor;

    public CheckersClient(String host, int port) {
        client = new Client();
        client.start();

        Network.register(client);

        client.addListener(new Listener() {
            // code to run upon successfully connecting with the server
            public void connected(Connection connection) {
                System.out.println("Connected to server");
                Network.NewConnect nc = new Network.NewConnect();
                nc.newClient = "foo";
                client.sendTCP(nc);
            }

            public void received(Connection connection, Object object) {
                if(object instanceof Network.Player) {
                    // Receive the player's color from the server and set the client accordingly
                    Network.Player player = (Network.Player)object;
                    playerColor = player.playerColor;
                    return;
                }

                if(object instanceof Network.NetBoard) {
                    // Receive an updated board from the server and update the client's board
                    Network.NetBoard updateBoard = (Network.NetBoard)object;
                    board = updateBoard.board;
                    System.out.println("Client board updated");
                    return;
                }

                if(object instanceof Network.EndGame) {
                    Network.EndGame endGame = (Network.EndGame)object;
                    String reason = endGame.reason;

                    if(reason.equals("disconnect")) {
                        // display to the user that the other player disconnected
                    }
                    else if(reason.equals("forfeit")) {
                        // tell the user that the other player forfeited and display the restart option
                    }
                    else if(reason.equals("loss")) {
                        // tell the user that he/she lost and prompt for restart
                    }
                    else if(reason.equals("win")) {
                        // tell the user that he/she won and prompt for restart
                    }
                    else if(reason.equals("full")) {
                        // display connection failure and exit the client
                    }

                    return;
                }
            }

            public void disconnected(Connection connection) {
                System.out.println("Disconnected from server");
            }
        });

        // start client
        new Thread(client).start();
        try {
            client.connect(5000, host, port);
        }
        catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // insert client side checkers code here
    }

    public static void main(String[] args) {
        String host;
        int port;
        if(args.length == 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }
        else {
            System.out.println("Must provide host and port");

            return;
        }

        Log.set(Log.LEVEL_DEBUG);
        new CheckersClient(host, port);
    }
}
