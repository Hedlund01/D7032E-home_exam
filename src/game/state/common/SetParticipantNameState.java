package game.state.common;

import game.state.veggie.VeggieInitState;
import networkIO.commands.recive.SendClientInputToServerCommand;
import networkIO.commands.send.system.AskClientToSendInputCommand;
import networkIO.commands.send.display.DisplayMessageCommand;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import player.Participant;
import player.Player;

public class SetParticipantNameState implements IGameState {
    private final Logger logger = LogManager.getLogger();
    private final StateContext stateContext;
    private final Participant currentParticipant;

    public SetParticipantNameState(StateContext stateContext, Participant currentParticipant) {
        this.stateContext = stateContext;
        this.currentParticipant = currentParticipant;
    }

    @Override
    public void executeState() {
        try (final var _ = CloseableThreadContext.put("playerID", Integer.toString(currentParticipant.getPlayerID()))) {
            logger.info("Setting participant name");

            if (!(currentParticipant instanceof Player player)) {
                return;
            }
            player.setReadTimeout(10 * 1000);
            player.sendCommand(new AskClientToSendInputCommand("Please enter your name (respond within 10 seconds)", "Name: "));

            var inputCmd = player.readCommand();
            if (inputCmd instanceof SendClientInputToServerCommand cmd) {
                player.setName(cmd.message());

            } else {
                player.sendCommand(new DisplayMessageCommand("You did not respond in time. Your name is set to 'Player " + player.getPlayerID() + "'"));
            }
            player.setReadTimeout(0);

            logger.info("Player name set to: {}", player.getName());
        } catch (Exception _) {
        }
    }

    @Override
    public IGameState getNextState() {
        var participants = stateContext.getParticipants();
        var nextIndex = participants.indexOf(currentParticipant) + 1;
        if (nextIndex <= participants.size() - 1) {
            return new SetParticipantNameState(stateContext, participants.get(nextIndex));
        } else {
            return new VeggieInitState(stateContext);
        }
    }
}
