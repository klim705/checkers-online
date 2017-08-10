package checkers;

public class OnlineCheckersServer {
    private Board board;
    private OnlineCheckersClient client1;
    private OnlineCheckersClient client2;
    private int port;

    public OnlineCheckersServer() {
        board = new Board();
    }

    public Board sendBoard() {
        return board;
    }

    // TODO: check win conditions
}
