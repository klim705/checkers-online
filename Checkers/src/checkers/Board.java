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

    public boolean addRemoveCellToMove(int row, int column) {
        BoardCell cell = getCellAt(row, column);

        if (move.isEmpty()) {
            if (!cell.hasPiece()) {
                return false;
            }
            return move.add(cell);
        }

        BoardCell previousCell = move.get(move.size() - 1);
        if (previousCell.equals(cell)) {
            return move.removeCell(previousCell);
        }

        if (move.isValidMove()) {
            return false;
        }

        if (MoveValidator.isMove(previousCell, cell) && MoveValidator.canMove(previousCell, cell)) {
            return move.add(cell);
        }

        BoardCell cellToJumpOver = getCellAt((row + previousCell.row) / 2, (column + previousCell.column) / 2);
        if (MoveValidator.isJump(previousCell, cell) && MoveValidator.canJump(move.get(0).getPiece(), previousCell, cell, cellToJumpOver)) {
            return move.add(cell);
        }
        return false;
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
    }

    public void cancelMove() {
        move.clear();
    }
}
