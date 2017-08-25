package checkers;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

public class CheckersClient {
    Client client;
    public static Board board;
    public static Piece.Color playerColor;
    private GUIManager guiManager;

    public CheckersClient(String host, int port) {
        client = new Client();
        client.start();

        Network.register(client);

        client.addListener(new Listener() {
            // code to run upon successfully connecting with the server
            public void connected(Connection connection) {
                Network.NewConnect nc = new Network.NewConnect();
                nc.newClient = "foo";
                client.sendTCP(nc);
            }

            public void received(Connection connection, Object object) {
                if (object instanceof Network.Player) {
                    // Receive the player's color from the server and set the client accordingly
                    Network.Player player = (Network.Player) object;
                    playerColor = player.playerColor;
                    return;
                }

                if (object instanceof Network.NetBoard) {
                    // Receive an updated board from the server and update the client's board
                    Network.NetBoard updateBoard = (Network.NetBoard) object;
                    if (board == null) {
                        if (guiManager == null) {
                            guiManager = new GUIManager();
                            guiManager.setSubmitListener(new Runnable() {
                                public void run() {
                                    Network.NetBoard netBoard = new Network.NetBoard();
                                    netBoard.board = board;
                                    client.sendTCP(netBoard);
                                }
                            });

                            guiManager.setForfeitListener(new Runnable() {
                                public void run() {
                                    Network.EndGame endGame = new Network.EndGame();
                                    endGame.reason = "forfeit";
                                    client.sendTCP(endGame);
                                }
                            });
                        }

                        board = updateBoard.board;
                        guiManager.setup(board);
                    } else {
                        board = updateBoard.board;
                        guiManager.refreshBoard();
                    }
                    return;
                }

                if (object instanceof Network.EndGame) {
                    Network.EndGame endGame = (Network.EndGame) object;
                    String reason = endGame.reason;
                    int result = JOptionPane.CLOSED_OPTION;

                    if (reason.equals("disconnect")) {
                        // display to the user that the other player disconnected
                        guiManager.showInfo("Your opponent disconnected. The game client will close when this box is closed.");
                    } else if (reason.equals("forfeit")) {
                        // tell the user that the other player forfeited and display the restart option
                        result = guiManager.showRestart("Your opponent forfeited");
                    } else if (reason.equals("selfforfeit")) {
                        result = guiManager.showRestart("You forfeited");
                    } else if (reason.equals("loss")) {
                        // tell the user that he/she lost and prompt for restart
                        result = guiManager.showRestart("You lost");
                    } else if (reason.equals("win")) {
                        // tell the user that he/she won and prompt for restart
                        result = guiManager.showRestart("You won");
                    } else if (reason.equals("full")) {
                        // display connection failure and exit the client
                        System.out.println("Failed to connect because a game is currently in progress. Please wait until the game ends.");
                    }
                    if (result == JOptionPane.OK_OPTION) {
                        Network.RestartGame restartGame = new Network.RestartGame();
                        restartGame.restart = true;
                        client.sendTCP(restartGame);
                    } else if (result == JOptionPane.NO_OPTION) {
                        Network.RestartGame restartGame = new Network.RestartGame();
                        restartGame.restart = false;
                        client.sendTCP(restartGame);
                        System.exit(0);
                    } else {
                        System.exit(0);
                    }
                }
            }

            public void disconnected(Connection connection) {
                if (guiManager != null) {
                    guiManager.cleanup();
                }
                System.exit(1);
            }
        });

        // start client
        new Thread(client).start();
        try {
            client.connect(5000, host, port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        String host;
        int port;
        if (args.length == 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        } else {
            System.out.println("Must provide host and port");
            return;
        }

        Log.set(Log.LEVEL_DEBUG);
        new CheckersClient(host, port);
    }

    static private class GUIManager extends JPanel implements ActionListener {
        private JFrame frame = new JFrame();
        private JPanel boardPanel = new JPanel();
        private JButton[][] boardButtons = new JButton[8][8];
        private JPanel hudPanel = new JPanel();
        private JPanel bottomButtonsPanel = new JPanel();
        private JPanel moveActionButtonsPanel = new JPanel();
        private JButton forfeitButton = new JButton("Forfeit");
        private JButton submitMoveButton = new JButton("OK");
        private JButton cancelMoveButton = new JButton("Cancel");
        private JPanel playerTurnPanel = new JPanel();
        private JLabel playerTurnLabel = new JLabel("Red's Turn");

        private ImageIcon greenSquareIcon = new ImageIcon("resources/OddEmpty.png");
        private ImageIcon whiteSquareIcon = new ImageIcon("resources/EvenEmpty.png");
        private ImageIcon selectedSquareIcon = new ImageIcon("resources/SelectedEmpty.png");

        private ImageIcon redPieceIcon = new ImageIcon("resources/RedMan.png");
        private ImageIcon blackPieceIcon = new ImageIcon("resources/BlackMan.png");
        private ImageIcon redKingIcon = new ImageIcon("resources/RedKing.png");
        private ImageIcon blackKingIcon = new ImageIcon("resources/BlackKing.png");
        private ImageIcon selectedRedPieceIcon = new ImageIcon("resources/SelectedRedMan.png");
        private ImageIcon selectedBlackPieceIcon = new ImageIcon("resources/SelectedBlackMan.png");
        private ImageIcon selectedRedKingIcon = new ImageIcon("resources/SelectedRedKing.png");
        private ImageIcon selectedBlackKingIcon = new ImageIcon("resources/SelectedBlackKing.png");

        private int showRestart(String title) {
            return JOptionPane.showConfirmDialog(frame,
                    "Would you like to restart the game?",
                    title,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
        }

        private void showInfo(String message) {
            JOptionPane.showMessageDialog(frame,
                    message,
                    "End game",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        private void setBoardCellIcon(int row, int column, BoardCell cell) {
            if (cell.hasPiece()) {
                if (cell.getPiece().getColor() == Piece.Color.RED) {
                    if (cell.getPiece().isKing()) {
                        boardButtons[row][column].setIcon(redKingIcon);
                    } else {
                        boardButtons[row][column].setIcon(redPieceIcon);
                    }
                } else {
                    if (cell.getPiece().isKing()) {
                        boardButtons[row][column].setIcon(blackKingIcon);
                    } else {
                        boardButtons[row][column].setIcon(blackPieceIcon);
                    }
                }
            } else {
                if (cell.getColor() == BoardCell.Color.WHITE) {
                    boardButtons[row][column].setIcon(whiteSquareIcon);
                } else {
                    boardButtons[row][column].setIcon(greenSquareIcon);
                }
            }
        }

        private void setSubmitButtonEnabled(boolean enabled) {
            submitMoveButton.setEnabled(enabled);
        }

        private void setMoveActionButtonsEnabled(boolean enabled) {
            setSubmitButtonEnabled(enabled);
            cancelMoveButton.setEnabled(enabled);
        }

        private void updateSelectedIcon(int row, int column) {
            BoardCell cell = board.getCellAt(row, column);
            cell.toggleSelected();

            if (!cell.hasPiece()) {
                if (cell.isSelected()) {
                    boardButtons[row][column].setIcon(selectedSquareIcon);
                } else {
                    boardButtons[row][column].setIcon(greenSquareIcon);
                }
                return;
            }
            Piece piece = cell.getPiece();

            if (piece.getColor() == Piece.Color.RED) {
                if (piece.isKing()) {
                    if (cell.isSelected()) {
                        boardButtons[row][column].setIcon(selectedRedKingIcon);
                    } else {
                        boardButtons[row][column].setIcon(redKingIcon);
                    }
                } else {
                    if (cell.isSelected()) {
                        boardButtons[row][column].setIcon(selectedRedPieceIcon);
                    } else {
                        boardButtons[row][column].setIcon(redPieceIcon);
                    }
                }
            } else {
                if (piece.isKing()) {
                    if (cell.isSelected()) {
                        boardButtons[row][column].setIcon(selectedBlackKingIcon);
                    } else {
                        boardButtons[row][column].setIcon(blackKingIcon);
                    }

                } else {
                    if (cell.isSelected()) {
                        boardButtons[row][column].setIcon(selectedBlackPieceIcon);
                    } else {
                        boardButtons[row][column].setIcon(blackPieceIcon);
                    }
                }
            }
        }

        public GUIManager() {
            frame.setSize(400, 500);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Online Checkers - Player " + playerColor);
            frame.setResizable(false);
        }

        public void setup(Board boardToSetup) {
            board = boardToSetup;
            Container cP = frame.getContentPane();
            cP.add(boardPanel, BorderLayout.CENTER);
            playerTurnPanel.add(playerTurnLabel);
            cancelMoveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    board.cancelMove();
                    refreshBoard();
                }
            });
            moveActionButtonsPanel.add(forfeitButton);
            moveActionButtonsPanel.add(submitMoveButton);
            moveActionButtonsPanel.add(cancelMoveButton);
            setMoveActionButtonsEnabled(false);
            bottomButtonsPanel.add(moveActionButtonsPanel, BorderLayout.EAST);
            hudPanel.add(bottomButtonsPanel, BorderLayout.CENTER);
            cP.add(playerTurnPanel, BorderLayout.NORTH);
            cP.add(hudPanel, BorderLayout.SOUTH);
            boardPanel.setLayout(new GridLayout(8, 8));

            for (int i = 0; i < board.cells.length; i++) {
                for (int j = 0; j < board.cells[i].length; j++) {
                    boardButtons[i][j] = new JButton("");
                    setBoardCellIcon(i, j, board.cells[i][j]);
                    boardButtons[i][j].addActionListener(this);
                    boardButtons[i][j].setActionCommand("" + i + "" + j);
                    boardPanel.add(boardButtons[i][j]);
                }
            }

            frame.setVisible(true);
        }

        public void refreshBoard() {
            for (int i = 0; i < board.cells.length; i++) {
                for (int j = 0; j < board.cells[i].length; j++) {
                    setBoardCellIcon(i, j, board.cells[i][j]);
                }
            }
            if (board.currentPlayer == Piece.Color.RED) {
                playerTurnLabel.setText("Red's Turn");
            } else {
                playerTurnLabel.setText("Black's Turn");
            }
        }

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            int row = Integer.parseInt("" + command.charAt(0));
            int column = Integer.parseInt("" + command.charAt(1));
            if (playerColor != board.currentPlayer) {
                return;
            }
            boolean result = board.addRemoveCellToMove(row, column);
            if (result) {
                updateSelectedIcon(row, column);
            }
            setMoveActionButtonsEnabled(board.isMoveSelected());
            setSubmitButtonEnabled(board.isMoveValid());
        }

        public void cleanup() {
            frame.dispose();
        }

        public void setSubmitListener(final Runnable listener) {
            submitMoveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    board.submitMove();
                    setMoveActionButtonsEnabled(false);
                    refreshBoard();
                    listener.run();
                }
            });
        }

        public void setForfeitListener(final Runnable listener) {
            forfeitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    setMoveActionButtonsEnabled(false);
                    listener.run();
                }
            });
        }
    }
}
