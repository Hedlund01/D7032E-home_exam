import game.state.common.StateContext;
import networkIO.Server;
import player.Participant;
import java.util.ArrayList;

public class PointSalad {
    private ArrayList<Participant> players;

    private Server server;


    public PointSalad(String[] args) {
        try{
            server = new Server(Integer.parseInt(args[0]));
            // Start accepting connections where args[1] is the number of players and args[2] is the number of bots
            players = server.startAcceptingConnections(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            StateContext game = new StateContext(players);
            game.executeNextState();
        } catch (IndexOutOfBoundsException e){
            System.out.println("Invalid number of arguments; please provide a port, number of players, and number of bots");
        }
    }

}
