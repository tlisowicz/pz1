package pl.edu.agh.kis.pz1;

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

    public int getId() {
        return id;
    }

    public BufferedReader getReader() {
        return reader;
    }

    @Override
    public void run() {
       /*String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = reader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e ) {

                closeStreams(socket, writer, reader);
                break;
            }

        }*/

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

    public void loopbackMessage(String message) {
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

        // TODO: Dodać metodę pass do gracza i ją wołać razem z tą poniżej
        broadcastMessage("SERVER: " + name + " passed!.");
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
