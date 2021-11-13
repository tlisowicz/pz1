package pl.edu.agh.kis.pz1.model;

import java.util.ArrayList;
import java.util.Collections;

public class Player {

    private ArrayList<Card> Cards = new ArrayList<>();

    public ArrayList<Card> getCards() {
        return Cards;
    }

    public void setCards(ArrayList<Card> cards) {
        Cards = cards;
    }

    public void getCards(Deck d){

        for (int i = 0; i < 5; ++i) {
            Collections.shuffle(d.getDeck());
            Cards.add( d.getDeck().remove(0));

        }
    }
    public void show_cards(){
        for (Card c: Cards){
            System.out.println(c.getSuit() + " " + c.getRank() + " ");
        }
        System.out.println();
    }
}
