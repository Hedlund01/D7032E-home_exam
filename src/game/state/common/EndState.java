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
import java.util.Objects;

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
                    for (var score : scores.entrySet()) {
                        if (score.getKey() != winnerID) {
                            var hand = participants.get(score.getKey()-1).getHand();
                            p.sendCommand(
                                    new DisplayParticipantHandAndScoreSendCommand(
                                            score.getKey(),
                                            participants.get(score.getKey()-1).getName(),
                                            score.getValue(),
                                            hand,
                                            false
                                    )
                            );
                        }
                    }
                }
            }
            for (Participant participant : stateContext.getParticipants()) {
                if (participant instanceof Player p) {
                    var hand = participants.get(winnerID-1).getHand();
                    p.sendCommand(
                            new DisplayParticipantHandAndScoreSendCommand(
                                    winnerID,
                                    participants.get(winnerID-1).getName(),
                                    scores.get(winnerID),
                                    hand,
                                    true
                            )
                    );
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
