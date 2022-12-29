package view;

import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.EnemyPlaceBomb;
import controllers.MoveEnemy;
import controllers.MovePlayer;
import controllers.PlaceBomb;
import entity.Field;
import entity.FieldCell;
import entity.Player;
import handlers.ClientHandler;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Data;
import org.w3c.dom.css.Rect;
import protocol.PacketTypes;
import protocol.packets.GameOverPacket;
import protocol.packets.MovePacket;
import protocol.packets.Packet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Data
public class ClientView extends Application {

    private static final String PATH_GROUND_SPRITE = "json/ground.json";
    private static final String PATH_WALL_SPRITE = "json/wall.json";
    private static final String PATH_BORDER_SPRITE = "json/border.json";
    private static final String PATH_PLAYER_IMAGE = "images/cat.png";
    private static final String PATH_BOMB_IMAGE = "images/bomb.png";

    private static final String PATH_EXPLOSION_IMAGE = "images/explosion.png";

    private Field field;
    private MovePlayer movePlayer;
    private PlaceBomb placeBomb;
    private Pane fieldPane;
    private ObjectMapper mapper = new ObjectMapper();
    private static int[][] coordsSpawnPosition = new int[2][2];

    private static ClientHandler clientHandler;

    private byte clientId;
    private byte enemyId;

    private static Player client;
    private static Player enemy;

    private static boolean isGaming = false;
    private static Stage stage;
    private static Scene scene;


    public boolean isGaming(){
        return isGaming;
    }

    public void setGaming(boolean gaming){
        isGaming = gaming;
    }

    public static Player getClient() {
        return client;
    }

    @Override
    public void start(Stage stage) throws IOException {
        field = new Field();
        this.stage = stage;
        isGaming = false;
        field.getFieldCells().addAll(createFieldCell(PATH_GROUND_SPRITE));
        field.getFieldCells().addAll(createFieldCell(PATH_BORDER_SPRITE));
        field.getFieldCells().addAll(createFieldCell(PATH_WALL_SPRITE));

        fieldPane = new Pane();
        for (FieldCell rectangleHandler : field.getFieldCells()) {
            fieldPane.getChildren().add(rectangleHandler.getRectangle());
        }

        createPlayer();
        createEnemy();

        scene = new Scene(fieldPane);
        scene.setOnKeyPressed(movePlayer);
        scene.setOnKeyReleased(placeBomb);

        stage.setScene(scene);
        stage.show();

        handle();

    }

