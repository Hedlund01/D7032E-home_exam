package game.state.veggie;

import game.state.common.IGameState;
import game.state.common.StateContext;
import game.state.common.GameState;
import player.Participant;

public class VeggieInitState implements IGameState {

    private final StateContext stateContext;
    public VeggieInitState(StateContext stateContext) {
        this.stateContext = stateContext;
    }

    @Override
    public void executeState() {
        stateContext.getMarket().initializeMarket(stateContext.getParticipants().size(), "resources/PointSaladManifest.json");


    }

    @Override
    public GameState getNextState() {
        Participant initParticipant = stateContext.getParticipants().get((int) (Math.random() * stateContext.getParticipants().size()));
        return new VeggieNextPlayerState(stateContext, initParticipant);
    }
}
