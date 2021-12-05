package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.model.Player;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class is responsible for communicating with clients.
 */
public class ClientHandler implements Runnable {

    /**
     * List of every instantiated object of this class
     */
    public static ArrayList<ClientHandler> clients = new ArrayList<>();
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String name;
    private int id;
    private boolean isMyTurn = false;
    private Player player;

    /**
     * Constructor of client handler. For every client who has established connection to server an instance of client handler is created.
     * @param socket socked connected to a server
     */
    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.name = reader.readLine();
            clients.add(this);
            this.id = clients.indexOf(this);
            this.player = new Player();
            broadcastMessage("SERVER: " + name + " has jointed the game.");

        } catch (IOException e) {

            closeStreams(socket, writer, reader);
        }
    }
    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BufferedReader getReader() {
        return reader;
    }

    /**
     * return a player object associated with a client
     * @return player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Override of run method to deal with multiple clients independently
     */
    @Override
    public void run() {
      String messageFromClient;

        while (socket.isConnected() && isMyTurn()) {
            try {
                messageFromClient = reader.readLine();
                broadcastMessage(messageFromClient);

            } catch (IOException e ) {

                closeStreams(socket, writer, reader);
                break;
            }

        }

    }

    /**
     * method to send a message for every connected client
     * @param message String to broadcast
     */
    public void broadcastMessage(String message) {
        for (ClientHandler clientHandler : clients) {
            try {
                if (clientHandler.getId() != id) {

                    clientHandler.writer.write(message);
                    clientHandler.writer.newLine();
                    clientHandler.writer.flush();
                }
            } catch (IOException e) {

                closeStreams(socket, writer, reader);
            }
        }
    }

    /**
     * method to send message directly for one player
     * @param message String to send
     */
    public void sendMessageToThisPlayer(String message) {
        for (ClientHandler clientHandler : clients) {
            try {
                if (clientHandler.getId() == id) {

                    clientHandler.writer.write(message);
                    clientHandler.writer.newLine();
                    clientHandler.writer.flush();
                }
            } catch (IOException e) {

                closeStreams(socket, writer, reader);
            }
        }
    }

    /**
     * removes client handler instance and broadcasts information about that
     */
    public void removeClient() {
        clients.remove(this);

        if (!clients.isEmpty()){
            broadcastMessage("SERVER: " + name + " passed!.");

        }
    }

    /**
     * close every I/O stream associated with a client
     * @param socket client's socket
     * @param writer client's buffer reader
     * @param reader client's buffer writer
     */
    public void closeStreams(Socket socket, BufferedWriter writer, BufferedReader reader) {
        removeClient();
        try {
            if (reader != null){
               reader.close();
            }
            if (writer != null){
                writer.close();
            }
            if (socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
