package game.state.common;

import static org.mockito.Mockito.*;

import game.score.IScorer;
import networkIO.commands.send.game.DisplayParticipantHandAndScoreSendCommand;
import networkIO.commands.send.system.TerminateSendCommand;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import player.Participant;
import player.Player;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class EndStateTest {
    private EndState endState;
    @Mock
    private StateContext stateContext;
    @Mock
    private IScorer scorer;
    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
    }

    @Test
    void executeState_calculatesScoresAndLogsGameOver(@Mock Participant participant1, @Mock Participant participant2) {
        ArrayList<Participant> participants = new ArrayList<>();
        participants.add(participant1);
        participants.add(participant2);
        when(stateContext.getParticipants()).thenReturn(participants);
        when(participant1.getPlayerID()).thenReturn(1);
        when(participant2.getPlayerID()).thenReturn(2);

        when(scorer.calculateScore(any(), eq(participant1), any())).thenReturn(10);
        when(scorer.calculateScore(any(), eq(participant2), any())).thenReturn(20);

        try (var _ = mockStatic(LogManager.class)) {
            when(LogManager.getLogger()).thenReturn(logger);
            try (var ignored = mockStatic(CloseableThreadContext.Instance.class)) {
                endState = new EndState(stateContext, scorer);
                endState.executeState();
            }
        }

        verify(logger).info("GAME OVER");
        verify(logger).info(
                "Player {}'s score is: {}",
                1,
                10
        );;
        verify(logger).info(
                "Player {}'s score is: {}",
                2,
                20
        );;

    }

    @Test
    void executeState_sendsCommandsToPlayers(@Mock Player player1, @Mock Player player2) {
        ArrayList<Participant> participants = new ArrayList<>();
        participants.add(player1);
        participants.add(player2);
        when(stateContext.getParticipants()).thenReturn(participants);
        when(player1.getPlayerID()).thenReturn(1);
        when(player2.getPlayerID()).thenReturn(2);
        when(scorer.calculateScore(any(), eq(player1), any())).thenReturn(10);
        when(scorer.calculateScore(any(), eq(player2), any())).thenReturn(20);

            try (var ignored = mockStatic(CloseableThreadContext.Instance.class)) {
                endState = new EndState(stateContext, scorer);
                endState.executeState();
            }

        verify(player1, times(2)).sendCommand(any(DisplayParticipantHandAndScoreSendCommand.class));
        verify(player2, times(2)).sendCommand(any(DisplayParticipantHandAndScoreSendCommand.class));
    }

    @Test
    void executeState_sendsTerminateCommandToAllPlayers(@Mock Player player1, @Mock Player player2) {
        ArrayList<Participant> participants = new ArrayList<>();
        participants.add(player1);
        participants.add(player2);
        when(stateContext.getParticipants()).thenReturn(participants);
        when(player1.getPlayerID()).thenReturn(1);
        when(player2.getPlayerID()).thenReturn(2);

        try (var _ = mockStatic(LogManager.class)) {
            when(LogManager.getLogger()).thenReturn(logger);
            try (var ignored = mockStatic(CloseableThreadContext.Instance.class)) {
                endState = new EndState(stateContext, scorer);
                endState.executeState();
            }
        }

        verify(stateContext).sendCommandToAllPlayers(any(TerminateSendCommand.class));
    }

}