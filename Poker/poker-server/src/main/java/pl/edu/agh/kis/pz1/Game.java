package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.model.Card;
import pl.edu.agh.kis.pz1.model.Deck;
import pl.edu.agh.kis.pz1.model.Player;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing poker game
 */
public class Game {

    private final Deck deck;
    private int ante;
    private int pool;
    private int actualStake;
    private final ArrayList<Integer> playersWhoFolded = new ArrayList<>();

    /**
     * Constructor. After initialization, deck is shuffled 3 times
     */
    public Game() {
        deck = new Deck();
        pool = 0;
        ante = 0;
        actualStake = 0;
        for (int i = 0; i < 3; ++i) {
            deck.shuffle();
        }
    }

    public Deck getDeck() {
        return deck;
    }

    /**
     * Sends message to first client who jointed takes response and sets ante for the game
     */
    public void setAnte() {
        ClientHandler.clients.get(0).broadcastMessage("First player, who joined will set ante.");
        int playersChoice = getInputFromPlayer(ClientHandler.clients.get(0), "Set ante:");
        ClientHandler.clients.get(0).broadcastMessage("Ante is set for "+ playersChoice + "$");
        ante = playersChoice;
    }

    /**
     * Starts the game. Takes ante from every player and deals 5 cards to every player
     */
    public void startGame() {
        for (ClientHandler clientHandler: ClientHandler.clients) {
            clientHandler.getPlayer().takeAnte(ante);
            pool += ante;
        }
        for (int i = 0; i < 5; ++i) {
            for (ClientHandler clientHandler: ClientHandler.clients) {
                clientHandler.getPlayer().getCard(deck.getCards().remove(0));
            }
        }
        showPlayersCardsAndCash();
    }

    /**
     * shows players their cards and cash
     */
    public void showPlayersCardsAndCash(){
        for (ClientHandler clientHandler : ClientHandler.clients) {
            clientHandler.sendMessageToThisPlayer(clientHandler.getPlayer().showCards());
            clientHandler.sendMessageToThisPlayer(clientHandler.getPlayer().showCash());
        }
    }

    /**
     * Asks every client to set his cash, takes response and sets cash
     */
    public void setPlayersCash() {
        for (ClientHandler clientHandler : ClientHandler.clients) {
            int playersCash = getInputFromPlayer(clientHandler, "Set your cash:");
            clientHandler.getPlayer().setCash(playersCash);
        }
    }

    /**
     * Util method to take integer input from player. Number cannot be lower than 0
     * @param client client from whom input will be taken
     * @param message message to send while asking for input
     * @return integer
     */
    private int getInputFromPlayer(ClientHandler client , String message) {
        client.sendMessageToThisPlayer(message);
        int result = 0;
        do {
            try {
                result = Integer.parseInt(client.getReader().readLine());
                if (result <=  0) {
                    throw  new IOException();
                }
                break;
            } catch (IOException | NumberFormatException e) {
                client.sendMessageToThisPlayer("Wrong number. Try again: ");
            }
        } while (true);
        return result;
    }

