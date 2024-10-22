package networkIO.commands.recive;

import networkIO.commands.SendCommand;

/**
 * Command to send the client's input to the server.
 */
public class ServerReceiveClientInputCommand extends SendCommand {

    private final String message;

    /**
     * Constructor to initialize the command with a message.
     *
     * @param message The message from the client to be sent to the server.
     */
    public ServerReceiveClientInputCommand(String message) {
        this.message = message;
    }

    /**
     * Gets the message from the client to be sent to the server.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

}
