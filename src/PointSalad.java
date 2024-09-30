import Card.Vegetable;
import IONetwork.Server;
import Market.IMarket;
import Market.VeggieMarket;
import Player.Player;
import Player.Participant;
import java.util.ArrayList;

public class PointSalad {
    private ArrayList<Participant<Vegetable>> players;

    private Server<Vegetable> server;

    private IMarket market;

    private Participant<Vegetable> currentPlayer;

    public PointSalad(String[] args) {
        try{
            server = new Server<Vegetable>(Integer.parseInt(args[0]), Vegetable.class);
            // Start accepting connections where args[1] is the number of players and args[2] is the number of bots
            players = server.startAcceptingConnections(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            market = new VeggieMarket();
        } catch (IndexOutOfBoundsException e){
            System.out.println("Invalid number of arguments; please provide a port, number of players, and number of bots");
            return;
        }


        _gameLoop();
    }

    private void _getAndSetNewCurrentPlayer(){
        if(currentPlayer == null){
            currentPlayer = players.get((int) (Math.random() * (players.size())));
        }else{
            int index = players.indexOf(currentPlayer);
            if(index == players.size() - 1){
                currentPlayer = players.getFirst();
            } else {
                currentPlayer = players.get(index + 1);
            }
        }

    }

    private void _gameLoop(){
        // Set random starting player
        while(true){

            if(market.pilesIsEmpty()){
                // End game
                //[TODO] Implement end game logic in a seperate method
                //[TODO] Implement scoring logic
                break;
            }

            _getAndSetNewCurrentPlayer();


            if(currentPlayer.isBot()){
                // Bot logic
                //[TODO] Implement bot logic,
                // if seperated from player a bot could just have a method that does all the necesariy logic,
                // but keep in mind the modularity for the other game
            } else {
                // Player logic
                //[TODO] Implement player logic
            }
        }

    }
}
