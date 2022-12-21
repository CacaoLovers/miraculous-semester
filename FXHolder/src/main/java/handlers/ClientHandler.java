package handlers;

import view.ClientView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler{
    private static int port;
    private Socket clientSocket;
    private  String url;

    private InputStream in;
    private OutputStream out;

    public ClientHandler(int port, String url){

        try{

            clientSocket = new Socket(url, port);
            this.port = port;
            this.url = url;
            in = clientSocket.getInputStream();
            out = clientSocket.getOutputStream();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(){
        try {
        ClientView.showView();

        out.write(new byte[]{3, 5, 6});
        out.flush();


            while (true){

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
