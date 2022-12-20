package entity;


import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Player {
    private Rectangle body;
    private Image image;

    public Player(Rectangle rectangle, Image image){
        body = rectangle;
        this.image = image;
        rectangle.setFill(new ImagePattern(image));
    }

    public Rectangle getBody() {
        return body;
    }
}
