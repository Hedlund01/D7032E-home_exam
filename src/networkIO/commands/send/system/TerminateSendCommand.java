package networkIO.commands.send.system;

import networkIO.commands.SendCommand;

/**
 * Command to terminate the connection.
 * This command is used to notify the client that the connection is being terminated.
 * The client should close the connection after receiving this command.
 */
public class TerminateSendCommand extends SendCommand {
}
