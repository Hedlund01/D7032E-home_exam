package game.state.common;

import player.Participant;
import market.IMarket;
import java.util.ArrayList;

public abstract class GameState implements IGameState {
    protected StateContext stateContext;
    protected IMarket market;
    protected ArrayList<Participant> participants;
    public GameState( StateContext stateContext) {
        this.stateContext = stateContext;
        this.market = stateContext.getMarket();
        this.participants = stateContext.getParticipants();
    }

    public abstract void executeState();

    public abstract IGameState getNextState();

}
