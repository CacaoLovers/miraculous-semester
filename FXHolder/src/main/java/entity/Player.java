package entity;


import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Builder
@Data
public class Player {

    private Rectangle body;
    private Image image;

    private Integer playerId;

    private Double posX;
    private Double posY;


}
