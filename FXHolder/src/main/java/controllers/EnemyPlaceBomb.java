package controllers;

import entity.FieldCell;
import javafx.scene.shape.Rectangle;
import view.ClientView;

public class EnemyPlaceBomb {
    private static ClientView application;
    private static Rectangle enemy;

    public EnemyPlaceBomb(ClientView application, Rectangle enemy) {
        this.application = application;
        this.enemy = enemy;
    }
    public int[] checkCollision() {
        for (FieldCell fieldCell: application.getField().getFieldCells()){
            if (fieldCell.getType() == 1) {
                if (enemy.getBoundsInParent().intersects(fieldCell.getRectangle().getBoundsInParent())){
                    return new int[] {fieldCell.getPosX(), fieldCell.getPosY()};
                }
            }
        }
        return null;
    }
}
