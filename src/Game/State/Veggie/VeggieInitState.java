package Game.State.Veggie;

import Game.State.Common.StateContext;
import Game.State.Common.GameState;
import Player.Participant;

public class VeggieInitState extends GameState {

    public VeggieInitState(StateContext stateContext) {
        super(stateContext);
    }

    @Override
    public void executeState() {
        market.setPiles(participants.size(), "resources/PointSaladManifest.json");
    }

    @Override
    public void executeNextStage() {
        Participant initParticipant = participants.get((int) (Math.random() * participants.size()));
        stateContext.setNextState(new VeggieNextPlayerState(stateContext, initParticipant));
        stateContext.executeNextState();
    }
}
