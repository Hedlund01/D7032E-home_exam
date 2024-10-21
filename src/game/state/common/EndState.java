package game.state.common;

import game.score.IScorer;
import networkIO.commands.send.game.DisplayParticipantHandAndScoreSendCommand;
import networkIO.commands.send.system.TerminateSendCommand;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import player.Participant;
import player.Player;

import java.util.Collections;
import java.util.HashMap;

public class EndState extends GameState {
    private final Logger logger = LogManager.getLogger();
    HashMap<Integer, Integer> scores = new HashMap<>();
    IScorer scorer;

    public EndState(StateContext stateContext, IScorer scorer) {
        super(stateContext);
        this.scorer = scorer;
    }

    @Override
    public void executeState() {
        for (Participant participant : stateContext.getParticipants()) {
            scores.put(participant.getPlayerID(), scorer.calculateScore(participants, participant, participant.getHand()));
        }

        Integer winnerID = Collections.max(scores.entrySet(), HashMap.Entry.comparingByValue()).getKey();

        try (final CloseableThreadContext.Instance _ = CloseableThreadContext.put("winnerID", winnerID.toString())
                .put("winnerScore", scores.get(winnerID).toString())) {
            logger.info("GAME OVER");
            for (Participant participant : stateContext.getParticipants()) {
                logger.info("Player {}'s score is: {}", participant.getPlayerID(), scores.get(participant.getPlayerID()));
                if (participant instanceof Player p) {
                    if (participant.getPlayerID() == winnerID) {
                        logger.info("Player {} is the winner with a score of {}", winnerID, scores.get(winnerID));
                        p.sendCommand(new DisplayParticipantHandAndScoreSendCommand(p.getName(), scores.get(winnerID), p.getHand(), true));
                    } else {
                        p.sendCommand(new DisplayParticipantHandAndScoreSendCommand(p.getName(), scores.get(participant.getPlayerID()), p.getHand(), false));
                    }
                }
            }
            stateContext.sendCommandToAllPlayers(new TerminateSendCommand());

        }

    }

    @Override
    public IGameState getNextState() {
        return null;
    }


}
