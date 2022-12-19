package controllers;

import entity.FieldCell;
import entity.Player;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import view.ClientView;

public class MovePlayer implements EventHandler<KeyEvent> {
    private Rectangle collisionRectangle;
    private Player player;
    private ClientView application;

    public MovePlayer(Player player, Rectangle collisionRectangle, ClientView application) {
        this.player = player;
        this.collisionRectangle = collisionRectangle;
        this.application = application;
    }

    @Override
    public void handle(KeyEvent keyEvent) {

        boolean onCollision = false;

        if(keyEvent.getCode() == KeyCode.RIGHT){
            collisionRectangle.setX(player.getBody().getX() + 7);
            if (!checkCollision(collisionRectangle)){
                onCollision = false;
                player.getBody().setX(player.getBody().getX() + 7);
            } else if(!onCollision){
                onCollision = true;
                collisionRectangle.setX(player.getBody().getX() - 7);
            }
        }
        if(keyEvent.getCode() == KeyCode.LEFT){
            collisionRectangle.setX(player.getBody().getX() - 7);
            if (!checkCollision(collisionRectangle)){
                player.getBody().setX(player.getBody().getX() - 7);
                onCollision = false;
            } else if(!onCollision){
                onCollision = true;
                collisionRectangle.setX(player.getBody().getX() + 7);
            }
        }
        if(keyEvent.getCode() == KeyCode.DOWN){
            collisionRectangle.setY(player.getBody().getY() + 7);
            if (!checkCollision(collisionRectangle)){
                player.getBody().setY(player.getBody().getY() + 7);
                onCollision = false;
            } else if(!onCollision){
                onCollision = true;
                collisionRectangle.setY(player.getBody().getY() - 7);
            }
        }
        if(keyEvent.getCode() == KeyCode.UP){
            collisionRectangle.setY(player.getBody().getY() - 7);
            if (!checkCollision(collisionRectangle)){
                player.getBody().setY(player.getBody().getY() - 7);
                onCollision = false;
            } else if(!onCollision){
                onCollision = true;
                collisionRectangle.setY(player.getBody().getY() + 7);
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
