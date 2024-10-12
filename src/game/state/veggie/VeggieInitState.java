package game.state.veggie;

import game.state.common.StateContext;
import game.state.common.GameState;
import player.Participant;

public class VeggieInitState extends GameState {

    public VeggieInitState(StateContext stateContext) {
        super(stateContext);
    }

    @Override
    public void executeState() {
        market.setPiles(participants.size(), "resources/PointSaladManifest.json");
    }

    @Override
    public void executeNextState() {
        Participant initParticipant = participants.get((int) (Math.random() * participants.size()));
        stateContext.setNextState(new VeggieNextPlayerState(stateContext, initParticipant));
        stateContext.executeNextState();
    }
}
