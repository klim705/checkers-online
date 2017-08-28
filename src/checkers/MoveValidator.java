package checkers;

public class MoveValidator {
    public MoveValidator() {
    }

    public static boolean isMove(BoardCell fromCell, BoardCell toCell) {
        return (Math.abs(toCell.row - fromCell.row) == 1 && Math.abs(toCell.column - fromCell.column) == 1);
    }

    public static boolean isJump(BoardCell fromCell, BoardCell toCell) {
        return (Math.abs(toCell.row - fromCell.row) == 2 && Math.abs(toCell.column - fromCell.column) == 2);
    }

    public static boolean canMove(BoardCell fromCell, BoardCell toCell) {
        Piece piece = fromCell.getPiece();
        int r1 = fromCell.row;
        int c1 = fromCell.column;
        int r2 = toCell.row;
        int c2 = toCell.column;

        if (!fromCell.hasPiece() || toCell.hasPiece()) {
            return false;
        }

        if (piece.isKing()) {
            return Math.abs(r2 - r1) == 1 && Math.abs(c2 - c1) == 1;
        }
        if (piece.getColor() == Piece.Color.RED) {
            return r1 - r2 == 1 && Math.abs(c2 - c1) == 1;
        } else {
            return r2 - r1 == 1 && Math.abs(c2 - c1) == 1;
        }
    }

    public static boolean canJump(Piece pieceToJump, BoardCell fromCell, BoardCell toCell, BoardCell jumpedCell) {
        Piece pieceToCapture = jumpedCell.getPiece();
        int r1 = fromCell.row;
        int c1 = fromCell.column;
        int r2 = toCell.row;
        int c2 = toCell.column;

        if (pieceToCapture == null || (pieceToCapture.getColor() == pieceToJump.getColor()) || (toCell.hasPiece())) {
            return false;
        }

        if (pieceToJump.isKing()) {
            return Math.abs(r2 - r1) == 2 && Math.abs(c2 - c1) == 2;
        }
        if (pieceToJump.getColor() == Piece.Color.RED) {
            return r1 - r2 == 2 && Math.abs(c2 - c1) == 2;
        } else {
            return r2 - r1 == 2 && Math.abs(c2 - c1) == 2;
        }

    }
}
