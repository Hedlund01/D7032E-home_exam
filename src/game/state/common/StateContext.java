package game.state.common;

import exceptions.NotImplementedException;
import game.state.veggie.VeggieInitState;
import market.IMarket;
import market.VeggieMarket;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import player.Participant;
import player.Player;

import java.util.ArrayList;

public class StateContext {

    private final Logger logger = LogManager.getLogger();
    private IMarket market = new VeggieMarket();
    private ArrayList<Participant> participants;
    private GameState nextState;

    public StateContext(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    public void setNextState(GameState currentState) {
        this.nextState = currentState;
    }

    public void executeNextState() {

            if (nextState != null) {
                logger.trace("Executing state {}", nextState.getClass().getSimpleName());
                nextState.executeState();
                nextState.executeNextState();
            } else {
                sendToAllPlayers("\n-------------------------------------- GAME OVER --------------------------------------\n");
                logger.info("Game Over, exiting program");
                System.exit(0);
            }
    }

    public IMarket getMarket() {
        return market;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void sendToAllPlayers(String message) {
        for (Participant player : participants) {
            if (player instanceof Player) {
                ((Player) player).sendMessage(message);
            }
        }
    }
}
