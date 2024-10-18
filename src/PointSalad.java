import card.Card;
import card.ICard;
import card.Vegetable;
import game.state.common.StateContext;
import game.state.veggie.VeggieInitState;
import market.VeggieMarket;
import networkIO.Server;
import networkIO.commands.DisplayMarketCommandDTO;
import player.Participant;
import player.Player;

import java.util.ArrayList;

public class PointSalad {
    private ArrayList<Participant> players;

    private Server server;


    public PointSalad(String[] args) {
        try{
            server = new Server(Integer.parseInt(args[0]));
            // Start accepting connections where args[1] is the number of players and args[2] is the number of bots
            players = server.startAcceptingConnections(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            StateContext game = new StateContext(players, new VeggieMarket());
            game.setState(new VeggieInitState(game));
            game.execute();

        } catch (IndexOutOfBoundsException e){
            System.out.println("Invalid number of arguments; please provide a port, number of players, and number of bots");
        }
    }

}
