package checkers;

public class OnlineCheckersClient {
    public static Piece.Color playerColor;
    public static Board board;
    public static BoardGUI boardGUI;

    public static void main(String args[]) {
        //board = OnlineCheckersServer.getBoard();
        board = new Board();
        boardGUI = new BoardGUI();
        boardGUI.setup(board);
    }
}
