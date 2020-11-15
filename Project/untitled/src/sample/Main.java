package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group group = new Group();
        Scene scene = new Scene(group, 800, 800);

        ArrayList<Image> upWalkingImages = initializePersonGoingPics("up" , 9);

        Image startingImage = setStartingImage();
        ImageView imageView = new ImageView(startingImage);
        imageView.setX(200);
        imageView.setY(200);

        int movingSpeed = 2;

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (event.getCode().equals(KeyCode.UP)) {
                            personWalk(imageView , upWalkingImages , movingSpeed , "up");
                        }
                    }
                });
            }
        });

        group.getChildren().add(imageView);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Hello Back to javafx");
        primaryStage.show();

    }

    public Image setStartingImage(){
        File file = new File("C:\\Users\\lenovo\\Downloads\\Telegram Desktop\\" + "1.jpg");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(fileInputStream);
        return image;
    }

    public ArrayList<Image> initializePersonGoingPics(String direct , int numOfPics) throws FileNotFoundException {
        ArrayList<Image> images = new ArrayList<>(4);
        for(int i = 1 ; i <= numOfPics ; i++) {
            File file = new File("C:\\Users\\lenovo\\Downloads\\Telegram Desktop"
                    + "\\" + i + ".jpg");
            FileInputStream fileInputStream = new FileInputStream(file);
            Image image = new Image(fileInputStream);
            images.add(image);
        }
        return images;
    }

    public void personWalk(ImageView imageView , ArrayList<Image> images , int movingSpeed , String direct){
        switch (direct){
            case "up":
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0 ; i < images.size() ; i++){
                            synchronized (imageView) {
                                imageView.setImage(images.get(i));
                            }
                            try{
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        synchronized (imageView) {
                            imageView.setImage(images.get(0));
                        }
                    }
                }).start();
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                synchronized (imageView) {
                    imageView.setY(imageView.getY() - movingSpeed);
                }
                break;
        }
    }
}
