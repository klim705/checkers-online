package checkers;

public class OnlineCheckersClient {
    public static Piece.Color playerColor;
    public static Board board;
    public static GUIManager GUIManager;

    public static void main(String args[]) {
        // TODO: connect to server, get board from client upon successful connection
        board = new Board();
        GUIManager = new GUIManager();
        GUIManager.setup(board);
    }
}
