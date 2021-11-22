package pl.edu.agh.kis.pz1.model;

import pokerExceptions.MultipleIdenticalCardsInDeckException;

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

    /* TODO:
        Sprawdzic czy nie dodaje karty, ktora juz jest w talii
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
