package checkerstest;

import checkers.BoardCell;
import checkers.Piece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardCellTest {
    @org.junit.jupiter.api.Test
    void getColor() {
        BoardCell b = new BoardCell();
        assertNull(b.getColor());

        b = new BoardCell(0,0,BoardCell.Color.WHITE);
        assertEquals(b.getColor(), BoardCell.Color.WHITE);
    }

    @org.junit.jupiter.api.Test
    void hasPiece() {
        BoardCell b = new BoardCell();
        assertFalse(b.hasPiece());

        b = new BoardCell(0,0,BoardCell.Color.WHITE);
        Piece p = new Piece(Piece.Color.RED);
        b.setPiece(p);
        assertTrue(b.hasPiece());
    }

    @org.junit.jupiter.api.Test
    void getAndSetPiece() {
        BoardCell b = new BoardCell();
        assertNull(b.getPiece());

        b = new BoardCell(0,0,BoardCell.Color.WHITE);
        Piece p = new Piece(Piece.Color.RED);
        b.setPiece(p);
        assertEquals(b.getPiece(), p);
    }

    @org.junit.jupiter.api.Test
    void removePiece() {
        BoardCell b = new BoardCell(0,0,BoardCell.Color.WHITE);
        Piece p = new Piece(Piece.Color.RED);
        b.setPiece(p);
        assertEquals(b.getPiece(), p);

        b.removePiece();
        assertEquals(b.getPiece(), null);
    }

    @org.junit.jupiter.api.Test
    void toggleSetIsSelected() {
        BoardCell b1 = new BoardCell(0,0,BoardCell.Color.WHITE);
        assertFalse(b1.isSelected());
        b1.setSelected(true);
        assertTrue(b1.isSelected());
        b1.toggleSelected();
        assertFalse(b1.isSelected());
    }

    @org.junit.jupiter.api.Test
    void equals() {
        BoardCell b1 = new BoardCell(0,0,BoardCell.Color.WHITE);
        BoardCell b2 = new BoardCell(0,0,BoardCell.Color.WHITE);

        assertTrue(b1.equals(b2));

        BoardCell b3 = new BoardCell(2,7,BoardCell.Color.WHITE);
        BoardCell b4 = new BoardCell(1,8,BoardCell.Color.WHITE);

        assertFalse(b3.equals(b4));
    }

}