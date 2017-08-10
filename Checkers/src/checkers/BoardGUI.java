package checkers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardGUI extends JPanel implements ActionListener {
    private Board board;
    private JFrame frame = new JFrame();
    private JPanel boardPanel = new JPanel();
    private JButton[][] boardButtons = new JButton[8][8];
    private JPanel hudPanel = new JPanel();
    private JPanel bottomButtonsPanel = new JPanel();
    private JPanel moveActionButtonsPanel = new JPanel();
    private JButton newGameButton = new JButton("New Game");
    private JButton submitMoveButton = new JButton("Apply");
    private JButton cancelMoveButton = new JButton("Cancel");
    private JPanel playerTurnPanel = new JPanel();
    private JLabel playerTurnLabel = new JLabel("Player 1's Turn");

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

    public BoardGUI() {
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Online Checkers");
        //this.pack();
        frame.setResizable(false);
    }

    public void setup(Board board) {
        this.board = board;
        Container cP = frame.getContentPane();
        cP.add(boardPanel, BorderLayout.CENTER);
        playerTurnPanel.add(playerTurnLabel);
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.resetBoard();
                refreshBoard();
            }
        });
        bottomButtonsPanel.add(newGameButton, BorderLayout.WEST);
        submitMoveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.submitMove();
                setMoveActionButtonsEnabled(false);
                refreshBoard();
            }
        });
        cancelMoveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.cancelMove();
                refreshBoard();
            }
        });
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
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        int row = Integer.parseInt("" + command.charAt(0));
        int column = Integer.parseInt("" + command.charAt(1));
        boolean result = board.addRemoveCellToMove(row, column);
        if (result) {
            updateSelectedIcon(row, column);
        }
        setMoveActionButtonsEnabled(board.isMoveSelected());
        setSubmitButtonEnabled(board.isMoveValid());
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
}
