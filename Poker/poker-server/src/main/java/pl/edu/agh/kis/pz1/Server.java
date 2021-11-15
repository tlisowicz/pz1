package pl.edu.agh.kis.pz1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static final int PORT = 5555;
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void serverStart(){
        try{

            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("A player has jointed the game.");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread clientThread = new Thread(clientHandler);
                clientThread.start();


            }
        } catch (IOException e) {

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

    public static void main(String [] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(PORT);
        Server server = new Server(serverSocket);
        System.out.println("SERVER STARTED.\n Waiting for players...");
        server.serverStart();
    }

    }


