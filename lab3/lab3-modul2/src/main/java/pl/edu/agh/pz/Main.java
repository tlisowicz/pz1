package pl.edu.agh.pz;

import utils.CryptUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    ////===============MAIN METHOD FOR GENERATING PLAYERS===============
    public static void Get_players(Deck d){

        Player p = new Player();
        p.getCards(d);
        p.show_cards();
    }
    public static void main(String [] args){
       Deck d = new Deck();

        //===============SORTED DECK TEST===============
        d.shuffle();
        ArrayList<Card> SortedDeck = d.getSortedDeck();

        //===============SHUFFLE METHOD TEST===============
        Rank r= null;
        Suit s= null;

        Card c1 = new Card(r.ACE,s.CLUB);
        Card c2 = new Card(r.QUEEN,s.DIAMOND);


        //===============PRINTING PLAYERS' CARDS===============
        for (int i = 0; i< 4; ++i ){
            System.out.println("PLAYER :   "+ (i+1) + "\nCARDS:" );
            Get_players(d);
        }
        //===============CARD EQUALS, HASHCODE TEST===============
        Set<Card> set = new HashSet<>();
        set.add(c1);
        set.add(c2);

        //===============PASSWORD HASHING TEST===============*/
        System.out.println(CryptUtil.encryptThisString("Losowy tekst"));

    }
}
