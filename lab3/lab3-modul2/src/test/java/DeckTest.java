import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import pl.edu.agh.pz.*;


import java.util.ArrayList;

public class DeckTest {

    Deck deck = new Deck();

   @Test
   @DisplayName("Deck creation test")
   public void testDeck(){
       ArrayList<Card> testDeck = new ArrayList<>();
        for (Suit suit : Suit.values()){
            for (Rank rank : Rank.values()){
                testDeck.add(new Card(rank,suit));
            }
        }
   Assertions.assertEquals(testDeck, deck.getDeck());
   }

   @Test
    @DisplayName("shuffle method test")
   public void testshuffle(){

       ArrayList<Card> testDeck = new ArrayList<>();
       for (Suit suit : Suit.values()){
           for (Rank rank : Rank.values()){
               testDeck.add(new Card(rank,suit));
           }
       }
       deck.shuffle();
       Assertions.assertNotEquals(testDeck, deck.getDeck());
   }
}