    /**
     * This is the first round of the game. Firstly, game checks how many players has left in. Then player broadcasts that it
     * is his turn now. First player has 3 options: He can check, rise or fold. If he checks he doesn't add any money to the pool.
     * For player to have ability to check it is required first player to check. If first player rises or folds next players
     * can't check. If player rises, he has bet current stake. If player folds, he no longer participates in a game. When
     * every player has taken an action the game moves to second round.
     * @param logger just to log some messages to server
     */
    public void firstRound(Logger logger) {
        boolean canNextPlayerCheck = true;
        ArrayList<Player> hasToCall = new ArrayList<>();

        for (ClientHandler clientHandler : ClientHandler.clients) {
            if (playersWhoFolded.contains(clientHandler.getId())) {
                continue;
            }
            //if only one player left in a game
            if (ClientHandler.clients.size() - playersWhoFolded.size() == 1 && !playersWhoFolded.contains(clientHandler.getId())) {
                clientHandler.sendMessageToThisPlayer("You won!\nYour prize: " + pool);
                clientHandler.broadcastMessage("Player "+ clientHandler.getName() + " won!");
                break;
            }
            clientHandler.setMyTurn(true);
            clientHandler.broadcastMessage("It's " + clientHandler.getName()+ "'s turn.\n");

            if (hasToCall.contains(clientHandler.getPlayer())) {

                actionWhenPlayerHasToCall(clientHandler, hasToCall, canNextPlayerCheck, logger);
            }
            else {
                clientHandler.sendMessageToThisPlayer(clientHandler.getPlayer().showCards());
                clientHandler.sendMessageToThisPlayer(clientHandler.getPlayer().showCash());
                int response = getInputFromPlayer(clientHandler, "What do you want to do?\n1. check\n2. raise\n3. fold");
                switchResponse(clientHandler, response, canNextPlayerCheck, hasToCall, logger);

                //if this player checks next one also can check
                if (response != 1) {
                    canNextPlayerCheck = false;
                }
            }
        }
        //this handles the situation where f.e. 3rd player has risen the stake and first and second player didn't.
        while (!hasToCall.isEmpty()) {
            for (ClientHandler clientHandler: ClientHandler.clients) {

                //if player has folded, he shouldn't be counted
                if (playersWhoFolded.contains(clientHandler.getId())) {
                    hasToCall.remove(clientHandler.getPlayer());
                }
                if (hasToCall.contains(clientHandler.getPlayer())) {

                    actionWhenPlayerHasToCall(clientHandler, hasToCall, canNextPlayerCheck, logger);
                }

            }
        }
    }

    /**
     * Util method to perform action depending on player's response
     * @param clientHandler current player
     * @param response response
     * @param canNextPlayerCheck used in playerChecks
     * @param hasToCall used in playerRises, playerChecks
     * @param logger logger to log some messages to server
     */
    private void switchResponse(ClientHandler clientHandler, int response, boolean canNextPlayerCheck, ArrayList<Player> hasToCall, Logger logger) {
        boolean goodIndex = false;

        while (!goodIndex){
            switch (response) {
                case (1):
                    playerChecks(clientHandler, canNextPlayerCheck,hasToCall,logger);
                    goodIndex = true;
                    break;

                case (2):
                    playerRises(clientHandler, hasToCall, canNextPlayerCheck, logger);
                    goodIndex = true;
                    break;

                case (3):
                    playersWhoFolded.add(clientHandler.getId());
                    removePlayer(clientHandler, logger);
                    goodIndex = true;
                    break;

                default:
                    while (response >= 4 || response <= 0) {
                        response = getInputFromPlayer(clientHandler, "Wrong number provided. Try again: ");
                    }
            }
        }
    }

    /**
     * This method is called when one of the players has risen the stake and hence other players has to call, rise, or fold
     * @param clientHandler client that has to take an action
     * @param hasToCall arraylist of clients that has to take an action. Passed just to delete player who has taken an action
     * @param canNextPlayerCheck boolean that determinate if this player can check
     * @param logger logger to log some messages to the server
     */
    private void actionWhenPlayerHasToCall(ClientHandler clientHandler, ArrayList<Player> hasToCall, boolean canNextPlayerCheck, Logger logger) {
        int actionWhenStakeRisen = getInputFromPlayer(clientHandler, "One of the previous player has risen the stake (Actual stake: " + actualStake + " $). You have to call, rise or fold:\n1.call\n2.rise\n3.fold");

        boolean goodIndex = false;
        while (!goodIndex) {
            switch (actionWhenStakeRisen) {
                case (1):
                    hasToCall.remove(clientHandler.getPlayer());
                    playerCalls(clientHandler, logger);
                    goodIndex = true;
                    break;

                case (2):
                    playerRises(clientHandler,hasToCall,canNextPlayerCheck, logger);
                    goodIndex = true;
                    break;

                case (3):
                    playersWhoFolded.add(clientHandler.getId());
                    removePlayer(clientHandler, logger);
                    goodIndex = true;
                    break;

                default:
                    while (actionWhenStakeRisen >= 4 || actionWhenStakeRisen <= 0) {
                        actionWhenStakeRisen = getInputFromPlayer(clientHandler, "Wrong number provided. Try again: ");
                    }
            }
        }
    }

