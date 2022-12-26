package handlers;

import protocol.packets.Packet;

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
    private ArrayList<Socket> socketList = new ArrayList<>();

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

    @Override
    public void run() {
        try {
            clientSocket = serverSocket.accept();
            System.out.println("Клиент - " + countClient + " подключился");
            outputStream = clientSocket.getOutputStream();
            inputStream = clientSocket.getInputStream();
            outputStream.write(countClient++);
            outputStream.flush();

            while (!serverSocket.isClosed()){

                if(!socketList.contains(clientSocket)){
                    socketList.add(clientSocket);
                }

                System.out.println(readInput(clientSocket.getInputStream()));

                outputStream.write(null);
                outputStream.flush();
                serverPool.notifyAll();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    private byte[] readInput(InputStream stream) throws IOException {
        int b;
        byte[] buffer = new byte[10];
        int counter = 0;
        while ((b = stream.read()) > -1) {
            buffer[counter++] = (byte) b;
            System.out.println(buffer);
            if (counter >= buffer.length) {
                buffer = extendArray(buffer);
            }
            if (counter > 1 && Packet.isEndOfPacket(buffer, counter - 1)) {
                break;
            }
        }
        byte[] data = new byte[counter];
        System.arraycopy(buffer, 0, data, 0, counter);
        return data;
    }

    private byte[] extendArray(byte[] oldArray) {
        int oldSize = oldArray.length;
        byte[] newArray = new byte[oldSize * 2];
        System.arraycopy(oldArray, 0, newArray, 0, oldSize);
        return newArray;
    }



}
