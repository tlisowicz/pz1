package pl.edu.agh.pz;

import java.util.*;

/**
 * Klasa reprezentujaca talie kart
 */
public class Deck {

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

    public ArrayList<Card> getDeck() {
        return Deck;
    }


    /**
     * komparator do porównywania wartości kart w talii
     */
    public Comparator<Card> comparator = new Comparator<Card>() {
        @Override
        public int compare(Card o1, Card o2) {
            return o1.getRank().compareTo(o2.getRank());
        }
    };

    /**
     * @return Posortowana talie
     */
    public ArrayList<Card> getSortedDeck(){

        Deck.sort(comparator);
        return Deck;
    }

    /**
     * funkcja sortujaca przyjeta liste kart
     * @return posortowana lista kart
     */
    public void shuffle(){
        Collections.shuffle(Deck);

    }


}
