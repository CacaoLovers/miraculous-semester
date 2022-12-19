package executors;

import handlers.ServerHandler;

public class ServerExecute {
    public static void main(String[] args) {
        ServerHandler server = ServerHandler.create(5656, null);
        server.start();
    }
}
