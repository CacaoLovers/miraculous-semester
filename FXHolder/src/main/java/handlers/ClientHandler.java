package handlers;

import exceptions.SocketConnectionError;
import protocol.packets.BombPacket;
import protocol.packets.Packet;
import sockets.ClientSocket;
import view.ClientView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler{

    private ClientSocket clientSocket;
    private byte clientId;
    private int[][] coordsForSpawn = new int[][] {
            {80, 80}, {300, 300}
    };


    public ClientHandler(int port, String url) {
        //try {
            clientSocket = new ClientSocket(url, port);
            //clientId = clientSocket.getSocket().getInputStream().read();

        /*} catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    public void start(){
        new ClientView().showView(coordsForSpawn[clientId]);
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
