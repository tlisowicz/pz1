package cards;

import java.util.*;

/**
 * Klasa reprezentujaca talie kart
 */
public class Deck {

    public ArrayList<Card> getDeck() {
        return Deck;
    }

    private final ArrayList<Card> Deck;

    /**
     * Konstruktor tworzacy obiekt typu ArrayList posortowanych kart
     */
    public Deck(){
        this.Deck = new ArrayList<>();
        for (Suit s: Suit.values()){

            for (Rank r : Rank.values())  {
                Deck.add(new Card(r,s));
            }
        }
    }

    /**
     * @return Posortowana talie
     */
    public ArrayList<Card> getSortedDeck(){
        return Deck;
    }

    /**
     * funkcja sortujaca przyjeta liste kart
     * @param Cards dowolna lista kart
     * @return posortowana lsita kart
     */
    public ArrayList<Card> shuffle(List<Card> Cards){
        Cards.sort(Card::compare);
        ArrayList<Card> sorted = (ArrayList<Card>) Cards;
        return sorted;

    }


}
