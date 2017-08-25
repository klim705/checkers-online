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
                            Network.Player networkPlayer = new Network.Player();
                            networkPlayer.playerColor = Piece.Color.RED;
                            server.sendToTCP(connection.getID(), networkPlayer);
                        } else {
                            // set player color black
                            ((CheckersConnection) c).color = "black";
                            Network.Player networkPlayer = new Network.Player();
                            networkPlayer.playerColor = Piece.Color.BLACK;
                            server.sendToTCP(connection.getID(), networkPlayer);
                        }
                        numPlayers++;
                        System.out.println(((CheckersConnection) c).color + " is connected");

                        if (numPlayers == 2) {
                            Network.NetBoard netBoard = new Network.NetBoard();
                            netBoard.board = new Board();
                            server.sendToAllTCP(netBoard);
                        }
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

                    // Relay the board to the other client
                    Network.NetBoard updateBoard = (Network.NetBoard) object;
                    server.sendToAllExceptTCP(connection.getID(), updateBoard);

                    Network.EndGame endGameRed = new Network.EndGame();
                    Network.EndGame endGameBlack = new Network.EndGame();
                    if (connection.color.equals("red")) {
                        if (updateBoard.board.areAllCaptured(Piece.Color.RED) || !updateBoard.board.canPlayerMove(Piece.Color.RED)) {
                            endGameRed.reason = "loss";
                            server.sendToTCP(connection.getID(), endGameRed);
                            endGameBlack.reason = "win";
                            server.sendToAllExceptTCP(connection.getID(), endGameBlack);
                        } else if (updateBoard.board.areAllCaptured(Piece.Color.BLACK) || !updateBoard.board.canPlayerMove(Piece.Color.BLACK)) {
                            endGameRed.reason = "win";
                            server.sendToTCP(connection.getID(), endGameRed);
                            endGameBlack.reason = "loss";
                            server.sendToAllExceptTCP(connection.getID(), endGameBlack);
                        }
                    } else {
                        if (updateBoard.board.areAllCaptured(Piece.Color.RED) || !updateBoard.board.canPlayerMove(Piece.Color.RED)) {
                            endGameRed.reason = "win";
                            server.sendToTCP(connection.getID(), endGameRed);
                            endGameBlack.reason = "loss";
                            server.sendToAllExceptTCP(connection.getID(), endGameBlack);
                        } else if (updateBoard.board.areAllCaptured(Piece.Color.BLACK) || !updateBoard.board.canPlayerMove(Piece.Color.BLACK)) {
                            endGameRed.reason = "loss";
                            server.sendToTCP(connection.getID(), endGameRed);
                            endGameBlack.reason = "win";
                            server.sendToAllExceptTCP(connection.getID(), endGameBlack);
                        }
                    }

                    return;
                }

                if (object instanceof Network.EndGame) {
                    Network.EndGame endGame = (Network.EndGame) object;

                    // relay the reason to the client for it to process
                    server.sendToAllExceptTCP(connection.getID(), endGame);
                    if (endGame.reason.equals("forfeit")) {
                        Network.EndGame selfForfeit = new Network.EndGame();
                        selfForfeit.reason = "selfforfeit";
                        server.sendToTCP(connection.getID(), selfForfeit);
                    }

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

                            Network.NetBoard netBoard = new Network.NetBoard();
                            netBoard.board = new Board();
                            server.sendToAllTCP(netBoard);
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
                    numPlayers--;

                    Network.EndGame endGame = new Network.EndGame();
                    endGame.reason = "disconnect";
                    server.sendToAllExceptTCP(connection.getID(), endGame);
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
