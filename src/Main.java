import networkIO.Client;

public class Main {
    public static void main(String[] args) throws Exception {
        //Check if arg[0] is valid ip address and port
        //127.0.0.1:123
        if (args[0].matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}:[0-9]{1,5}\\b")) {
            String[] ipAndPort = args[0].split(":");
            new Client(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
        } else if (args[0].matches("[0-9]{1,5}")) {
            new PointSalad(args);
        } else {
            System.out.println("Invalid ip address and port or port");
        }
    }
}