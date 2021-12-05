package pl.edu.agh.kis.pz1.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class PlayerTest {

    private Player p;

    @Before
    public void setUp() {
        p = new Player();
}

    @Test
    public void ShouldExchangeCards(){

        Deck deck = new Deck();
        for (int i = 0; i< 5; ++i){
            p.getCard(deck.getCards().remove(0));
        }
        ArrayList<Card> d = deck.getCards();
        ArrayList<Card> Cards = new ArrayList<>(p.getCards());
        ArrayList<Integer> positions = new ArrayList<>();
        positions.add(0);
        positions.add(1);
        positions.add(2);
        p.exchange(positions, deck);
        ArrayList<Card> Cards2 = new ArrayList<>(p.getCards());
        Assert.assertNotEquals("Cards should be exchanged",Cards, Cards2);
        for (int i = 0; i < 3; ++i){
            System.out.println("Deck should contain " + Cards.get(i).getRank() + " " + Cards.get(i).getSuit());
            Assert.assertTrue(deck.getCards().contains(Cards.get(i)));
        }

    }
}
