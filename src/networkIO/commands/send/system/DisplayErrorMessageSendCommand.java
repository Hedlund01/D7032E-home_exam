package networkIO.commands.send.system;

import networkIO.commands.SendCommand;

/**
 * Command to display an error message.
 * This command sends an error message to the client.
 */
public class DisplayErrorMessageSendCommand extends SendCommand {

    private final String message;

    /**
     * Constructor to initialize the command with an error message.
     *
     * @param message The error message to be sent to the client.
     */
    public DisplayErrorMessageSendCommand(String message) {
        this.message = message;
    }

    /**
     * Gets the error message to be sent to the client.
     *
     * @return The error message.
     */
    public String getMessage() {
        return message;
    }
}