    /**
     * Called when player checks
     * @param clientHandler client who checks
     * @param allowedToCheck boolean that determinate if player can do this
     * @param hasToCall used in switchResponse method
     * @param logger used in switchResponse method
     */
    private void playerChecks(ClientHandler clientHandler, boolean allowedToCheck, ArrayList<Player> hasToCall, Logger logger) {
        if (allowedToCheck) {
            clientHandler.sendMessageToThisPlayer("You have checked.\n");
            clientHandler.broadcastMessage("Player " + clientHandler.getName() + " checks.\n");
        } else {
            int actionWhenCantCheck = 0;
            while (actionWhenCantCheck <= 1) {

                actionWhenCantCheck = getInputFromPlayer(clientHandler,"You can't check because previous player's haven't done so. You still can:\n2. raise\n3. fold ");
            }
            switchResponse(clientHandler, actionWhenCantCheck, allowedToCheck, hasToCall, logger);
        }
    }

    /**
     *
     * @param clientHandler client who rises
     * @param hasToCall arraylist of clients who will have to call, rise or fold after this player rises
     * @param canNextPlayerCheck used in switchResponse method
     * @param logger used in switchResponse method
     */
    private void playerRises(ClientHandler clientHandler, ArrayList<Player> hasToCall, boolean canNextPlayerCheck, Logger logger) {
        int newStake = actualStake;
        while (newStake <= actualStake || newStake > clientHandler.getPlayer().getCash()) {

            newStake = getInputFromPlayer(clientHandler, "Actual stake is: " + actualStake + " input new stake: ");
            if (newStake <= actualStake) {
                clientHandler.sendMessageToThisPlayer("New stake cannot be lower than actual stake.");
            }
            if (newStake > clientHandler.getPlayer().getCash()) {
                int response = 0;
                while (response <= 0 || response == 2) {
                    response = getInputFromPlayer(clientHandler,"You don't have enough cash. You can instead:\n3. fold");
                }
                switchResponse(clientHandler,response,canNextPlayerCheck, hasToCall, logger);
                break;
            }
        }
        actualStake = newStake;
        clientHandler.broadcastMessage("Player: " + clientHandler.getName() + " has risen stake! New stake is: " + actualStake + " $");
        for (ClientHandler client: ClientHandler.clients) {
            if (hasToCall.contains(client.getPlayer())) {
                continue;
            }
            hasToCall.add(client.getPlayer());
            hasToCall.remove(clientHandler.getPlayer());
        }
        clientHandler.getPlayer().raise(actualStake);
        pool += newStake;
        clientHandler.sendMessageToThisPlayer(clientHandler.getPlayer().showCash());
    }

    /**
     * Called when player calls
     * @param clientHandler client who calls
     * @param logger logger to log some messages to the server
     */
    private void playerCalls(ClientHandler clientHandler, Logger logger) {

        if (actualStake > clientHandler.getPlayer().getCash()) {
            clientHandler.sendMessageToThisPlayer("You don't have enough money to play further. You have to pass.");
            playersWhoFolded.add(clientHandler.getId());
            removePlayer(clientHandler, logger);
        } else {
            clientHandler.broadcastMessage("Player: " + clientHandler.getName() + " has has called!");
            clientHandler.getPlayer().raise(actualStake);
            pool += actualStake;
            clientHandler.sendMessageToThisPlayer(clientHandler.getPlayer().showCash());

        }
    }

    /**
     * Second round of the game. Firstly, game checks if everyone but one player folded. If so the left one is the winner.
     * Otherwise players can exchange their cards and then take an action.
     * @param logger logger to log some messages to the server
     * @throws IOException when
     */
    public void secondRound(Logger logger) throws IOException {

        // if everyone but one player folded
        if (ClientHandler.clients.size() - playersWhoFolded.size() == 1) {
            for (ClientHandler clientHandler : ClientHandler.clients) {

                if (!playersWhoFolded.contains(clientHandler.getId())) {

                    clientHandler.sendMessageToThisPlayer("You won!\nYour prize: " + pool);
                    clientHandler.broadcastMessage("Player "+ clientHandler.getName() + " won!");
                }
            }
        } else {
            actualStake = 0;
            for (ClientHandler clientHandler : ClientHandler.clients) {

                clientHandler.broadcastMessage("It's " + clientHandler.getName()+ " turn.");
                if (playersWhoFolded.contains(clientHandler.getId())) {
                    continue;
                }
                clientHandler.sendMessageToThisPlayer(clientHandler.getPlayer().showCards());
                clientHandler.sendMessageToThisPlayer(clientHandler.getPlayer().showCash());

                clientHandler.sendMessageToThisPlayer("Do you want to exchange cards? (y/n)");
                if (clientHandler.getReader().readLine().equals("y")) {

                    ArrayList<Integer> indexes = getIndexesAsArrayList(clientHandler);
                    clientHandler.getPlayer().exchange(indexes, getDeck());
                    clientHandler.sendMessageToThisPlayer(clientHandler.getPlayer().showCards());
                }
            }
            firstRound(logger);
        }
    }

