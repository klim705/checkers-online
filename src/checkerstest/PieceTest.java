package checkerstest;

import checkers.Piece;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
    @org.junit.jupiter.api.Test
    void constructorHasDefaults() {
        Piece p = new Piece();
        assertNull(p.getColor());
        assertFalse(p.isKing());
        p = new Piece(Piece.Color.RED);
        assertEquals(p.getColor(), Piece.Color.RED);
        assertFalse(p.isKing());
    }

    @org.junit.jupiter.api.Test
    void getColor() {
        Piece p = new Piece();
        assertNull(p.getColor());
        p = new Piece(Piece.Color.RED);
        assertEquals(p.getColor(), Piece.Color.RED);
    }

    @org.junit.jupiter.api.Test
    void canCheckIfAndSetKing() {
        Piece p = new Piece();
        assertFalse(p.isKing());
        p.setKing();
        assertTrue(p.isKing());
    }
}