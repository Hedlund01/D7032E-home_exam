package market;

import card.ICard;
import exceptions.InvalidArgumentException;
import exceptions.PileOutOfCardsException;

public interface IPile {
    ICard getPointCard() throws PileOutOfCardsException;

    ICard removeFirstPointCard() throws PileOutOfCardsException;

    ICard removeLastPointCard() throws PileOutOfCardsException;

    ICard getFaceCard(int index);

    ICard removeFaceCard(int index) throws PileOutOfCardsException;

    ICard forceRemoveFaceCard(int index);

    void addPointCard(ICard card);

    void addFaceCard(ICard card, int index) throws InvalidArgumentException;

    boolean isEmpty();

    int getPointCardCount();

    int getFaceCardCount();
}
