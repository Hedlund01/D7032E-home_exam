package player;

public class Bot extends Participant {

    public Bot(int playerID) {
        super(playerID);
    }

    @Override
    public boolean isBot() {
        return true;
    }
}
