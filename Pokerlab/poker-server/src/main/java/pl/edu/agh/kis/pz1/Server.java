package pl.edu.agh.kis.pz1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static final int PORT = 5555;
    public final String IP_ADDRESS= "127.0.0.0";

    public ServerSocket getServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }

    public static void main(String [] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Waiting For players...");

            /*while (true) {


                Socket clientSocket = serverSocket.accept();
                Thread thread = new Thread(){
                    public void run(){
                        try {
                            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                            Scanner in = new Scanner(clientSocket.getInputStream());
                            while (in.hasNextLine()) {
                                String input = in.nextLine();
                                if (input.equalsIgnoreCase("exit")) {
                                    break;
                                }
                                System.out.println("Received radius from client: " + input);

                                double radius = Double.parseDouble(input);
                                double area = Math.PI* radius *radius ;
                                out.println(area);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
                }*/
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }


