package pl.edu.agh.kis.pz1;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 5555;

    public static void main(String [] args){

        System.out.println("Client started.");
        try {
            Socket socket = new Socket(HOST, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner in = new Scanner(socket.getInputStream());
            Scanner s = new Scanner(System.in);

            while (true) {
                System.out.print("Input: ");
                String input = s.nextLine();
                out.println(input);
                if (input.equalsIgnoreCase("exit")) break;
                System.out.println("Echoed from server: " + in.nextLine());
            }

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
