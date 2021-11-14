package pl.edu.agh.kis.pz1.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class representing Deck
 */
public class Deck {

    private final ArrayList<Card> Deck;

    /**
     *Constructor creating ArrayList Object with sorted cards
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
     * Util method for comparing two cards
     */
    public Comparator<Card> comparator = new Comparator<Card>() {
        @Override
        public int compare(Card o1, Card o2) {
            return o1.getRank().compareTo(o2.getRank());
        }
    };

    /**
     * Sorted Deck getter
     * @return Sorted Deck
     */
    public ArrayList<Card> getSortedDeck(){

        Deck.sort(comparator);
        return Deck;
    }

    /**
     * method for shuffling Cards in Deck
     */
    public void shuffle(){
        Collections.shuffle(Deck);

    }

    public void add(Card card){

        this.Deck.add(card);
    }


}
