package checkerstest;

import checkers.BoardCell;
import checkers.MoveValidator;
import checkers.Piece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveValidatorTest {
    @org.junit.jupiter.api.Test
    void isMoveTest()
    {
        MoveValidator mv = new MoveValidator();
        BoardCell b1 = new BoardCell(0,2,BoardCell.Color.GREEN);
        BoardCell b2 = new BoardCell(1,2,BoardCell.Color.GREEN);
        assertFalse(MoveValidator.isMove(b1,b2));

        BoardCell b3 = new BoardCell(3,6,BoardCell.Color.GREEN);
        BoardCell b4 = new BoardCell(4,7,BoardCell.Color.GREEN);
        assertTrue(MoveValidator.isMove(b3,b4));
    }

    @Test
    void isJumpTest()
    {
        MoveValidator mv = new MoveValidator();
        BoardCell b1 = new BoardCell(0,3,BoardCell.Color.GREEN);
        BoardCell b2 = new BoardCell(2,6,BoardCell.Color.GREEN);
        assertFalse(MoveValidator.isJump(b1,b2));

        BoardCell b3 = new BoardCell(2,3,BoardCell.Color.GREEN);
        BoardCell b4 = new BoardCell(4,5,BoardCell.Color.GREEN);
        assertTrue(MoveValidator.isJump(b3,b4));
    }

    @Test
    void canMoveTest()
    {
        MoveValidator mv = new MoveValidator();
        Piece p = new Piece(Piece.Color.BLACK);
        BoardCell b1 = new BoardCell(0,2,BoardCell.Color.GREEN);
        b1.setPiece(p);
        BoardCell b2 = new BoardCell(1,2,BoardCell.Color.GREEN);
        assertFalse(MoveValidator.canMove(b1,b2));

        BoardCell b3 = new BoardCell(2,3,BoardCell.Color.GREEN);
        b3.setPiece(p);
        BoardCell b4 = new BoardCell(3,4,BoardCell.Color.GREEN);
        assertTrue(MoveValidator.canMove(b3,b4));
    }

    @Test
    void canJumpTest()
    {
        MoveValidator mv = new MoveValidator();
        Piece pB = new Piece(Piece.Color.BLACK);
        Piece pR = new Piece(Piece.Color.RED);

        BoardCell b1 = new BoardCell(2,3,BoardCell.Color.GREEN);
        b1.setPiece(pB);
        BoardCell b2 = new BoardCell(3,4,BoardCell.Color.GREEN);
        b2.setPiece(pB);
        BoardCell b3 = new BoardCell(4,5,BoardCell.Color.GREEN);
        assertFalse(MoveValidator.canJump(pB,b1,b3,b2));

        BoardCell b4 = new BoardCell(2,3,BoardCell.Color.GREEN);
        b4.setPiece(pB);
        BoardCell b5 = new BoardCell(3,4,BoardCell.Color.GREEN);
        b5.setPiece(pR);
        BoardCell b6 = new BoardCell(4,5,BoardCell.Color.GREEN);
        assertTrue(MoveValidator.canJump(pB,b4,b6,b5));
    }

}