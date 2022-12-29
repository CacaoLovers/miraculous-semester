package controllers;

import entity.Player;
import lombok.Data;
import protocol.packets.MovePacket;

import java.util.HashMap;

@Data
public class MoveEnemy {
    private Player player;

    public MoveEnemy(Player player) {
        this.player= player;
    }

    public void move(MovePacket packet) {
        byte direction = packet.getDirection();
        if(direction == 0) {
            player.getBody().setY(player.getBody().getY() - 7);
        }
        else if(direction == 1) {
            player.getBody().setX(player.getBody().getX() + 7);
        }
        else if(direction == 2) {
            player.getBody().setY(player.getBody().getY() + 7);
        }
        else if(direction == 3) {
            player.getBody().setX(player.getBody().getX() - 7);
        }
        else {
            throw new IllegalArgumentException("Wrong direction code");
        }
    }

}
