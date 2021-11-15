package pl.edu.agh.kis.pz1.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class PlayerTest {

    Player p = new Player();

@Test

    public void testexchange(){
    Deck deck = new Deck();
    p.pickCards(deck);
    ArrayList<Card> d = deck.getDeck();
    ArrayList<Card> Cards = new ArrayList<>(p.getCards());
    p.exchange("1,2,3", deck);
    ArrayList<Card> Cards2 = new ArrayList<>(p.getCards());
    System.out.println("Cards should be exchanged");
    Assert.assertNotEquals(Cards, Cards2);
    for (int i = 0; i < 3; ++i){
        System.out.printf("Should contain %d", i);
        Assert.assertTrue(deck.getDeck().contains(Cards.get(i)));
    }

    }

}
