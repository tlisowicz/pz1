package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.Utils.Utils;
import pl.edu.agh.kis.pz1.model.Player;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clients = new ArrayList<>();
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String name;
    private int id;
    private boolean isMyTurn = false;
    private Player player;
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

    public Socket getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public Player getPlayer() {
        return player;
    }


    @Override
    public void run() {
       String messageFromClient;

        while (socket.isConnected() && !isMyTurn()) {
            try {
                if (Utils.gameStarted) {
                    messageFromClient = reader.readLine();
                    broadcastMessage(messageFromClient + "Its not your turn.");
                }

            } catch (IOException e ) {

                closeStreams(socket, writer, reader);
                break;
            }

        }

    }

    public void broadcastMessage(String message) {
        for (ClientHandler clientHandler : clients) {
            try {
                if (clientHandler.getId() != id) {
                    // Tu wysyłamy wiadomosć do innych klientów
                    clientHandler.writer.write(message);
                    clientHandler.writer.newLine();
                    clientHandler.writer.flush();
                }
            } catch (IOException e) {

                closeStreams(socket, writer, reader);
            }
        }
    }

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
    public void removeClient() {
        clients.remove(this);

        if (clients.size() > 0){
            for (ClientHandler clientHandler: clients) {

                broadcastMessage("SERVER: " + name + " passed!.");
            }
        }
    }

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
