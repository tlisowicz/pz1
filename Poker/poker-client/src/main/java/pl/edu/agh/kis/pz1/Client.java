package pl.edu.agh.kis.pz1;


import pl.edu.agh.kis.pz1.Utils.Utils;
import pokerExceptions.MaxNumberOfPlayersException;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public class Client {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String name;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public Client(Socket socket, String name) {
        try {
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.socket = socket;
            this.name = name;

        } catch (IOException e) {
            closeStreams( socket, reader, writer);
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public void sendMessage() {
        try {
            writer.write(name);
            writer.newLine();
            writer.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                writer.write(messageToSend);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            closeStreams(socket, reader, writer);
        }
    }

    public void listenForMessage() {
        new Thread(() -> {
            String serverMessage;

            while (socket.isConnected()) {
                try {
                    serverMessage = reader.readLine();
                    if (serverMessage.equals("DISCONNECTED")) {
                        closeStreams(socket, reader, writer);
                    }
                    System.out.println(serverMessage);
                } catch (IOException e) {

                    closeStreams(socket, reader, writer);
                }
            }

        }).start();
    }

    private void closeStreams(Socket socket, BufferedReader reader, BufferedWriter writer) {
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

    public static void main(String [] args) throws IOException {

        try(Scanner scanner = new Scanner(System.in)) {

            //problem z poprawnym odczytem statycznej zmiennej gameStarted
            if (Utils.gameStarted) {
                throw new MaxNumberOfPlayersException();
            }
            Socket socket = new Socket("localhost", Server.PORT);
            String name;
            while (true) {

                System.out.println("Enter your name: ");
                name = scanner.nextLine();
                if (name.equals("")) {
                    System.out.println("Incorrect name try again");
                }
                else {
                    break;
                }
            }
            Client client = new Client(socket, name);

            client.listenForMessage();
            client.sendMessage();

        } catch (MaxNumberOfPlayersException e) {
            System.out.println("Game has already started. Please try again later.");

        } catch (ConnectException e) {
            System.out.println("Server is down. Please try again later.");
        }
    }
}
