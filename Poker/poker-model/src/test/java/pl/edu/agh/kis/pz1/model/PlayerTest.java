package pl.edu.agh.kis.pz1.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class PlayerTest {

    private Player p;

    @Before
    public void setUp() throws Exception {
        p = new Player();
}

@Test
    public void ShouldExchangeCards(){

        Deck deck = new Deck();
        p.pickCards(deck);
        ArrayList<Card> d = deck.getDeck();
        ArrayList<Card> Cards = new ArrayList<>(p.getCards());
        p.exchange("1,2,3", deck);
        ArrayList<Card> Cards2 = new ArrayList<>(p.getCards());
        Assert.assertNotEquals("Cards should be exchanged",Cards, Cards2);
        for (int i = 0; i < 3; ++i){
           // Card c =;
            System.out.println("Deck should contain " + Cards.get(i).getRank() + " " + Cards.get(i).getSuit());
            Assert.assertTrue(deck.getDeck().contains(Cards.get(i)));
        }

    }

}
