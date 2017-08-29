package checkerstest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckersMoveTest {
    @Test
    void isValidMove() {
        CheckersMove checkersMove = new CheckersMove();
        assertFalse(checkersMove.isValid());
        assertFalse(checkersMove.isValidMove());
        assertFalse(checkersMove.isValidJump());
        
        BoardCell fromCell = new BoardCell(0, 0, BoardCell.GREEN);
        fromCell.setPiece(new Piece(Piece.RED));
        checkersMove.add(fromCell);
        assertFalse(checkersMove.isValid());
        assertFalse(checkersMove.isValidMove());
        assertFalse(checkersMove.isValidJump());
        
        BoardCell toCell = new BoardCell(1, 1, BoardCell.GREEN);
        toCell.setPiece(null);
        checkersMove.add(toCell);
        assertTrue(checkersMove.isValid());
        assertTrue(checkersMove.isValidMove());
        assertFalse(checkersMove.isValidJump());
        
    }

    @Test
    void isValidJump() {
        CheckersMove checkersMove = new CheckersMove();
        assertFalse(checkersMove.isValid());
        assertFalse(checkersMove.isValidMove());
        assertFalse(checkersMove.isValidJump());
        
        BoardCell fromCell = new BoardCell(0, 0, BoardCell.GREEN);
        fromCell.setPiece(new Piece(Piece.RED));
        checkersMove.add(fromCell);
        assertFalse(checkersMove.isValid());
        assertFalse(checkersMove.isValidMove());
        assertFalse(checkersMove.isValidJump());
        
        BoardCell toCell = new BoardCell(2, 2, BoardCell.GREEN);
        toCell.setPiece(null);
        checkersMove.add(toCell);
        assertFalse(checkersMove.isValid());
        assertTrue(checkersMove.isValidMove());
        assertFalse(checkersMove.isValidJump());
    }

    @Test
    void add() {
        CheckersMove checkersMove = new CheckersMove();
        assertEquals(checkersMove.move.size(), 0);
        BoardCell fromCell = new BoardCell(0, 0, BoardCell.GREEN);
        checkersMove.add(fromCell);
        assertEquals(checkersMove.move.size(), 1);
        assertEquals(checkersMove.move.get(0), fromCell);
    }

    @Test
    void get() {
        CheckersMove checkersMove = new CheckersMove();
        BoardCell fromCell = new BoardCell(0, 0, BoardCell.GREEN);
        BoardCell toCell = new BoardCell(1, 1, BoardCell.GREEN);
        checkersMove.add(fromCell);
        checkersMove.add(toCell);
        assertEquals(checkersMove.get(0), fromCell);
        assertEquals(checkersMove.get(0), toCell);
    }

    @Test
    void removeCell() {
        CheckersMove checkersMove = new CheckersMove();
        BoardCell fromCell = new BoardCell(0, 0, BoardCell.GREEN);
        BoardCell toCell = new BoardCell(1, 1, BoardCell.GREEN);
        checkersMove.add(fromCell);
        checkersMove.add(toCell);
        assertEquals(checkersMove.move.size(), 2);
        checkersMove.remove(fromCell);
        assertEquals(checkersMove.move.size(), 1);
    }

    @Test
    void size() {
        CheckersMove checkersMove = new CheckersMove();
        BoardCell fromCell = new BoardCell(0, 0, BoardCell.GREEN);
        BoardCell toCell = new BoardCell(1, 1, BoardCell.GREEN);
        checkersMove.add(fromCell);
        assertEquals(checkersMove.size(), 1);
        checkersMove.add(toCell);
        assertEquals(checkersMove.size(), 2);
    }

    @Test
    void isEmpty() {
        CheckersMove checkersMove = new CheckersMove();
        assertTrue(checkersMove.isEmpty());
        BoardCell fromCell = new BoardCell(0, 0, BoardCell.GREEN);
        checkersMove.add(fromCell);
        assertFalse(checkersMove.isEmpty());
        checkersMove.remove(fromCell);
        assertTrue(checkersMove.isEmpty());
    }

    @Test
    void first() {
        CheckersMove checkersMove = new CheckersMove();
        BoardCell fromCell = new BoardCell(0, 0, BoardCell.GREEN);
        BoardCell toCell = new BoardCell(1, 1, BoardCell.GREEN);
        checkersMove.add(fromCell);
        assertEquals(checkersMove.first(), fromCell);
        checkersMove.add(toCell);
        assertEquals(checkersMove.first(), fromCell);
    }

    @Test
    void last() {
        CheckersMove checkersMove = new CheckersMove();
        BoardCell fromCell = new BoardCell(0, 0, BoardCell.GREEN);
        BoardCell toCell = new BoardCell(1, 1, BoardCell.GREEN);
        checkersMove.add(fromCell);
        assertEquals(checkersMove.last(), fromCell);
        checkersMove.add(toCell);
        assertEquals(checkersMove.last(), toCell);
    }

    @Test
    void clear() {
        CheckersMove checkersMove = new CheckersMove();
        BoardCell fromCell = new BoardCell(0, 0, BoardCell.GREEN);
        BoardCell toCell = new BoardCell(1, 1, BoardCell.GREEN);
        checkersMove.add(fromCell);
        checkersMove.add(toCell);
        assertFalse(checkersMove.move.isEmpty());
        checkersMove.clear();
        assertTrue(checkersMove.move.isEmpty());
    }
}