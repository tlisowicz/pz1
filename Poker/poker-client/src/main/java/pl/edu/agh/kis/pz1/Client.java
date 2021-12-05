package pl.edu.agh.kis.pz1;


import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * class representing client
 */
public class Client {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String name;
    private static final Logger logger = Logger.getLogger(Client.class.getName());

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

    /**
     * Logger to display important messages to client
     * @return Logger
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Takes input from player and writes it to the buffer, which will be read out by clientHandler
     */
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

    /**
     * Creates a new thread to read incoming messages independently of other clients
     */
    public void listenForMessage() {
        new Thread(() -> {
            String serverMessage;

            while (socket.isConnected()) {
                try {
                    serverMessage = reader.readLine();
                    System.out.println(serverMessage);
                } catch (IOException e) {

                    closeStreams(socket, reader, writer);
                }
            }

        }).start();
    }

    /**
     * Closes streams. Called when error occurs
     * @param socket client's socket
     * @param reader client's buffer reader
     * @param writer client's buffer writer
     */
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

            Socket socket = new Socket("localhost", Server.PORT);
            String name;
            while (true) {

                Client.logger.log(Level.INFO,"Enter your name: ");
                name = scanner.nextLine();
                if (name.equals("")) {
                    Client.logger.log(Level.INFO,"Incorrect name try again");
                }
                else {
                    break;
                }
            }
            Client client = new Client(socket, name);

            client.listenForMessage();
            client.sendMessage();

        } catch (ConnectException e) {
            Client.logger.log(Level.INFO,"Server is down. Please try again later.");
        }
    }
}