    /**
     * Called when player want to exchange his cards
     * @param clientHandler client who want to exchange
     * @return arraylist of integers representing cards to exchange
     */
    private ArrayList<Integer> getIndexesAsArrayList(ClientHandler clientHandler) {
        String indexes;
        ArrayList<Integer> result;
        clientHandler.sendMessageToThisPlayer("Which cards do you want to exchange? (all indexes in one line):");

        while (true) {
            try {
                 indexes = clientHandler.getReader().readLine();
                break;
            } catch (IOException e) {
                clientHandler.sendMessageToThisPlayer("Given invalid string of indexes. Input correct indexes:");
            }
        }
        while (true) {
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
                result = new ArrayList<>(positions);
                break;
            } catch (IndexOutOfBoundsException e) {
                clientHandler.sendMessageToThisPlayer("Given invalid string of indexes. Input correct indexes:");
            }
        }

        return result;
    }

    /**
     * Called when player folds. Broadcasts message, who has folded and logs int to the server
     * @param client client who folds
     * @param logger server loger
     */
    public void removePlayer(ClientHandler client, Logger logger) {
        client.sendMessageToThisPlayer("You have folded!\n");
        client.broadcastMessage("Player: " + client.getName() + " has folded!\n");
        String message = String.format("Player %s id: %d folded", client.getName() , client.getId());
        logger.log(Level.INFO, message);
    }

    /**
     * For every player evaluates his score, compares to other players and returns winner
     * @return index of winner
     */
    public int calculateWhoWins(){

        Map<Integer, ArrayList<Integer>> playersHands = new HashMap<>();
        for (ClientHandler clientHandler: ClientHandler.clients) {
            if (playersWhoFolded.contains(clientHandler.getId())) {
                continue;
            }
            int playersId = clientHandler.getId();
            ArrayList<Card> cards =  clientHandler.getPlayer().getCards();

            int[] ranks = new int[14];
            Arrays.fill(ranks, 0);

            for (int i = 0; i <= 4; i++) {
                // +1 to keep consistency with real ranks one is 1 etc.
                ranks[ cards.get(i).getRank().ordinal() + 1 ]++;
            }

            //Two variables that will show if there is a pair,three or four of a kind
            //Two variables are needed to resolve cases of multiple pairs, full house etc.
            int sameCards = 1;
            int sameCards2 = 1;
            int largeGroupRank = 0;
            int smallGroupRank = 0;

            for (int i = 13; i>=1; --i) {
                if (ranks[i] > sameCards) {
                    if (sameCards != 1) {
                        sameCards2 = sameCards;
                        smallGroupRank = largeGroupRank;

                    }
                    sameCards = ranks[i];
                    largeGroupRank = i;

                } else if (ranks[i] > sameCards2) {
                    sameCards2 = ranks[i];
                    largeGroupRank = i;
                }
            }
            //checking if there is a flush
            boolean flush = isFlush(cards);

            //checking if there is a straight
            boolean straight = isStraight(ranks);
            int topStraightValue = topStraightValue(ranks);

            // evaluating player's hand
            ArrayList<Integer> valuesOfCardsInPlayersHand;
            valuesOfCardsInPlayersHand = evaluate(ranks, sameCards, sameCards2, smallGroupRank, largeGroupRank, straight, flush, topStraightValue);

            playersHands.put(playersId, valuesOfCardsInPlayersHand);
        }
        return selectBiggestScore(playersHands);
    }

    /**
     * Checks if there is a flush
     * @param cards cards to check
     * @return true when there is a flush, false otherwise
     */
    private boolean isFlush(ArrayList<Card> cards) {
        for (int x = 0; x < 4; x++) {
            if (cards.get(x).getSuit() != cards.get(x + 1).getSuit()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if there is a straight
     * @param ranks cards to check
     * @return true when there is a straight, false otherwise
     */
    private boolean isStraight(int [] ranks) {

        //case with an ace
        if (ranks[10] == 1 && ranks[11] == 1 && ranks[12] == 1 && ranks[13] == 1 && ranks[1] == 1) {
            return true;
        }
        for (int x = 1; x <= 9; x++) {
            if (ranks[x] == 1 && ranks[x+1] == 1 && ranks[x+2] == 1 && ranks[x+3] == 1 && ranks[x+4] == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Called with isStraight. Used to determinate 'power' of a straight. Straight from ten to ace is the strongest one.
     * @param ranks cards which values will be taken
     * @return 'power' of a straight
     */
    private int topStraightValue(int [] ranks) {
        if (ranks[10] == 1 && ranks[11] == 1 && ranks[12] == 1 && ranks[13] == 1 && ranks[1] == 1) {

            return 14;
        }
        for (int x = 1; x <= 9; x++) {
            if (ranks[x] == 1 && ranks[x+1] == 1 && ranks[x+2] == 1 && ranks[x+3] == 1 && ranks[x+4] == 1) {
                return x + 4;
            }
        }
        return 0;
    }

    /**
     * evaluates
     * @param ranks cards to evaluate
     * @param sameCards number representing if there is a pair or three/four of a kind
     * @param sameCards2 number representing if there is a second pair or three/four of a kind
     * @param smallGroupRank rank of pair or three/four of a kind
     * @param largeGroupRank rank of second pair or three/four of a kind
     * @param straight boolean
     * @param flush boolean
     * @param topStraightValue 'power' of a flush
     * @return Total score of cards i player's hand as an arraylist
     */
    private ArrayList<Integer> evaluate(int [] ranks, int sameCards, int sameCards2, int smallGroupRank, int largeGroupRank, boolean straight, boolean flush, int topStraightValue) {

        ArrayList<Integer> result = new ArrayList<Integer>();
        //creating a table of values representing value of a card
        int [] orderedRanks = new int[5];
        int index = 0;
        if (ranks[1] == 1) {
            orderedRanks[index] = 14;
            index++;
        }

        for (int i = 13; i >= 2; --i) {
            if (ranks[i] == 1) {
                orderedRanks[index] = i;
                index++;
            }
        }

        //royal flush
        if (straight && flush && topStraightValue == 14) {
            result.add(10);
        }

        //straight flush
        else if (straight && flush) {
            result.add(9);
        }

        //four of a kind
        else if (sameCards==4) {
            result.add(8);
            result.add(largeGroupRank);
            result.add(orderedRanks[0]);
        }

        //full house
        else if (sameCards==3 && sameCards2==2) {
            result.add(7);
            result.add(largeGroupRank);
            result.add(smallGroupRank);
        }

        //flush
        else if (flush) {
            result.add(6);
            for (int i = 0; i < 5; ++i) {
                //tie determined by ranks of cards
                result.add(orderedRanks[i]);
            }
        }

        //straight
        else if (straight) {
            result.add(5);
            result.add(topStraightValue);
        }

        //tree of a kind
        else if (sameCards == 3) {
            result.add(4);
            result.add(largeGroupRank);
            result.add(orderedRanks[0]);
            result.add(orderedRanks[1]);
        }

        //two pairs
        else if (sameCards==2 && sameCards2==2) {
            result.add(3);
            //rank of greater pair
            result.add(Math.max(largeGroupRank, smallGroupRank));
            result.add(Math.min(largeGroupRank, smallGroupRank));
            result.add(orderedRanks[0]);  //extra card
        }

        //one pair
        else if (sameCards == 2 && sameCards2 == 1) {
            result.add(2);
            result.add(largeGroupRank);
            for (int i = 0; i < 3; ++i) {
                result.add(orderedRanks[i]);
            }
        }

        //no pair high card
        else if (sameCards == 1 ) {

            //first value is 'power' of combination
            result.add(1);
            for (int i = 0; i < 5; ++i) {
                result.add(orderedRanks[i]);
            }
        }

        return result;
    }

    /**
     * Calculates which player has the biggest score
     * @param playersHands Map with player's id as a key and arraylist of his score as a value
     * @return id of player
     */
    private int selectBiggestScore(Map<Integer, ArrayList<Integer>> playersHands) {
        Iterator<Map.Entry<Integer, ArrayList<Integer>>> entries = playersHands.entrySet().iterator();
        int playerIndex = -1;
        int highestValue = 0;
        int [] nextValuesOfCards = new int[5];
        Arrays.fill(nextValuesOfCards, 0);
        while (entries.hasNext()) {
            Map.Entry<Integer, ArrayList<Integer>> entry = entries.next();
            if (entry.getValue().get(0) > highestValue) {
                playerIndex = entry.getKey();
                highestValue = entry.getValue().get(0);
                Arrays.fill(nextValuesOfCards, 0);
                for (int i = 1; i < entry.getValue().size(); ++i) {
                    nextValuesOfCards[i-1] = entry.getValue().get(i);
                }
            } else if (entry.getValue().get(0) == highestValue) {

                int hasBetterCardsThanPrevious = compare(entry.getValue(), nextValuesOfCards);
                if (hasBetterCardsThanPrevious == 1) {
                    playerIndex = entry.getKey();
                    Arrays.fill(nextValuesOfCards, 0);
                    for (int i = 1; i < entry.getValue().size(); ++i ) {
                        nextValuesOfCards[i-1] = entry.getValue().get(i);
                    }
                }
            }
        }
        return playerIndex;
    }

    /**
     * Util method to compare players' 'hands'
     * @param currentPlayersCards cards of current player
     * @param previousPlayersCards values of previous player's cards
     * @return 1 if currently tested player has better cards, -1 when cards are worse, 0 if they are equal
     */
    public int compare(ArrayList<Integer> currentPlayersCards, int [] previousPlayersCards) {
        for (int i = 1; i< currentPlayersCards.size(); ++i) {
            if (currentPlayersCards.get(i) > previousPlayersCards[i-1]) {
                return 1;
            } else if (currentPlayersCards.get(i) < previousPlayersCards[i-1]) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * Broadcasts who win
     */
    public void showWinner() {
        int IndexOfWinner = calculateWhoWins();
        ClientHandler.clients.get(IndexOfWinner).sendMessageToThisPlayer("You won!\nYour prize: " + pool + "$");
        ClientHandler.clients.get(IndexOfWinner).broadcastMessage("Player: " + ClientHandler.clients.get(IndexOfWinner).getName() + " won!");
    }

    /**
     * take response from every client and  evaluates if everyone agreed
     * @param responses arraylist to store responses
     * @return true when everyone agrees, false otherwise
     */
    private boolean takeResponse(ArrayList<Integer> responses) {
        for (ClientHandler client: ClientHandler.clients) {
            while (true) {

                try {
                    String response = client.getReader().readLine();
                    responses.add(response.equals("y") ?  1 : 0);
                    break;

                } catch (IOException e) {
                    ClientHandler.clients.get(0).sendMessageToThisPlayer("Wrong input.");
                }
            }
        }
        if (!responses.contains(0)) {
            return true;
        }
        else {
            ClientHandler.clients.get(0).broadcastMessage("Not everyone agreed.");
            ClientHandler.clients.get(0).sendMessageToThisPlayer("Not everyone agreed.");
        }
        return false;
    }
    /**
     * asks players if they want to play again and takes responses
     * @return true if everyone has agreed, false otherwise
     */
    public boolean playAgain()  {
        ArrayList<Integer> responses = new ArrayList<>();
        ClientHandler.clients.get(0).broadcastMessage("Do you want to play again?(y/n)");
        ClientHandler.clients.get(0).sendMessageToThisPlayer("Do you want to play again?(y/n)");
        return takeResponse(responses);
    }

    /**
     * Removes Card of players' hands and cash from previous game
     */
    public void clearHandsAndCash() {
        for (ClientHandler clientHandler: ClientHandler.clients) {
            clientHandler.getPlayer().clearHand();
            clientHandler.getPlayer().setCash(0);
        }
    }
}