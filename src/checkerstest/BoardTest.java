package checkerstest;

import checkers.Board;
import checkers.BoardCell;
import checkers.CheckersMove;
import checkers.Piece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @org.junit.jupiter.api.Test
    void setupBoard()
    {

        Board b = new Board();
        assertEquals(b.cells.length,8);
    }

    @org.junit.jupiter.api.Test
    void resetBoard()
    {
        Board b = new Board();
        b.resetBoard();
        assertEquals(b.cells.length,8);
    }

    @org.junit.jupiter.api.Test
    void getCellAt()
    {
        Board b = new Board();
        assertEquals(b.getCellAt(0,0), b.cells[0][0]);
        assertNotEquals(b.getCellAt(2,0), b.cells[3][0]);
    }

    @org.junit.jupiter.api.Test
    void movePiece()
    {
        Board b = new Board();
        BoardCell b1 = new BoardCell(2,3,BoardCell.Color.GREEN);
        Piece p = new Piece(Piece.Color.RED);
        b1.setPiece(p);
        BoardCell b2 = new BoardCell(3,4,BoardCell.Color.GREEN);
        b.movePiece(b1, b2);

        assertEquals(b1.getPiece(), null);
        assertEquals(b2.getPiece(), p);
    }

    @org.junit.jupiter.api.Test
    void addRemoveCellToMove()
    {
        Board b = new Board();
        assertFalse(b.addRemoveCellToMove(3,2));
    }


    @org.junit.jupiter.api.Test
    void isMoveSelected()
    {
        Board b = new Board();
        assertFalse(b.isMoveSelected());
    }

    @org.junit.jupiter.api.Test
    void isMoveValid()
    {
        Board b = new Board();
        assertFalse(b.isMoveValid());
    }

    @org.junit.jupiter.api.Test
    void submitMove()
    {
        Board b = new Board();
        assertEquals(b.currentPlayer, Piece.Color.RED);
        //b.submitMove();
        assertNotEquals(b.currentPlayer, Piece.Color.BLACK);
    }

    @org.junit.jupiter.api.Test
    void switchPlayerTurn()
    {
        Board b = new Board();
        assertEquals(b.currentPlayer, Piece.Color.RED);
        b.switchPlayerTurn();
        assertEquals(b.currentPlayer, Piece.Color.BLACK);
    }

    @org.junit.jupiter.api.Test
    void cancelMove()
    {
        CheckersMove m = new CheckersMove();
        Board b = new Board();
        b.cancelMove();

    }

    @org.junit.jupiter.api.Test
    void areAllCaptured()
    {
        Board b = new Board();
        assertFalse(b.areAllCaptured(Piece.Color.RED));
    }

    @org.junit.jupiter.api.Test
    void canPlayerMove()
    {
        Board b = new Board();
        assertTrue(b.canPlayerMove(Piece.Color.RED));
    }

}