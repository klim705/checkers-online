package checkers;

public class Piece {
    public enum Color {RED, BLACK}

    private Color color;
    private boolean isKing;

    public Piece() {}

    public Piece(Color color) {
        this.color = color;
        isKing = false;
    }

    public Color getColor() {
        return color;
    }

    public boolean isKing() {
        return isKing;
    }

    public void setKing() {
        isKing = true;
    }
}
