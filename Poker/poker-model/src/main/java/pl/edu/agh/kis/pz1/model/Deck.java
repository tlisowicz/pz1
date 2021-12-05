package pl.edu.agh.kis.pz1.model;

import poker.Exceptions.MultipleIdenticalCardsInDeckException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representing Deck
 */
public class Deck {

    private ArrayList<Card> cards;

    /**
     *Constructor creating ArrayList Object with sorted cards
     */
    public Deck(){
        this.cards = new ArrayList<>();

        for (Suit s: Suit.values()){
            for (Rank r : Rank.values())  {
                cards.add(new Card(r,s));
            }
        }
    }

    public ArrayList<Card> getCards() {
        return cards;
    }


    /**
     * Util method for comparing two cards
     */
    public static Comparator<Card> comparator = (o1, o2) -> o1.getRank().compareTo(o2.getRank());

    /**
     * Sorted Deck getter
     * @return Sorted Deck
     */
    public ArrayList<Card> getSortedDeck(){

        cards.sort(comparator);
        return cards;
    }

    /**
     * method for shuffling Cards in Deck
     */
    public void shuffle(){
        Collections.shuffle(cards);
    }

    /**
     * method for adding card to a deck when the player has folded.
     * If A card is already in the deck Exception will be thrown
     * @see MultipleIdenticalCardsInDeckException
     * @param card Card to add
     */
    public void add(Card card){

        try{
            if (this.cards.contains(card)) {
                throw new MultipleIdenticalCardsInDeckException();
            }
            this.cards.add(card);

        } catch (MultipleIdenticalCardsInDeckException e){
            ArrayList<Card> withDups = new ArrayList<>(cards);
            cards = (ArrayList<Card>) withDups.stream().distinct().collect(Collectors.toList());
        }
    }


}
