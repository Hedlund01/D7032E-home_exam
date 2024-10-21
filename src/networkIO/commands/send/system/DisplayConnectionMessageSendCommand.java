package networkIO.commands.send.system;

import networkIO.commands.SendCommand;

/**
 * Command to display a connection message.
 * This command sends a message to the client indicating the player's connection details.
 */
public class DisplayConnectionMessageSendCommand extends SendCommand {

    // Name of the player
    private final String playerName;

    // ID of the player
    private final int playerID;

    /**
     * Constructor to initialize the command with the player's name and ID.
     *
     * @param playerName The name of the player.
     * @param playerID The ID of the player.
     */
    public DisplayConnectionMessageSendCommand(String playerName, int playerID) {
        this.playerName = playerName;
        this.playerID = playerID;
    }

    /**
     * Gets the name of the player.
     *
     * @return The name of the player.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Gets the ID of the player.
     *
     * @return The ID of the player.
     */
    public int getPlayerID() {
        return playerID;
    }
}