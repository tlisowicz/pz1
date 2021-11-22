package pl.edu.agh.kis.pz1.model;

import java.util.ArrayList;
import java.util.Scanner;

public class Player {

    private final ArrayList<Card> cards = new ArrayList<>();
    private int cash = 0;


    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void pickCards(Deck deck){

        for (int i = 0; i < 5; ++i) {
            cards.add( deck.getCards().remove(0));

        }
    }
    public String showCards() {
        cards.sort(Deck.comparator);
        StringBuilder builder = new StringBuilder();
        builder.append("Your Cards:\n\n");
        for (Card c: cards){
            builder.append(cards.indexOf(c) + 1).append(". ").append(c.getSuit()).append(" ").append(c.getRank()).append(" \n");
        }
        return builder.toString();
    }

    public String showCash(){
        return "Your Cash: " + getCash()+ "$\n";

    }

    public void takeAnte(int ante) {
        cash-= ante;
    }

    /* TODO:
        Zajac sie wyjatkiem wyrzucanym
    */

    public void exchange(String indexes, Deck deck) {

        ArrayList<Card> Cards = getCards();
        try (Scanner scanner = new Scanner(indexes).useDelimiter("[,\\s+]")) {
            ArrayList<Integer> positions = new ArrayList<>();
            while (scanner.hasNext()) {
                if (scanner.hasNextInt()) {
                    int pos = scanner.nextInt() - 1;
                    if (pos > 4 || pos < 0 || positions.contains(pos)) {

                        throw new IndexOutOfBoundsException();
                    }
                    positions.add(pos);
                } else {
                    scanner.next();
                }
            }
            for (int i : positions) {
                deck.add(Cards.get(i));
                Cards.remove(i);
                Cards.add(i, deck.getCards().remove(0));
            }
        }
    }

    public void raise(int stake) {
        this.cash -= stake;
    }

    public void fold(Deck deck){
        for (Card card: cards) {
            deck.add(card);
        }
        cards.clear();
    }

}
