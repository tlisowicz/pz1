package pl.edu.agh.kis.pz1.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Player {

    private final ArrayList<Card> Cards = new ArrayList<>();
    private int cash = 0;


    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public ArrayList<Card> getCards() {
        return Cards;
    }

    public void pickCards(Deck deck){

        for (int i = 0; i < 5; ++i) {
            Cards.add( deck.getDeck().remove(0));

        }
    }
    public void show_cards(){
        System.out.println("Your Cards: \n");
        for (Card c: Cards){
            System.out.println((Cards.indexOf(c)+ 1) + ". " + c.getSuit() + " " + c.getRank() + " ");
        }
        System.out.println();
    }

    public void pass(){

    }

    /* TODO:
        Zajac sie wyjatkiem wyrzucanym
    */
    public void exchange(String indexes, Deck deck) throws IndexOutOfBoundsException{

        ArrayList<Card> Cards = getCards();
        Scanner  scanner= new Scanner(indexes).useDelimiter("[,\\s+]");
        ArrayList<Integer> positions = new ArrayList<>();
        while (scanner.hasNext()){
            if (scanner.hasNextInt()){
                int pos = scanner.nextInt()-1;
                if (pos > 4 || pos < 0 || positions.contains(pos)){

                    throw new IndexOutOfBoundsException("Given invalid string of indexes.");
                }
                positions.add(pos);
            }
            else{
                scanner.next();
            }
        }

        for (int i : positions){
            deck.add(Cards.get(i));
            Cards.remove(i);
            Cards.add(i, deck.getDeck().remove(0));
        }
    }
}
