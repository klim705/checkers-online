package checkers;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import java.io.IOException;

public class CheckersServer {
    Server server;
    int numPlayers = 0;
    int numRestarts = 0;  // keeps track of how many clients opted to restart the game

    public CheckersServer(int port) throws IOException {
        server = new Server() {
            protected Connection newConnection() {
                return new CheckersConnection();
            }
        };

        Network.register(server);

        server.addListener(new Listener() {
            public void received(Connection c, Object object) {
                CheckersConnection connection = (CheckersConnection) c;

                // code to be executed if there is new player connection
                if (object instanceof Network.NewConnect) {
                    if (numPlayers < 2) {
                        if (numPlayers == 0) {
                            // set player color red
                            // Pl
                            ((CheckersConnection) c).color = "red";
                        } else {
                            // set player color black
                            ((CheckersConnection) c).color = "black";
                        }
                        numPlayers++;
                        System.out.println(((CheckersConnection) c).color + " is connected");
                    } else {
                        // tell the client trying to connect that the game's full
                        System.out.println("Client attempted to connect to full game");
                        Network.EndGame endGame = new Network.EndGame();
                        endGame.reason = "full";
                        server.sendToTCP(connection.getID(), endGame);
                    }

                    return;
                }

                // code to be executed if the object received is a NetBoard
                if (object instanceof Network.NetBoard) {
                    System.out.println("Board update received");

                    // Relay the board to the other client
                    Network.NetBoard updateBoard = (Network.NetBoard) object;
                    server.sendToAllExceptTCP(connection.getID(), updateBoard);

                    return;
                }

                if (object instanceof Network.EndGame) {
                    Network.EndGame endGame = (Network.EndGame) object;

                    // relay the reason to the client for it to process
                    server.sendToAllExceptTCP(connection.getID(), endGame);

                    return;
                }

                if (object instanceof Network.RestartGame) {
                    Network.RestartGame restartGame = (Network.RestartGame) object;

                    if (restartGame.restart) {
                        numRestarts++;
                        if (numRestarts == 1) {
                            // other client still needs to decide to restart or not
                        } else if (numRestarts == 2) {
                            numRestarts = 0;  // reset the counter
                            // code to execute that will restart the game
                        }
                    }
                }
            }

            // code to be executed if a client disconnects
            public void disconnected(Connection c) {
                CheckersConnection connection = (CheckersConnection) c;
                if (connection.color != null) {
                    //insert code to be executed here
                    System.out.println(((CheckersConnection) c).color + " disconnected");
                }
            }
        });

        server.bind(port);
        server.start();

        System.out.println("Server started");
    }

    static class CheckersConnection extends Connection {
        public String color;
    }

    public static void main(String[] args) throws IOException {
        int port;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        } else {
            System.out.println("Must provide a port number");
            return;
        }

        Log.set(Log.LEVEL_DEBUG);
        new CheckersServer(port);
    }
}
