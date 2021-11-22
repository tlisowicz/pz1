package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.model.Deck;
import pl.edu.agh.kis.pz1.model.Player;
import java.io.IOException;


public class Game {

    private final Deck deck;
    private int ante;
    private int pool = 0;
    private int actualStake;
    public Game() {
        this.deck = new Deck();
        this.deck.shuffle();
        }

    public void setAnte(int ante) {
        this.ante = ante;
        this.actualStake = ante;
    }

    public Deck getDeck() {
        return deck;
    }

    public void startGame() {
        for (ClientHandler clientHandler: ClientHandler.clients) {
            clientHandler.getPlayer().pickCards(deck);
            clientHandler.getPlayer().takeAnte(ante);
            pool += ante;
        }
        showPlayersCardsAndCash();
    }

    public void showPlayersCardsAndCash(){
        for (ClientHandler clientHandler : ClientHandler.clients) {
            clientHandler.sendMessageToThisPlayer(clientHandler.getPlayer().showCards());
            clientHandler.sendMessageToThisPlayer(clientHandler.getPlayer().showCash());
        }
    }


    public void setPlayersCash() throws IOException {
        for (ClientHandler clientHandler : ClientHandler.clients) {

            clientHandler.sendMessageToThisPlayer("Set your cash: ");
            while (clientHandler.getPlayer().getCash() <= 0 ) {
                int cash = Integer.parseInt(clientHandler.getReader().readLine());
                if (cash <= 0) {
                    clientHandler.sendMessageToThisPlayer("Provided wrong amount try again: ");
                }
                clientHandler.getPlayer().setCash(cash);
            }
        }
    }

    public void firstRound()  {
        boolean check = false;
        while (!check && ClientHandler.clients.size() > 1) {
            for (ClientHandler clientHandler : ClientHandler.clients) {
                clientHandler.setMyTurn(true);
                clientHandler.sendMessageToThisPlayer("What do you want to do?\n1. check\n2. raise\n3. fold");
                boolean action = false;

                while (!action) {
                    try {
                        int response = Integer.parseInt(clientHandler.getReader().readLine());
                        switch (response) {
                            case 1:
                                check = true;
                                calculateWhoWins();
                                break;

                            case 2:
                                int stake = actualStake;
                                while (actualStake <= stake) {
                                    clientHandler.sendMessageToThisPlayer("Actual stake is: " + actualStake + " input new stake: ");
                                    actualStake = Integer.parseInt(clientHandler.getReader().readLine());
                                    clientHandler.getPlayer().raise(actualStake-ante);
                                    pool += actualStake-ante;
                                    if (actualStake<= stake) {
                                        clientHandler.sendMessageToThisPlayer("New stake cannot be lower than actual stake.");
                                    }
                                }
                                action = true;
                                break;

                            case 3:
                                clientHandler.getPlayer().fold(deck);
                                clientHandler.sendMessageToThisPlayer("You have folded!");
                                ClientHandler.clients.remove(clientHandler);
                                System.out.println("Player: " + clientHandler.getName() + " folded");
                                action = true;
                                break;

                        }
                    } catch (NumberFormatException | IOException e) {
                        clientHandler.sendMessageToThisPlayer("Wrong input");
                    }
                }
                clientHandler.setMyTurn(false);
            }
        }
    }

    public void secondRound() throws IOException {
        if (ClientHandler.clients.size() > 1) {
            for (ClientHandler clientHandler : ClientHandler.clients) {
                clientHandler.sendMessageToThisPlayer("Do you want to exchange cards? (y/n)");
                if (clientHandler.getReader().readLine().equals("y")) {

                    clientHandler.sendMessageToThisPlayer("Which cards do you want to exchange? (all indexes in one line):");
                    try {
                        clientHandler.getPlayer().exchange(clientHandler.getReader().readLine(), getDeck());
                        clientHandler.sendMessageToThisPlayer(clientHandler.getPlayer().showCards());
                    } catch (IndexOutOfBoundsException e) {
                        clientHandler.sendMessageToThisPlayer("Given invalid string of indexes.");
                    }
                }
            }
            firstRound();
        } else {
            ClientHandler.clients.get(0).sendMessageToThisPlayer("You won!\nYour prize: "+ pool);
        }

    }



    public Player calculateWhoWins(){
        for (ClientHandler clientHandler: ClientHandler.clients) {
            clientHandler.getPlayer().getCards();
        }
        return null;
    }

}