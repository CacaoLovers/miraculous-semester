package handlers;

import handlers.sockets.ClientSocket;
import protocol.packets.HandshakePacket;
import protocol.packets.MovePacket;
import protocol.packets.Packet;
import services.GameActionService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerHandler implements Runnable{
    private static final int MAX_PLAYERS = 2;
    private Integer port;
    private ServerSocket serverSocket;
    private ArrayList<Socket> socketList = new ArrayList<>();

    private Socket clientSocket;
    private ThreadPoolExecutor serverPool;
    private InputStream inputStream;
    private OutputStream outputStream;

    private static int countClient = 0;
    private GameActionService gameActionService;




    private ServerHandler() {}

    public static ServerHandler create(Integer port, ThreadPoolExecutor serverPool){
        try {
            ServerHandler server = new ServerHandler();
            server.port = port;
            server.serverSocket = new ServerSocket(port);
            server.serverPool = serverPool;
            server.gameActionService = new GameActionService(server);
            return server;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            if(socketList.size() <= MAX_PLAYERS) {
                clientSocket = serverSocket.accept();
            } else {
                return;
            }

            while (!serverSocket.isClosed()){

                if(!socketList.contains(clientSocket) && socketList.size() <= MAX_PLAYERS){
                    socketList.add(clientSocket);
                }

                connectionClient();


                    sendMessage(clientSocket, new HandshakePacket((byte) 1).toByteArray());

                    byte[] dataFromClient = readInput(inputStream);

                    if (dataFromClient.length < 5) {
                        return;
                    }

                    switch (dataFromClient[2]) {
                        case 2:
                            gameActionService.movePlayer(MovePacket.fromByteArray(dataFromClient));
                    }


                /*outputStream.write(null);
                outputStream.flush();
                serverPool.notifyAll();*/
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void connectionClient(){
        try {
            System.out.println("Клиент - " + countClient + " подключился");
            outputStream = clientSocket.getOutputStream();
            inputStream = clientSocket.getInputStream();
            HandshakePacket handshakePacket = new HandshakePacket((byte)countClient++);
            outputStream.write(handshakePacket.toByteArray());
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(Socket socket, byte[] message) throws IOException {
        socket.getOutputStream().write(message);
        socket.getOutputStream().flush();
    }

    public void sendMessageToAllClients(byte[] message) throws IOException {
        for(Socket socket: socketList){
            sendMessage(socket, message);
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
