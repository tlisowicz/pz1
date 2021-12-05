package pl.edu.agh.kis.pz1;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class representing server
 */
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

    /**
     * method called in order to connect players to the server. After player's connection message: 'waiting for x players is displayed'
     */
    public void serverStart(int numberOfPlayers) {
        try {
            while(!serverSocket.isClosed() && ClientHandler.clients.size() < numberOfPlayers) {
                int playersLeftToConnect =  numberOfPlayers - ClientHandler.clients.size();
                Socket socket = serverSocket.accept();
                logger.log(Level.INFO,"Player has jointed the game.");

                ClientHandler clientHandler = new ClientHandler(socket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
                --playersLeftToConnect;
                if (playersLeftToConnect != 0) {

                    ClientHandler.clients.get(ClientHandler.clients.size()-1).sendMessageToThisPlayer("Waiting for "+ playersLeftToConnect + " more players.");
                }
            }
        } catch (IOException e){
            closeConnection();
        }
    }

    /**
     * method for closing server socked
     */
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
        if (Integer.parseInt(args[0]) <=0 || Integer.parseInt(args[0]) > 4) {
            serverSocket.close();
            logger.log(Level.INFO, "Illegal argument.");
            System.exit(-1);
        }
        logger.log(Level.INFO, "SERVER STARTED.\nWaiting for players...");
        server.serverStart(Integer.parseInt(args[0]));

        while (true) {
            TimeUnit.SECONDS.sleep(1);

            logger.log(Level.INFO, "GAME STARTED");
            Game game = new Game();

            game.setAnte();
            game.setPlayersCash();
            game.startGame();
            game.firstRound(logger);
            game.secondRound(logger);
            game.showWinner();
            logger.log(Level.INFO, "GAME ENDED");
            TimeUnit.SECONDS.sleep(2);
            game.clearHandsAndCash();
            if (!game.playAgain()) {
                break;
            }
        }
    }
}


