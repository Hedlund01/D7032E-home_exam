package Game.State.Common;

import Player.Participant;
import Market.IMarket;
import java.util.ArrayList;

public abstract class GameState {
    protected IMarket market;
    protected ArrayList<Participant> participants;
    protected StateContext stateContext;
    public GameState( StateContext stateContext) {
        this.stateContext = stateContext;
        this.market = stateContext.getMarket();
        this.participants = stateContext.getParticipants();
    }

    public abstract void executeState();

    public abstract void executeNextStage();

}
