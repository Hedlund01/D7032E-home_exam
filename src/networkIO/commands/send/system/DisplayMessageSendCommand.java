package networkIO.commands.send.system;

import networkIO.commands.SendCommand;

/**
 * Command to display an invalid input message.
 * This command is used to notify the client about invalid input.
 */
public class DisplayMessageSendCommand extends SendCommand {

    private final String message;

    /**
     * Constructor to initialize the command with a message.
     *
     * @param message The message to be sent to the client.
     */
    public DisplayMessageSendCommand(String message) {
        this.message = message;
    }

    /**
     * Gets the message to be sent to the client.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

}
