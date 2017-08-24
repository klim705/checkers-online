package checkers;

import java.util.ArrayList;

public class CheckersMove {
    public ArrayList<BoardCell> move;
    private boolean isValidMove;
    private boolean isValidJump;

    public CheckersMove() {
        move = new ArrayList<>();
        isValidMove = false;
        isValidJump = false;
    }

    public boolean isValid() {
        return isValidMove || isValidJump;
    }

    public boolean isValidMove() {
        return isValidMove;
    }

    public boolean isValidJump() {
        return isValidJump;
    }

    public boolean add(BoardCell cell) {
        if (move.size() == 1) {
            isValidMove = MoveValidator.isMove(move.get(move.size() - 1), cell);
        }
        if (move.size() >= 1) {
            isValidJump = MoveValidator.isJump(move.get(move.size() - 1), cell);
        }
        return move.add(cell);
    }

    public BoardCell get(int index) {
        return move.get(index);
    }

    public boolean removeCell(BoardCell cell) {
        boolean removed = move.remove(cell);
        if (move.size() <= 1) {
            isValidJump = false;
            isValidMove = false;
        }
        return removed;
    }

    public int size() {
        return move.size();
    }

    public boolean isEmpty() {
        return move.isEmpty();
    }

    public BoardCell first() {
        return move.get(0);
    }

    public BoardCell last() {
        return move.get(move.size() - 1);
    }

    public void clear() {
        for (BoardCell cell : move) {
            cell.setSelected(false);
        }
        move.clear();
        isValidMove = false;
        isValidJump = false;
    }
}
