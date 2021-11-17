package pl.edu.agh.kis.pz1.model;



import java.util.ArrayList;

public class Game {

    public static boolean gameStarted = false;
    private Deck deck;
    private ArrayList<Player> players = new ArrayList<>();
    private int ante;

    public Game(int howManyPlayers) {
        //Creating Deck
        this.deck = new Deck();
        this.deck.shuffle();

        //Creating players
        for (int i = 0; i < howManyPlayers - 1; ++i) {
            Player player = new Player();
            player.pickCards(deck);
            players.add(player);
        }
    }

    public void setAnte(Player player) {

    }
}
