package handlers;

import lombok.Data;
import protocol.PacketTypes;
import protocol.packets.HandshakePacket;
import protocol.packets.Packet;
import view.ClientView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


@Data
public class ClientHandler{

    private int MAX_PLAYERS = 2;
    private Socket clientSocket;
    private byte clientId;

    private ClientView clientView;

    private InputStream inputStream;
    private OutputStream outputStream;
    private int[][] coordsForSpawn = new int[][] {
            {50, 100}, {450, 450}
    };


    public ClientHandler(int port, String url) {
        try {
            this.clientSocket = new Socket(url, port);
            this.inputStream = clientSocket.getInputStream();
            this.outputStream = clientSocket.getOutputStream();

            byte[] packet = readInput(inputStream);
            if(packet[2] != PacketTypes.HANDSHAKE) {
                throw new IllegalArgumentException("No way... Wrong type");
            }

            HandshakePacket handshakePacket = HandshakePacket.fromByteArray(packet);

            clientId = handshakePacket.getPlayerId();
            System.out.println("Вы успешно подключились к серверу с id " + clientId);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public void start() {
        clientView = new ClientView();
        if(clientId != 0) {
            coordsForSpawn = new int[][] {{450, 450}, {50, 100}};
        }
        clientView.showView(coordsForSpawn, this, clientId);
    }

    public void sendPacket(Packet packet){
        try {
            outputStream.write(packet.toByteArray());
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void gameOver() {
        try {
            System.out.println("Игра завершена");
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] readInput(InputStream stream) throws IOException {
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

    public byte[] extendArray(byte[] oldArray) {
        int oldSize = oldArray.length;
        byte[] newArray = new byte[oldSize * 2];
        System.arraycopy(oldArray, 0, newArray, 0, oldSize);
        return newArray;
    }


}
