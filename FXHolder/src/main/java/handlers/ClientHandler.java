package handlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler{
    private static int port;
    private Socket clientSocket;
    private  String url;

    private Scanner in;
    private PrintWriter out;

    public ClientHandler(int port, String url){
        try{
            clientSocket = new Socket(url, port);
            this.port = port;
            this.url = url;
            in = new Scanner(clientSocket.getInputStream());
            out = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (true){

                    }
                } catch (Exception e){

                }
            }
        });
    }
}
