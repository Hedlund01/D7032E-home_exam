package networkIO;

import networkIO.commands.CommandTypeEnum;
import networkIO.commands.recive.SendClientInputToServerCommand;
import networkIO.commands.send.AskClientToSendInputCommand;
import networkIO.commands.send.DisplayMarketCommand;
import networkIO.commands.send.DisplayStringCommand;
import networkIO.commands.ICommand;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private final Socket socket;
    private final ObjectOutputStream outToServer;
    private final ObjectInputStream inFromServer;

    public Client(String ipAddress, int port) throws Exception {
        this.socket = new Socket(ipAddress, port);
        this.outToServer = new ObjectOutputStream(socket.getOutputStream());
        this.inFromServer = new ObjectInputStream(socket.getInputStream());
        start();
    }

    private void start() throws Exception {
        while (true){
            var message = inFromServer.readObject();
            ICommand command;
            try{
                // Check if message is a command that is sent from writeObject
                command = (ICommand) message;

            } catch (Exception e){
                // If not a command, then it is a string
                System.out.println(message);
                continue;
            }


            switch (command.getCommand()){
                case CommandTypeEnum.DISPLAY_MARKET -> {
                    var dto = (DisplayMarketCommand) command;
                    System.out.println(dto.faceCards.getFirst().getFace());
                }
                case CommandTypeEnum.DISPLAY_STRING -> {
                    var dto = (DisplayStringCommand) command;
                    System.out.println(dto.getMessage());
                }

                case CommandTypeEnum.ASk_CLIENT_TO_SEND_INPUT -> {
                    var cmd = (AskClientToSendInputCommand) command;
                    System.out.println(cmd.getMessage());
                    var input = System.console().readLine();
                    sendCommand(new SendClientInputToServerCommand(input));
                }

                case CommandTypeEnum.TERMINATE -> {
                    close();
                    System.exit(0);
                }
            }

        }
    }

    public void close() throws Exception {
        inFromServer.close();
        outToServer.close();
        socket.close();
    }

    private void sendCommand(ICommand command) {
        try {
            outToServer.writeObject(command);
        } catch (Exception e) {
            System.out.println("Error sending command to server. Error: " + e.getMessage());
        }
    }

}
