package handlers;

import lombok.Data;
import protocol.packets.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;

@Data
public class ClientSocket extends Thread{

    private Socket socket;
    private ServerHandler serverHandler;

    public ClientSocket(Socket socket, ServerHandler serverHandler) {
        this.socket = socket;
        this.serverHandler = serverHandler;
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                if (socket.getInputStream().available() != 0) {
                    byte[] dataFromClient = readInput(socket.getInputStream());
                    System.out.println(Arrays.toString(dataFromClient));

                    serverHandler.sendMessageWithoutClient(socket, dataFromClient);
                }
                Thread.sleep(10);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] readInput(InputStream stream) throws IOException {
        int inByte;
        byte[] buffer = new byte[10];
        int counter = 0;
        while ((inByte = stream.read()) > -1) {
            buffer[counter++] = (byte) inByte;
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
