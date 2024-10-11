package game.state.common;

import exceptions.NotImplementedException;
import game.state.veggie.VeggieInitState;
import market.IMarket;
import market.VeggieMarket;
import player.Participant;

import java.util.ArrayList;

public class StateContext {
    protected IMarket market = new VeggieMarket();
    protected ArrayList<Participant> participants;
    private GameState nextState;
    public StateContext(ArrayList<Participant> participants) {
        this.participants = participants;
        setNextState(new VeggieInitState( this));
    }

    public void setNextState(GameState currentState) {
        this.nextState = currentState;
    }

    public void executeNextState() {
        nextState.executeState();
        if(nextState != null){
            nextState.executeNextStage();
        }else{
            //[TODO] Implement game over
            throw new NotImplementedException("Game Over");
        }
    }

    public IMarket getMarket() {
        return market;
    }
    public ArrayList<Participant> getParticipants() {
        return participants;
    }
}
