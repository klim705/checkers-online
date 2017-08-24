package checkers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
    // This registers objects that are going to be sent over the network.
    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(checkers.Board.class);
        kryo.register(checkers.BoardCell.class);
        kryo.register(checkers.BoardCell.Color.class);
        kryo.register(checkers.BoardCell[].class);
        kryo.register(checkers.BoardCell[][].class);
        kryo.register(checkers.CheckersMove.class);
        kryo.register(checkers.MoveValidator.class);
        kryo.register(checkers.Piece.class);
        kryo.register(checkers.Piece.Color.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(NetBoard.class);
        kryo.register(Player.class);
        kryo.register(NewConnect.class);
        kryo.register(EndGame.class);
        kryo.register(RestartGame.class);
    }

    // Board to be sent over the network and relayed to clients
    static public class NetBoard {
        public Board board;
    }

    static public class Player {
        public Piece.Color playerColor;
    }

    static public class NewConnect {
        public String newClient;
    }

    static public class EndGame {
        public String reason;  // why the game ended
        /*
        disconnect = the other client disconnected
        forfeit = the other player forfeited
        loss = the receiving client lost the game
        win =  the receiving client won the game
        full = game is full
         */
    }

    static public class RestartGame {
        public boolean restart;
        /*
        true = client wants to restart
        false = client does not want to restart
         */
    }
}