package handlers;

import protocol.packets.Packet;
import handlers.sockets.ClientSocket;
import view.ClientView;

import java.io.IOException;
import java.io.InputStream;

public class ClientHandler{

    private int MAX_PLAYERS = 2;
    private ClientSocket clientSocket;
    private byte clientId;

    private ClientView clientView;
    private int[][] coordsForSpawn = new int[][] {
            {120, 120}, {300, 300}
    };


    public ClientHandler(int port, String url) {
        try {
            this.clientSocket = new ClientSocket(url, port);
            clientId = readInput(clientSocket.getSocket().getInputStream())[3];
            System.out.println("Вы успешно подключились к серверу с id " + clientId);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        try {

            System.out.println("Ожидание игроков");
            readInput(clientSocket.getSocket().getInputStream());

            this.clientView = new ClientView();
            clientView.showView(coordsForSpawn[clientId]);

            while (!clientSocket.getSocket().isClosed()) {

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
