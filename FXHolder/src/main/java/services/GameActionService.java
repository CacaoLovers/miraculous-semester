package services;

import handlers.ServerHandler;
import protocol.packets.MovePacket;

public class GameActionService {
    private ServerHandler serverHandler;

    public GameActionService(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    public void movePlayer(MovePacket movePacket){

    }
}
