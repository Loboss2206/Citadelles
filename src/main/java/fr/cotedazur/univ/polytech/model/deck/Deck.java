package fr.cotedazur.univ.polytech.model.deck;

import java.util.ArrayList;
import java.util.List;

/**
 * The Deck class represents a generic deck of cards.
 * It provides methods for adding, removing, and shuffling cards in the deck.
 *
 * @param <T> the type of cards in the deck
 */
public class Deck<T extends Enum<T>> {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Deck.class.getName());

    private final List<T> cards;

    /**
     * Constructs a deck with the given list of cards.
     *
     * @param cards the list of cards to initialize the deck with
     */
    public Deck(List<T> cards) {
        this.cards = cards;
    }

    /**
     * Constructs an empty deck.
     */
    public Deck() {
        this.cards = new ArrayList<T>();
    }

    /**
     * Returns the list of cards in the deck.
     *
     * @return the list of cards in the deck
     */
    public List<T> getCards() {
        return cards;
    }

    /**
     * Draws a card from the top of the deck.
     *
     * @return the card drawn from the deck
     */
    public T draw() {
        return cards.remove(0);
    }

    /**
     * Draws a card from the deck at the specified index.
     *
     * @param index the index of the card to draw
     * @return the card drawn from the deck
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public T draw(int index) {
        if (index < 0 || index >= cards.size()) {
            return null;
        }
        return cards.remove(index);
    }

    /**
     * Shuffles the cards in the deck using the Fisher-Yates algorithm.
     */
    public void shuffle() {
        for (int i = getCards().size() - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            T temp = getCards().get(i);
            getCards().set(i, getCards().get(j));
            getCards().set(j, temp);
        }
        LOGGER.info("Le deck a été mélangé");
    }

    /**
     * Adds a card to the bottom of the deck.
     *
     * @param card the card to add to the deck
     */
    public void add(T card) {
        cards.add(card);
    }

    /**
     * Adds a card at a random position in the deck.
     *
     * @param card the card to add to the deck
     */
    public void addRandom(T card) {
        cards.add((int) (Math.random() * cards.size()), card);
    }

    /**
     * Removes all cards from the deck.
     */
    public void clear() {
        cards.clear();
    }

    /**
     * Returns the number of cards in the deck.
     *
     * @return the number of cards in the deck
     */
    public int size() {
        return cards.size();
    }

    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Checks if the deck contains the specified card.
     *
     * @param card the card to check for
     * @return true if the deck contains the card, false otherwise
     */
    public boolean contains(T card) {
        return cards.contains(card);
    }
}
