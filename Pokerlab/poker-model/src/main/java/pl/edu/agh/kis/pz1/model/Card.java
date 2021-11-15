package pl.edu.agh.kis.pz1.model;

import java.util.Objects;

/**
 * Class representing card
 */
public class Card {
    /**
     * @see Rank
     */
    private final Rank rank;

    /**
     * @see Suit
     */
    private final Suit suit;

    public Card(Rank r, Suit s){
        this.rank = r;
        this.suit = s;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}
