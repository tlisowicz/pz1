package cards;

import java.util.Objects;

/**
 * Klasa reprezentujaca karte
 */
public class Card {
    /**
     * @see cards.Rank
     */
    private final Rank rank;

    /**
     * @see cards.Suit
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

    /**
     * Pomocnicza funkcja do porownywania kart
     * @param c karta wzgledem, ktorej dokonywane jest porownanie
     * @return 1 jezeli Karta utworzona ma wieksza wartosc, 0 jezeli karta(argument) ma wieksza wartosc
     */
    public int compare(Card c){
        return Integer.compare(this.rank.ordinal(),c.rank.ordinal());
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
