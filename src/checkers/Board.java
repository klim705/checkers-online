package checkers;

public class Board {
    public BoardCell cells[][];
    public Piece.Color currentPlayer;
    public CheckersMove move;

    public Board() {
        cells = new BoardCell[8][8];
        currentPlayer = Piece.Color.RED;
        move = new CheckersMove();
        setupBoard();
    }

    public void setupBoard() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (Math.abs(i - j) % 2 == 0) {
                    cells[i][j] = new BoardCell(i, j, BoardCell.Color.WHITE);
                } else {
                    cells[i][j] = new BoardCell(i, j, BoardCell.Color.GREEN);
                    if (i < 3) {
                        cells[i][j].setPiece(new Piece(Piece.Color.BLACK));
                    }
                    if (i > 4) {
                        cells[i][j].setPiece(new Piece(Piece.Color.RED));
                    }

                }
            }
        }
    }

    public void resetBoard() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (Math.abs(i - j) % 2 != 0) {
                    cells[i][j] = new BoardCell(i, j, BoardCell.Color.GREEN);
                    if (i < 3) {
                        cells[i][j].setPiece(new Piece(Piece.Color.BLACK));
                    } else if (i > 4) {
                        cells[i][j].setPiece(new Piece(Piece.Color.RED));
                    } else {
                        cells[i][j].removePiece();
                    }
                }
            }
        }
        move.clear();
        currentPlayer = Piece.Color.RED;
    }

    public BoardCell getCellAt(int row, int column) {
        return cells[row][column];
    }

    public void movePiece(BoardCell fromCell, BoardCell toCell) {
        toCell.setPiece(fromCell.getPiece());
        if (toCell.row == cells.length - 1 || toCell.row == 0) {
            toCell.getPiece().setKing();
        }
        fromCell.removePiece();
    }

    public boolean addRemoveCellToMove(int row, int column, CheckersMove checkersMove) {
        BoardCell cell = getCellAt(row, column);

        if (checkersMove.isEmpty()) {
            if (!cell.hasPiece() || cell.getPiece().getColor() != currentPlayer) {
                return false;
            }
            return checkersMove.add(cell);
        }

        BoardCell previousCell = checkersMove.get(checkersMove.size() - 1);
        if (previousCell.equals(cell)) {
            return checkersMove.removeCell(previousCell);
        }

        if (checkersMove.isValidMove()) {
            return false;
        }

        if (MoveValidator.isMove(previousCell, cell) && MoveValidator.canMove(previousCell, cell)) {
            return checkersMove.add(cell);
        }

        BoardCell cellToJumpOver = getCellAt((row + previousCell.row) / 2, (column + previousCell.column) / 2);
        if (MoveValidator.isJump(previousCell, cell) && MoveValidator.canJump(checkersMove.get(0).getPiece(), previousCell, cell, cellToJumpOver)) {
            return checkersMove.add(cell);
        }
        return false;
    }

    public boolean addRemoveCellToMove(int row, int column) {
        return addRemoveCellToMove(row, column, move);
    }

    public boolean isMoveSelected() {
        return !move.isEmpty();
    }

    public boolean isMoveValid() {
        return move.isValid();
    }

    public void submitMove() {
        movePiece(move.first(), move.last());
        if (move.isValidJump()) {
            for (int i = 0; i < move.size() - 1; i++) {
                BoardCell previous = move.get(i);
                BoardCell next = move.get(i + 1);
                BoardCell toCapture = getCellAt((previous.row + next.row) / 2, (previous.column + next.column) / 2);
                toCapture.removePiece();
            }
        }
        move.clear();
        switchPlayerTurn();
    }

    public void switchPlayerTurn() {
        if (currentPlayer == Piece.Color.RED) {
            currentPlayer = Piece.Color.BLACK;
        } else {
            currentPlayer = Piece.Color.RED;
        }
    }

    public void cancelMove() {
        move.clear();
    }

    public boolean areAllCaptured(Piece.Color color) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (Math.abs(i - j) % 2 != 0) {
                    if (cells[i][j].hasPiece()) {
                        if (cells[i][j].getPiece().getColor() == color) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean canPlayerMove(Piece.Color color) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (Math.abs(i - j) % 2 != 0) {
                    if (cells[i][j].hasPiece()) {
                        if (cells[i][j].getPiece().getColor() == color) {
                            if (canPieceMove(cells[i][j])) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean canPieceMove(BoardCell boardCell) {
        int row = boardCell.row;
        int column = boardCell.column;
        int startRow = row - 2 >= 0 ? row - 2 : row - 1 >= 0 ? row - 1 : row;
        int endRow = row + 2 < cells.length ? row + 2 : row + 1 < cells.length ? row + 1 : row;
        int startColumn = column - 2 >= 0 ? column - 2 : column - 1 >= 0 ? column - 1 : column;
        int endColumn = column + 2 < cells.length ? column + 2 : column + 1 < cells.length ? column + 1 : column;
        CheckersMove tempMove = new CheckersMove();
        tempMove.add(boardCell);

        for (int i = startRow; i <= endRow; i++) {
            for (int j = startColumn; j <= endColumn; j++) {
                if (addRemoveCellToMove(i, j, tempMove)) {
                    return true;
                }
            }
        }
        return false;
    }
}
