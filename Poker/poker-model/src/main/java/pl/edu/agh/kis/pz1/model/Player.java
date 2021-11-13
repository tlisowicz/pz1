package pl.edu.agh.kis.pz1.model;

import java.util.ArrayList;
import java.util.Collections;

public class Player {

    ArrayList<Card> Cards=new ArrayList<>();

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
        System.out.println("");
    }
}
