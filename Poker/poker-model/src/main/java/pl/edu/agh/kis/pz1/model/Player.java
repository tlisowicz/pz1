package pl.edu.agh.kis.pz1.model;

import java.util.ArrayList;
/**
 * Class representing player
 */
public class Player {

    /**
     * Player's cards
     */
    private final ArrayList<Card> cards = new ArrayList<>();
    /**
     * current amount of cash
     */
    private int cash = 0;

    /**
     * determinate if player when all in
     */
    private boolean allIn = false;


    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }


    public void getCard(Card card){
        cards.add(card);
    }

    public void setAllIn(boolean allIn) {
        this.allIn = allIn;
    }

    public boolean isAllIn() {
        return allIn;
    }

    /**
     * method called to inform player about his cards
     * @return Player's cards as a String
     */
    public String showCards() {
        cards.sort(Deck.comparator);
        StringBuilder builder = new StringBuilder();
        builder.append("Your Cards:\n\n");
        for (Card c: cards){
            builder.append(cards.indexOf(c) + 1).append(". ").append(c.getSuit()).append(" ").append(c.getRank()).append(" \n");
        }
        return builder.toString();
    }

    /**
     * method called to inform player about his cash
     * @return cash as a String
     */
    public String showCash(){
        return "Your Cash: " + getCash()+ "$\n";

    }

    /**
     * method for taking ante
     * @param ante int value
     */
    public void takeAnte(int ante) {
        cash-= ante;
    }

    /**
     * method for exchanging cards
     * @param positions indexes of cards separated with space or coma. When indexes are incorrect exception is thrown
     * @param deck source of cards
     */
    public void exchange(ArrayList<Integer> positions, Deck deck) {

        ArrayList<Card> Cards = getCards();
        for (int i : positions) {
            deck.add(Cards.get(i));
            Cards.remove(i);
            Cards.add(i, deck.getCards().remove(0));
        }
    }

    /**
     * method called when player has decided to add more cash to the pool
     * @param stake amount of money to subtract from cash
     */
    public void raise(int stake) {
        this.cash -= stake;
    }

    public void clearHand() {
        cards.clear();
    }
}
