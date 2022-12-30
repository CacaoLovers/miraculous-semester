package controllers;

import entity.FieldCell;
import entity.Player;
import handlers.ClientHandler;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import lombok.Data;
import protocol.packets.BombPacket;
import view.ClientView;


@Data
public class PlaceBomb implements EventHandler<KeyEvent> {

    private Rectangle collisionRectangle;
    private Player player;
    private ClientHandler clientHandler;
    private ClientView application;

    private static boolean bombHasPlant = false;

    private byte clientId;

    @Override
    public void handle(KeyEvent keyEvent) {
        if (application.isGaming()) {
            if (keyEvent.getCode() == KeyCode.SPACE) {
                System.out.println(bombHasPlant);
                if (!bombHasPlant) {
                    System.out.println("Bomb place");
                    bombHasPlant = true;
                    clientHandler.sendPacket(new BombPacket(clientId));
                    try {
                        Thread.sleep(75);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    application.placeBomb(checkCollision(collisionRectangle));
                }
            }
        } else {
            System.out.println("Игра еще не началась");
        }
    }


    public int[] checkCollision(Rectangle body){
        for (FieldCell fieldCell: application.getField().getFieldCells()){
            if (fieldCell.getType() == 1) {
                if (body.getBoundsInParent().intersects(fieldCell.getRectangle().getBoundsInParent())){
                    return new int[] {fieldCell.getPosX(), fieldCell.getPosY()};
                }
            }
        }
        return null;
    }

    public static void setBombHasPlant(boolean bombHasPlant) {
        PlaceBomb.bombHasPlant = bombHasPlant;
    }

    public PlaceBomb(Player player, Rectangle collisionRectangle, ClientView application, ClientHandler clientHandler, byte clientId) {
        this.player = player;
        this.collisionRectangle = collisionRectangle;
        this.application = application;
        this.clientHandler = clientHandler;
        this.clientId = clientId;
    }
}
