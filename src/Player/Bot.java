package Player;

public class Bot<T extends Enum<T>> extends Participant<T> implements IIsBot, IBot {

    public Bot(int playerID, Class<T> faceClass) {
        super(playerID, faceClass);
    }

    public boolean isBot() {
        return true;
    }
}
