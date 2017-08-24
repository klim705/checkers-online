package checkers;

public class BoardCell {
    public enum Color {WHITE, GREEN}

    public int row;
    public int column;
    public Color color;
    public Piece piece;
    private boolean selected;

    public BoardCell() {}

    public BoardCell(int row, int column, Color color) {
        this.row = row;
        this.column = column;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public boolean hasPiece() {
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void removePiece() {
        this.piece = null;
    }

    public void toggleSelected() {
        selected = !selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean equals(BoardCell cell) {
        return this.row == cell.row && this.column == cell.column;
    }
}
