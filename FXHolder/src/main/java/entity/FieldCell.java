package entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.beans.NamedArg;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import lombok.*;

@Data

public class FieldCell {

    private String image;
    private Integer type;
    private Integer posX;
    private Integer posY;
    @JsonIgnore
    private Rectangle rectangle;

    private byte blockId;


    @JsonCreator
    public FieldCell(@JsonProperty("image") String pathImage,
                     @JsonProperty("type") Integer type,
                     @JsonProperty("posX") Integer posX,
                     @JsonProperty("posY") Integer posY,
                     @JsonProperty("id") byte blockId){
        this.image = pathImage;
        this.type = type;
        this.posX = posX;
        this.posY = posY;
        this.blockId = blockId;
        if(this.type != 1) {
            this.rectangle = new Rectangle(49,49);
        }
        else {
            this.rectangle = new Rectangle(50, 50);
        }
        rectangle.setFill(new ImagePattern(new Image(pathImage)));
        rectangle.setX(posX);
        rectangle.setY(posY);
    }


}
