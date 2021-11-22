package pl.edu.agh.kis.pz1;


import pl.edu.agh.kis.pz1.Utils.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server {

    public static final int PORT = 5555;
    private final ServerSocket serverSocket;
    private final Logger logger = Logger.getLogger(Server.class.getName());

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public Logger getLogger() {
        return logger;
    }

    public void serverStart(){
        try{
            while(!serverSocket.isClosed() && !Utils.gameStarted) {
                Socket socket = serverSocket.accept();
                logger.log(Level.INFO,"A player has jointed the game.");

                ClientHandler clientHandler = new ClientHandler(socket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();

                if (ClientHandler.clients.size() < 2) {
                    ClientHandler.clients.get(0).sendMessageToThisPlayer("You need at least 2 players to start the game.");
                }
                else if (ClientHandler.clients.size() < 4) {
                    Boolean [] responses = new Boolean[ClientHandler.clients.size()];
                    ClientHandler.clients.get(0).broadcastMessage("Satisfied minimum number of players. Start the game?(y/n)");
                    ClientHandler.clients.get(0).sendMessageToThisPlayer("Satisfied minimum number of players. Start the game?(y/n)");
                    for (ClientHandler client: ClientHandler.clients) {
                        String response = client.getReader().readLine();
                        responses[ClientHandler.clients.indexOf(client)] = response.equals("y");
                    }
                    Arrays.sort(responses);
                    boolean previous = true;
                    for (Boolean i : responses) {
                        Utils.gameStarted = previous && i;
                        previous = i ;
                    }
                    if (!Utils.gameStarted) {
                        ClientHandler.clients.get(0).broadcastMessage("Not everyone agreed.");
                        ClientHandler.clients.get(0).sendMessageToThisPlayer("Not everyone agreed.");
                    }

                } else {
                    ClientHandler.clients.get(0).broadcastMessage("Reached max number of players. The game is starting now.");
                    ClientHandler.clients.get(0).sendMessageToThisPlayer("Reached max number of players. The game is starting now.");
                    Utils.gameStarted = true;
                }
            }
        } catch (IOException e){
            closeConnection();
        }
    }

    public void closeConnection(){
        try {
            if (serverSocket != null){

                serverSocket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String [] args) throws IOException, InterruptedException {

        ServerSocket serverSocket = new ServerSocket(PORT);
        Server server = new Server(serverSocket);

        Logger logger = server.getLogger();
        logger.log(Level.INFO, "SERVER STARTED.\nWaiting for players...");
        server.serverStart();
        TimeUnit.SECONDS.sleep(1);

        logger.log(Level.INFO, "GAME STARTED");
        Game game = new Game();

        ClientHandler.clients.get(0).broadcastMessage("First player, who joined will set ante.");
        ClientHandler.clients.get(0).sendMessageToThisPlayer("First player, who joined will set ante.\nSet ante: ");
        int ante = Integer.parseInt(ClientHandler.clients.get(0).getReader().readLine());

        game.setAnte(ante);
        game.setPlayersCash();
        game.startGame();
        game.firstRound();
        game.secondRound();
        serverSocket.close();
        System.exit(0);
    }

    }


