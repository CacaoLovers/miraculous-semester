package entity;

import javafx.beans.NamedArg;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


public class FieldCell {
    private Rectangle rectangle;
    private Image image;
    private Integer type;
    private Integer posX;
    private Integer posY;

    public FieldCell(Rectangle rectangle, Image image, Integer type, Integer posX, Integer posY) {
        this.rectangle = rectangle;
        this.image = image;
        this.type = type;
        this.posX = posX;
        this.posY = posY;
        rectangle.setFill(new ImagePattern(image));
        rectangle.setX(posX);
        rectangle.setY(posY);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Integer getType() {
        return type;
    }
}
