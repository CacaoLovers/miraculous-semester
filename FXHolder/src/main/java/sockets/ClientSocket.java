package sockets;

import lombok.Data;

import java.io.IOException;
import java.net.Socket;

@Data
public class ClientSocket {

    private String url;
    private int port;
    private Socket socket;

    public ClientSocket(String url, int port){
        try {
            this.port = port;
            this.url = url;
            this.socket = new Socket(url, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
