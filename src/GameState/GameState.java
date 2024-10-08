package GameState;

import Market.IMarket;
import Player.Participant;

import java.util.ArrayList;

public abstract class GameState {
    private IMarket market;
    private ArrayList<Participant> participants
    public GameState(IMarket market) {
        this.market = market;

    }

}
