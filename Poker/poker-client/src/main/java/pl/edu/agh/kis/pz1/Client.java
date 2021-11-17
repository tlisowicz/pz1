package pl.edu.agh.kis.pz1;


import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String name;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                String serverMessage;

                while (socket.isConnected()) {
                    try {
                        serverMessage = reader.readLine();
                        System.out.println(serverMessage);
                    } catch (IOException e) {

                        closeStreams(socket, reader, writer);
                    }
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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();

        try {
            Socket socket = new Socket("localhost", Server.PORT);
            Client client = new Client(socket, name);

            client.listenForMessage();
            client.sendMessage();

        } catch (ConnectException e) {
            System.out.println("Server is down or the game has already started. Please try again later.");
        }
    }
}
