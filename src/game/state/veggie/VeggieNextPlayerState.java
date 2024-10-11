package game.state.veggie;

import exceptions.NotImplementedException;
import game.state.common.StateContext;
import game.state.common.GameState;
import player.Participant;
import player.Player;

public class VeggieNextPlayerState extends GameState {
    private final Participant currentParticipant;
    private Participant nextParticipant;
    public VeggieNextPlayerState(StateContext stateContext, Participant currentParticipant) {
        super(stateContext);
        this.currentParticipant = currentParticipant;
    }

    @Override
    public void executeState() {
        if(currentParticipant == null) {
            nextParticipant = participants.get((int) (Math.random() * participants.size()));
        }
        nextParticipant = participants.get((participants.indexOf(currentParticipant) + 1) % participants.size());
    }

    @Override
    public void executeNextStage() {
        if(!market.isAllPilesEmpty()){
            if(nextParticipant instanceof Player) {
                stateContext.setNextState(new VeggiePlayerTurnState( stateContext, (Player) nextParticipant));
            } else {
                stateContext.setNextState(new VeggieBotTurnState(stateContext, nextParticipant));
            }
        }else{
            throw new NotImplementedException("End game state not implemented");
        }
        stateContext.executeNextState();

    }
}
