package view;

import controllers.MovePlayer;
import entity.Field;
import entity.FieldCell;
import entity.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;


public class ClientView extends Application {

    private Field field;
    private Pane fieldPane;

    @Override
    public void start(Stage stage) throws IOException {

        field = new Field();
        createGround();
        createBorder();
        createWall();


        Player player = new Player(new Rectangle(120, 120, 80, 80), new Image("cat.png"));

        Rectangle collisionPlayer = new Rectangle(player.getBody().getWidth(), player.getBody().getHeight());



        fieldPane = new Pane();

        collisionPlayer.setOpacity(0.0);


        for(FieldCell rectangleHandler: field.getFieldCells()){
            fieldPane.getChildren().add(rectangleHandler.getRectangle());
        }

        fieldPane.getChildren().add(player.getBody());
        fieldPane.getChildren().add(collisionPlayer);

        Scene scene = new Scene(fieldPane);
        MovePlayer movePlayer = new MovePlayer(player, collisionPlayer, this);
        scene.setOnKeyPressed(movePlayer);
        stage.setScene(scene);
        stage.show();
    }


    public void createWall(){
        FieldCell wall1 = new FieldCell(new Rectangle(100,100), new Image("brick.png"), 2, 200, 100);
        field.getFieldCells().add(wall1);
    }

    public void createBorder(){
        FieldCell border1 = new FieldCell(new Rectangle(100,100), new Image("stone.png"), 2, 0, 0);
        FieldCell border2 = new FieldCell(new Rectangle(100,100), new Image("stone.png"), 2, 100, 0);
        FieldCell border3 = new FieldCell(new Rectangle(100,100), new Image("stone.png"), 2, 0, 100);
        FieldCell border4 = new FieldCell(new Rectangle(100,100), new Image("stone.png"), 2, 200, 0);
        FieldCell border5 = new FieldCell(new Rectangle(100,100), new Image("stone.png"), 2, 200, 0);

        field.getFieldCells().add(border1);
        field.getFieldCells().add(border2);
        field.getFieldCells().add(border3);
        field.getFieldCells().add(border4);
        field.getFieldCells().add(border5);
    }

    public void createGround(){
        FieldCell ground1 = new FieldCell(new Rectangle(100,100), new Image("grass.png"), 1, 100, 100);
        FieldCell ground2 = new FieldCell(new Rectangle(100,100), new Image("grass.png"), 1, 0, 0);
        FieldCell ground3 = new FieldCell(new Rectangle(100,100), new Image("grass.png"), 1, 0, 100);
        FieldCell ground4 = new FieldCell(new Rectangle(100,100), new Image("grass.png"), 1, 100, 0);
        FieldCell ground5 = new FieldCell(new Rectangle(100,100), new Image("grass.png"), 1, 200, 0);
        FieldCell ground6 = new FieldCell(new Rectangle(100,100), new Image("grass.png"), 1, 200, 100);
        FieldCell ground7 = new FieldCell(new Rectangle(100,100), new Image("grass.png"), 1, 0, 200);
        FieldCell ground8 = new FieldCell(new Rectangle(100,100), new Image("grass.png"), 1, 100, 200);
        FieldCell ground9 = new FieldCell(new Rectangle(100,100), new Image("grass.png"), 1, 200, 200);
        field.getFieldCells().add(ground1);
        field.getFieldCells().add(ground2);
        field.getFieldCells().add(ground3);
        field.getFieldCells().add(ground4);
        field.getFieldCells().add(ground5);
        field.getFieldCells().add(ground6);
        field.getFieldCells().add(ground7);
        field.getFieldCells().add(ground8);
        field.getFieldCells().add(ground9);
    }

    public Field getField() {
        return field;
    }

    public static void showView(){
        launch();
    }

}