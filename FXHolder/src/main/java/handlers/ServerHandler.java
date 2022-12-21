package handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerHandler implements Runnable{
    private Integer port;
    private ServerSocket serverSocket;
    //private ArrayList<ClientHandler> clientSocket;
    private Socket clientSocket;
    private ThreadPoolExecutor serverPool;
    private InputStream inputStream;
    private OutputStream outputStream;

    private static int countClient = 0;

    private ServerHandler() {}

    public static ServerHandler create(Integer port, ThreadPoolExecutor serverPool){
        try {
            ServerHandler server = new ServerHandler();
            server.port = port;
            server.serverSocket = new ServerSocket(port);
            server.serverPool = serverPool;
            return server;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(){

        System.out.println("Сервер успешно запущен на порту " + this.port);
        System.out.println("Ожидание подключения игроков..");

    }

    public Integer getPort() {
        return port;
    }

    @Override
    public void run() {
        try {
            clientSocket = serverSocket.accept();
            System.out.println("Клиент - " + ++countClient + " подключился");
            outputStream = clientSocket.getOutputStream();
            inputStream = clientSocket.getInputStream();

            while (true){

                System.out.println(inputStream.read());

                outputStream.write(null);
                outputStream.flush();
                serverPool.notifyAll();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
