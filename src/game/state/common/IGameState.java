package game.state.common;

public interface IGameState {
    void executeState();
    IGameState getNextState();
}
