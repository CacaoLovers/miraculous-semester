package controllers;

import entity.FieldCell;
import entity.Player;
import handlers.ClientHandler;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import lombok.Data;
import protocol.packets.MovePacket;
import view.ClientView;

@Data
public class MovePlayer implements EventHandler<KeyEvent>{
    private Rectangle collisionRectangle;
    private Player player;
    private ClientHandler clientHandler;
    private ClientView application;

    private byte clientId;


    public MovePlayer(Player player, Rectangle collisionRectangle, ClientView application, ClientHandler clientHandler, byte clientId) {
        this.player = player;
        this.collisionRectangle = collisionRectangle;
        this.application = application;
        this.clientHandler = clientHandler;
        this.clientId = clientId;
    }
    @Override
    public void handle(KeyEvent keyEvent) {
        if(application.isGaming()) {
            if (keyEvent.getCode() == KeyCode.RIGHT) {
                collisionRectangle.setX(player.getBody().getX() + 7);
                if (!checkCollision(collisionRectangle)) {
                    clientHandler.sendPacket(new MovePacket(clientId, (byte) 1));
                    player.getBody().setX(player.getBody().getX() + 7);
                } else {
                    collisionRectangle.setX(player.getBody().getX() - 7);
                }
            }
            else if (keyEvent.getCode() == KeyCode.LEFT) {
                collisionRectangle.setX(player.getBody().getX() - 7);
                if (!checkCollision(collisionRectangle)) {
                    clientHandler.sendPacket(new MovePacket(application.getClientId(), (byte) 3));
                    player.getBody().setX(player.getBody().getX() - 7);
                } else {
                    collisionRectangle.setX(player.getBody().getX() + 7);
                }
            }
            else if (keyEvent.getCode() == KeyCode.DOWN) {
                collisionRectangle.setY(player.getBody().getY() + 7);
                if (!checkCollision(collisionRectangle)) {
                    clientHandler.sendPacket(new MovePacket(application.getClientId(), (byte) 2));
                    player.getBody().setY(player.getBody().getY() + 7);
                } else {
                    collisionRectangle.setY(player.getBody().getY() - 7);
                }
            }
            else if (keyEvent.getCode() == KeyCode.UP) {
                collisionRectangle.setY(player.getBody().getY() - 7);
                if (!checkCollision(collisionRectangle)) {
                    clientHandler.sendPacket(new MovePacket(application.getClientId(), (byte) 0));
                    player.getBody().setY(player.getBody().getY() - 7);
                } else {
                    collisionRectangle.setY(player.getBody().getY() + 7);
                }
            }
        } else {
            if(keyEvent.getCode() == KeyCode.ENTER) {
                System.out.println("Игра не началась");
                application.setGaming(true);
            }
        }
    }

    public boolean checkCollision(Rectangle rectanglePlayer){
        for (FieldCell fieldCell: application.getField().getFieldCells()){
            if (fieldCell.getType() == 2 || fieldCell.getType() == 3) {
                if (collisionRectangle.getBoundsInParent().intersects(fieldCell.getRectangle().getBoundsInParent())){
                    return true;
                }
            }
        }
        return false;
    }
}
