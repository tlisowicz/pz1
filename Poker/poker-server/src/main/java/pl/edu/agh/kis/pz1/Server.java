package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.model.Game;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class Server {

    public static final int PORT = 5555;
    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void serverStart(){
        try{
            while(!serverSocket.isClosed() && !Game.gameStarted) {
                Socket socket = serverSocket.accept();
                System.out.println("A player has jointed the game.");

                ClientHandler clientHandler = new ClientHandler(socket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();

                if (ClientHandler.clients.size() < 2) {
                    ClientHandler.clients.get(0).loopbackMessage("You need at least 2 players to start the game.");
                }
                else if (ClientHandler.clients.size() < 4) {
                    Boolean [] responses = new Boolean[ClientHandler.clients.size()];
                    ClientHandler.clients.get(0).broadcastMessage("Satisfied minimum number of players. Start the game?(y/n)");
                    ClientHandler.clients.get(0).loopbackMessage("Satisfied minimum number of players. Start the game?(y/n)");
                    for (ClientHandler client: ClientHandler.clients) {
                        String response = client.getReader().readLine();
                        responses[ClientHandler.clients.indexOf(client)] = response.equals("y");
                    }
                    Arrays.sort(responses);
                    boolean previous = true;
                    for (Boolean i : responses) {
                        Game.gameStarted = previous && i;
                        previous = i ;
                    }
                    if (!Game.gameStarted) {
                        ClientHandler.clients.get(0).broadcastMessage("Not everyone agreed.");
                        ClientHandler.clients.get(0).loopbackMessage("Not everyone agreed.");
                    }
                } else {
                    ClientHandler.clients.get(0).broadcastMessage("Reached max number of players. The game is starting now.");
                    ClientHandler.clients.get(0).loopbackMessage("Reached max number of players. The game is starting now.");
                    Game.gameStarted = true;
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
        System.out.println("SERVER STARTED.\n Waiting for players...");
        server.serverStart();
        TimeUnit.SECONDS.sleep(1);

        Game game = new Game(ClientHandler.clients.size());

    }

    }


