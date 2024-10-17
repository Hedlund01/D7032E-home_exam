package game.state.veggie;

import exceptions.NotImplementedException;
import game.score.VeggieScorer;
import game.state.common.EndState;
import game.state.common.StateContext;
import game.state.common.GameState;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import player.Participant;
import player.Player;


public class VeggieNextPlayerState extends GameState {
    private static final Logger logger = LogManager.getLogger();
    private final Participant currentParticipant;
    private Participant nextParticipant;
    public VeggieNextPlayerState(StateContext stateContext, Participant currentParticipant) {
        super(stateContext);
        this.currentParticipant = currentParticipant;
    }

    @Override
    public void executeState() {
        nextParticipant = participants.get((participants.indexOf(currentParticipant) + 1) % participants.size());
    }

    @Override
    public GameState getNextState() {

        if(!market.isAllPilesEmpty()){
            if(nextParticipant instanceof Player) {
                return new VeggiePlayerTurnState( stateContext, (Player) nextParticipant);
            } else {
                return new VeggieBotTurnState(stateContext, nextParticipant);
            }
        }else{
            return new EndState(stateContext, new VeggieScorer());
        }
    }
}
