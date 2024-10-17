package game.state.common;

import market.IMarket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import player.Participant;
import player.Player;

import java.util.ArrayList;

public class StateContext {

    private final Logger logger = LogManager.getLogger();
    private final IMarket market;
    private final ArrayList<Participant> participants;
    private IGameState currentState;

    public StateContext(ArrayList<Participant> participants, IMarket market) {
        this.participants = participants;
        this.market = market;
    }

    public void setState(IGameState state) {
        currentState = state;
    }


    public void execute(){
        while (currentState != null) {
            currentState.executeState();
            currentState = currentState.getNextState();
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
