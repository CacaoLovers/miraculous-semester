package handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.BlockField;
import entity.Field;
import entity.Player;
import protocol.packets.HandshakePacket;
import protocol.packets.Packet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerHandler implements Runnable{

    private static final String PATH_GROUND_SPRITE = "json/ground.json";
    private static final String PATH_WALL_SPRITE = "json/wall.json";
    private static final String PATH_BORDER_SPRITE = "json/border.json";
    private static final String PATH_PLAYER_IMAGE = "images/cat.png";
    private static final int MAX_PLAYERS = 2;
    private Integer port;
    private ServerSocket serverSocket;
    private static ArrayList<Socket> sockets = new ArrayList<>();
    private static ArrayList<Player> players = new ArrayList<>();
    private static ArrayList<ClientSocket> clientSockets = new ArrayList<>();

    private Socket clientSocket;
    private ThreadPoolExecutor serverPool;

    private static int countClient = 0;

    private InputStream in;
    private OutputStream out;

    private Field field = new Field();
    private static ArrayList<BlockField> fieldCells = new ArrayList<>();

    private static ObjectMapper mapper = new ObjectMapper();




    private ServerHandler() {}

    public static ServerHandler create(Integer port, ThreadPoolExecutor serverPool){
        try {
            ServerHandler server = new ServerHandler();
            server.port = port;
            server.serverSocket = new ServerSocket(port);
            server.serverPool = serverPool;
            fieldCells.addAll(createFieldCell(PATH_GROUND_SPRITE));
            fieldCells.addAll(createFieldCell(PATH_BORDER_SPRITE));
            fieldCells.addAll(createFieldCell(PATH_WALL_SPRITE));
            return server;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<BlockField> createFieldCell(String type){
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(type)){

            return Arrays.asList(mapper.readValue(in, BlockField[].class));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            Socket clientSocket;
            int socketId = 0;

            while(sockets.size() != MAX_PLAYERS){
                clientSocket = serverSocket.accept();
                if (!sockets.contains(clientSocket)){
                    sockets.add(clientSocket);
                    clientSockets.add(new ClientSocket(clientSocket, this));
                    socketId = sockets.lastIndexOf(clientSocket);
                }
                in = new BufferedInputStream(clientSocket.getInputStream());
                out = clientSocket.getOutputStream();
                System.out.println("Игрок #" + socketId + " подключился");
                out.write(new HandshakePacket((byte) socketId).toByteArray());
                out.flush();
            }

            System.out.println("Игра начинается");

            for(ClientSocket client: clientSockets){
                client.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void sendMessageWithoutClient(Socket socket, byte[] message)  {
        for(Socket socketGetter: sockets) {
            if(socketGetter != socket) {
                try {
                    OutputStream clientOutput = socketGetter.getOutputStream();
                    clientOutput.write(message);
                    clientOutput.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void sendMessageToAllClients(byte[] message) throws IOException {
        for(Socket socketGetter: sockets) {
            OutputStream clientOutput = null;
            try {
                clientOutput = socketGetter.getOutputStream();
                clientOutput.write(message);
                clientOutput.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



    private byte[] readInput(InputStream stream) throws IOException {
        int b;
        byte[] buffer = new byte[10];
        int counter = 0;
        while ((b = stream.read()) > -1) {
            buffer[counter++] = (byte) b;
            if (counter >= buffer.length) {
                buffer = extendArray(buffer);
            }
            if (counter > 1 && Packet.isEndOfPacket(buffer, counter )) {
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
