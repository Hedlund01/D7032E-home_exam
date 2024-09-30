package Player;

public class Bot<T extends Enum<T>> extends Participant<T> implements IIsBot, IBot {
    public Bot(int playerID) {
        super(playerID);
    }

    public boolean isBot() {
        return true;
    }
}
