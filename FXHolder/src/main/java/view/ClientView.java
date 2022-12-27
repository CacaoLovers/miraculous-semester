package view;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.MovePlayer;
import entity.Field;
import entity.FieldCell;
import entity.Player;
import handlers.ClientHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Data
public class ClientView extends Application {

    private static final String PATH_GROUND_SPRITE = "json/ground.json";
    private static final String PATH_WALL_SPRITE = "json/wall.json";
    private static final String PATH_BORDER_SPRITE = "json/border.json";
    private static final String PATH_PLAYER_IMAGE = "images/cat.png";

    private Field field;
    private MovePlayer movePlayer;
    private Pane fieldPane;
    private ObjectMapper mapper = new ObjectMapper();
    private static int[] coordsSpawnPosition = new int[2];
    private static ClientHandler clientHandler;

    @Override
    public void start(Stage stage) throws IOException {
        field = new Field();

        field.getFieldCells().addAll(createFieldCell(PATH_GROUND_SPRITE));
        field.getFieldCells().addAll(createFieldCell(PATH_BORDER_SPRITE));
        field.getFieldCells().addAll(createFieldCell(PATH_WALL_SPRITE));

        fieldPane = new Pane();
        for(FieldCell rectangleHandler: field.getFieldCells()){
            fieldPane.getChildren().add(rectangleHandler.getRectangle());
        }

        createPlayer();

        Scene scene = new Scene(fieldPane);
        scene.setOnKeyPressed(movePlayer);
        stage.setScene(scene);
        stage.show();

        waitingDataFromServer();
    }

    public List<FieldCell> createFieldCell(String type){
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(type)){

            return Arrays.asList(mapper.readValue(in, FieldCell[].class));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createPlayer(){
        Player player = new Player(new Rectangle(coordsSpawnPosition[0], coordsSpawnPosition[1], 80, 80), new Image(PATH_PLAYER_IMAGE));

        Rectangle collisionPlayer = new Rectangle(player.getBody().getWidth(), player.getBody().getHeight());
        collisionPlayer.setOpacity(0.0);

        fieldPane.getChildren().add(player.getBody());
        fieldPane.getChildren().add(collisionPlayer);

        movePlayer = new MovePlayer(player, collisionPlayer, this);
    }

    public void showView(int[] coordsForSpawn, ClientHandler clientHandler){
        coordsSpawnPosition = coordsForSpawn;
        launch();
    }

    public void waitingDataFromServer(){
        new Thread(() -> {
            while(true){
                System.out.println("Жду");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

}