    public List<FieldCell> createFieldCell(String type) {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(type)) {

            return Arrays.asList(mapper.readValue(in, FieldCell[].class));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void createPlayer() {

        client = Player.builder()
                .body(new Rectangle(coordsSpawnPosition[0][0], coordsSpawnPosition[0][1], 35, 35))
                .image(new Image(PATH_PLAYER_IMAGE))
                .build();

        client.getBody().setFill(new ImagePattern(client.getImage()));

        Rectangle collisionPlayer = new Rectangle(client.getBody().getWidth(), client.getBody().getHeight());
        collisionPlayer.setOpacity(0.0);

        fieldPane.getChildren().add(client.getBody());
        fieldPane.getChildren().add(collisionPlayer);

        movePlayer = new MovePlayer(client, collisionPlayer, this, clientHandler, clientId);
        placeBomb = new PlaceBomb(client, collisionPlayer, this, clientHandler, clientId);

    }

    public void createEnemy() {

        enemy = Player.builder()
                .body(new Rectangle(coordsSpawnPosition[1][0], coordsSpawnPosition[1][1], 35, 35))
                .image(new Image(PATH_PLAYER_IMAGE))
                .build();
        enemy.getBody().setFill(new ImagePattern(enemy.getImage()));

        fieldPane.getChildren().add(enemy.getBody());
    }

    public void showView(int[][] coordsForSpawn, ClientHandler client, byte clientId) {
        clientHandler = client;
        coordsSpawnPosition = coordsForSpawn;
        this.clientId = clientId;
        launch();
    }


    public Player getEnemy() {
        return enemy;
    }

    public void placeBomb(int[] coords) {
        new Thread(() -> {
            Rectangle bomb = new Rectangle(coords[0] + 10, coords[1] + 10, 45, 45);
            bomb.setFill(new ImagePattern(new Image(PATH_BOMB_IMAGE)));

            List<Rectangle> explosions = new ArrayList<>();
            explosions.add(new Rectangle(coords[0], coords[1], 45, 45));
            explosions.add(new Rectangle(coords[0] + 50, coords[1], 45, 45));
            explosions.add(new Rectangle(coords[0] - 50, coords[1], 45, 45));
            explosions.add(new Rectangle(coords[0], coords[1] + 50, 45, 45));
            explosions.add(new Rectangle(coords[0], coords[1] - 50, 45, 45));

            for(Rectangle explosion: explosions) {
                explosion.setFill(new ImagePattern(new Image(PATH_EXPLOSION_IMAGE)));
            }
            Platform.runLater(() -> {
                fieldPane.getChildren().add(bomb);
            });
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(() -> fieldPane.getChildren().remove(bomb));

            List<FieldCell> bricks = getField().getFieldCells()
                    .stream().filter(x -> x.getType() == 3).collect(Collectors.toList());

            for(Rectangle explosion: explosions) {
                Platform.runLater(() -> fieldPane.getChildren().add(explosion));
                if(explosion.getBoundsInParent().intersects(client.getBody().getBoundsInParent())) {
                    clientHandler.sendPacket(new GameOverPacket(GameOverPacket.ENEMY));
                }
                if(explosion.getBoundsInParent().intersects(enemy.getBody().getBoundsInParent())) {
                    clientHandler.sendPacket(new GameOverPacket(GameOverPacket.OWNER));
                }
                for(FieldCell cell: bricks) {
                    if(explosion.getBoundsInParent().intersects(cell.getRectangle().getBoundsInParent())) {
                        Platform.runLater(() -> {
                            fieldPane.getChildren().remove(cell.getRectangle());
                            getField().getFieldCells().remove(cell);
                        });
                    }
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(() -> {
                for(Rectangle explosion: explosions) {
                    fieldPane.getChildren().remove(explosion);
                }
            });
            PlaceBomb.setBombHasPlant(false);
        }).start();
    }

    public void handle() {
        new Thread(() -> {
            while (true) {
                try {
                    if (clientHandler.getInputStream().available() != 0) {
                        byte[] dataServer = clientHandler.readInput(clientHandler.getInputStream());
                        switch (dataServer[2]) {
                            case 2: {
                                MoveEnemy enemy = new MoveEnemy(getEnemy());
                                enemy.move(MovePacket.fromByteArray(dataServer));
                                break;
                            }
                            case 3: {
                                Rectangle enemy = getEnemy().getBody();
                                EnemyPlaceBomb enemyPlaceBomb = new EnemyPlaceBomb(this, enemy);
                                placeBomb(enemyPlaceBomb.checkCollision());
                                break;
                            }
                            case 5: {
                                GameOverPacket gameOverPacket = GameOverPacket.fromByteArray(dataServer);
                                if(gameOverPacket.getWinnerId() == GameOverPacket.OWNER) {
                                    System.out.println("You are loser");
                                    Platform.runLater(() -> {
                                        Text text = new Text("Вы проиграли");
                                        text.setFill(Color.RED);
                                        text.setStyle("-fx-font: 40 arial;");
                                        StackPane stackPane = new StackPane();
                                        stackPane.getChildren().add(text);
                                        scene.setRoot(stackPane);
                                        clientHandler.gameOver();
                                    });
                                    isGaming = false;
                                    return;
                                }
                                if(gameOverPacket.getWinnerId() == GameOverPacket.ENEMY) {
                                    System.out.println("You are winner");
                                    Platform.runLater(() -> {
                                        Text text = new Text("Вы выйграли");
                                        text.setFill(Color.GREEN);
                                        text.setStyle("-fx-font: 40 arial;");
                                        StackPane stackPane = new StackPane();
                                        stackPane.getChildren().add(text);
                                        scene.setRoot(stackPane);
                                        clientHandler.gameOver();
                                    });
                                    isGaming = false;
                                    return;
                                }
                                break;
                            }
                        }
                    }

                    Thread.sleep(20);

                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}

