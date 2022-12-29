package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import lombok.Data;

@Data
public class BlockField {

    private String image;
    private Integer type;
    private Integer posX;
    private Integer posY;

    private byte blockId;


    @JsonCreator
    public BlockField(@JsonProperty("image") String pathImage,
                     @JsonProperty("type") Integer type,
                     @JsonProperty("posX") Integer posX,
                     @JsonProperty("posY") Integer posY,
                     @JsonProperty("id") byte blockId){
        this.image = pathImage;
        this.type = type;
        this.posX = posX;
        this.posY = posY;
        this.blockId = blockId;
    }

}
