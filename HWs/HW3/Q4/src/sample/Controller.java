package sample;

import com.sun.imageio.spi.RAFImageInputStreamSpi;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import sun.swing.BakedArrayList;

import java.awt.*;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.*;


class PowerUp extends Circle{
    int velocity;
    boolean isPlayed;
    int n;
    int time;
    PowerUp(double centerX, double centerY, int velocity){
        super(centerX, centerY, 15);
        this.velocity = velocity/2;
    }

    public void move(){
        this.setCenterY(this.getCenterY() + velocity);
    }

    public void play(int n, int speed, Controller controller){
        if (isPlayed)
            return;
        isPlayed = true;
        this.n = n;
        this.effects(controller);
    }

    public void stop(int n, int speed, Controller controller){
        if (!isPlayed)
            return;
        if (n*speed - this.n*speed > time){
            isPlayed = false;
            this.reverseEffects(controller);
        }
    }

    public void effects(Controller controller){

    }
    public void reverseEffects(Controller controller){

    }
}

class LengthPowerUp extends PowerUp{

    LengthPowerUp(double centerX, double centerY, int velocity){
        super(centerX, centerY, velocity);
        this.setFill(Color.BLANCHEDALMOND);
        time = 15000;
    }

    public void effects(Controller controller){
        controller.playerBat.setWidth(controller.playerBat.getWidth()*1.5);
    }
    public void reverseEffects(Controller controller){
        controller.playerBat.setWidth(150);
    }
}

class BallSpeedDoublePowerUp extends PowerUp{

    BallSpeedDoublePowerUp(double centerX, double centerY, int velocity){
        super(centerX, centerY, velocity);
        this.setFill(Color.AZURE);
        time = 15000;
    }

    public void effects(Controller controller){
        Ball.xSpeed = Ball.xSpeed*2;
        Ball.ySpeed = Ball.ySpeed*2;
    }
    public void reverseEffects(){
        Ball.xSpeed /= 2;
        Ball.ySpeed /= 2;
    }
}

class BallSpeedHalfPowerUp extends PowerUp{

    BallSpeedHalfPowerUp(double centerX, double centerY, int velocity){
        super(centerX, centerY, velocity);
        this.setFill(Color.TAN);
        time = 15000;
    }

    public void effects(Controller controller){
        Ball.xSpeed = Ball.xSpeed/2;
        Ball.ySpeed = Ball.ySpeed/2;
        if (Ball.xSpeed < 1 && Ball.xSpeed > -1){
            Ball.xSpeed = 1;
            Ball.ySpeed = 1;
        }
    }
    public void reverseEffects(Controller controller){
        Ball.xSpeed *= 2;
        Ball.ySpeed *= 2;
    }
}

class BallTakhayoliPowerUp extends PowerUp{

    BallTakhayoliPowerUp(double centerX, double centerY, int velocity){
        super(centerX, centerY, velocity);
        this.setFill(Color.CRIMSON);
        time = 12000;
    }

    public void effects(Controller controller){
        controller.ball.isRadioActive = true;
    }
    public void reverseEffects(Controller controller){
        controller.ball.isRadioActive = false;
    }
}

class FireBallPowerUp extends PowerUp{

    FireBallPowerUp(double centerX, double centerY, int velocity){
        super(centerX, centerY, velocity);
        this.setFill(Color.FIREBRICK);
        time = 8000;
    }

    public void effects(Controller controller){
        controller.ball.setFill(Color.RED);
        controller.ball.isFired = true;
    }
    public void reverseEffects(Controller controller){
        controller.ball.setFill(Color.BLACK);
        controller.ball.isFired = false;
    }
}

class Brick extends Rectangle {
    static int brickWidth = 65 , brickHeight = 35;
    int necessaryTimes;
    int score;

    Brick(double x, double y, double width, double height) {
        super(x, y, width, height);

    }

}

class GellyBrick extends Brick{

    GellyBrick(double x, double y, double width, double height){
        super( x, y, width, height);
        necessaryTimes = 1;
        this.setFill(Color.ORANGE);
        score = 100;
    }
}

class WoodBrick extends Brick{

    WoodBrick(double x, double y, double width, double height){
        super( x, y, width, height);
        necessaryTimes = 2;
        this.setFill(Color.RED);
        score = 200;
    }
}

class IronBrick extends Brick{

    IronBrick(double x, double y, double width, double height){
        super( x, y, width, height);
        necessaryTimes = 3;
        this.setFill(Color.SILVER);
        score = 300;

    }
}

class TakhayoliBrick extends Brick{

    TakhayoliBrick(double x, double y, double width, double height){
        super( x, y, width, height);
        necessaryTimes = -1;
        this.setFill(Color.DARKSLATEBLUE);
        score = 500;

    }
}

class Ball extends Circle{
    static int xSpeed = -2000;
    static int ySpeed = -1000;
    boolean isFired = false;
    boolean isRadioActive = false;
    boolean isStopped;

    Ball(double centerX, double centerY, double radius, Paint fill){
        super(centerX, centerY, radius, fill);
    }

}

class Bat extends Rectangle{

    boolean isGameOver;
    int brickScore, timeScore = 5000;
    Bat(double x, double y, double width, double height){
        super(x, y, width, height);
    }
}

public class Controller implements Initializable{

    @FXML
    Pane pane;

    Timeline t;
    int speed = 20;
    int n = 1;
    Ball ball;
    Bat playerBat;
    ArrayList<Brick> bricks;
    ArrayList<PowerUp> powerUps = new ArrayList<>();

    @FXML
    Button slow;

    @FXML
    Button fast;

    @FXML
    Button veryFast;

    @FXML
    void slow(){
        Ball.xSpeed = -5;
        Ball.ySpeed = -5;
        start();
    }

    @FXML
    void fast(){
        Ball.xSpeed = -10;
        Ball.ySpeed = -10;
        start();
    }

    @FXML
    void veryFast(){
        Ball.xSpeed = -25;
        Ball.ySpeed = -25;
        start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        javafx.scene.image.Image image = new Image("file:D:\\University\\Courses\\Advanced Programming (Java)\\Tamarin\\Tamrin 3\\Q4\\aks.jpg");
        pane.setBackground(new Background(new BackgroundImage(image, null, null, null, null)));
    }

    @FXML
    void start(){
        pane.getChildren().clear();
        if (bricks != null)
            bricks.clear();
        if (powerUps != null)
            powerUps.clear();

        ball  = new Ball(400, 850, 5, Color.BLACK);
        playerBat = new Bat(325, 890, 150, 10);
        pane.getChildren().addAll(ball, playerBat);
        n = 1;
        playerBat.isGameOver = false;
        Ball.xSpeed = 5;
        Ball.ySpeed = 5;
        playerBat.brickScore = 0;
        playerBat.timeScore = 5000;

        bricks = new ArrayList<>();

        for (int k = 0; k < 12; k++){
            for (int i = 0; i < 4; i++){
                Random rnd = new Random();
                int type = rnd.nextInt(5);

                if (type == 0){
                }
                else if (type == 1){
                    Brick temp = new GellyBrick(140 + i * Brick.brickWidth, 100 + k * Brick.brickHeight, Brick.brickWidth,  Brick.brickHeight);
                    bricks.add(temp);
                    temp = new GellyBrick(140 + (7-i) * Brick.brickWidth, 100 + k * Brick.brickHeight, Brick.brickWidth,  Brick.brickHeight);
                    bricks.add(temp);
                }
                else if (type == 2){
                    Brick temp = new WoodBrick(140 + i * Brick.brickWidth, 100 + k * Brick.brickHeight, Brick.brickWidth,  Brick.brickHeight);
                    bricks.add(temp);
                    temp = new WoodBrick(140 + (7-i) * Brick.brickWidth, 100 + k * Brick.brickHeight, Brick.brickWidth,  Brick.brickHeight);
                    bricks.add(temp);
                }
                else if (type == 3){
                    Brick temp = new IronBrick(140 + i * Brick.brickWidth, 100 + k * Brick.brickHeight, Brick.brickWidth,  Brick.brickHeight);
                    bricks.add(temp);
                    temp = new IronBrick(140 + (7-i) * Brick.brickWidth, 100 + k * Brick.brickHeight, Brick.brickWidth,  Brick.brickHeight);
                    bricks.add(temp);
                }
                else if (type == 4){
                    Brick temp = new TakhayoliBrick(140 + i * Brick.brickWidth, 100 + k * Brick.brickHeight, Brick.brickWidth,  Brick.brickHeight);
                    bricks.add(temp);
                    temp = new TakhayoliBrick(140 + (7-i) * Brick.brickWidth, 100 + k * Brick.brickHeight, Brick.brickWidth,  Brick.brickHeight);
                    bricks.add(temp);
                }
            }
        }


        for (int i = 0; i < bricks.size(); i++) {
            pane.getChildren().add(bricks.get(i));
        }

        Button pause = new Button();
        Button exit = new Button();
        pause.setText("Pause");
        pause.setLayoutX(50);
        pause.setOnMouseClicked(e -> {
            pauseTheGame();
        });
        exit.setText("Exit");
        exit.setOnMouseClicked(e -> {
            exit();
        });

        pane.getChildren().addAll(exit, pause);

        EventHandler<KeyEvent> key = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.LEFT){
                    if (playerBat.getX() >= 80){
                        for (int i = 0; i < 10; i++){
                            playerBat.setX(playerBat.getX() - 8);
                        }
                    }
                }
                else if (event.getCode() == KeyCode.RIGHT){
                    if (playerBat.getX() + playerBat.getWidth() <= 720){
                        for (int i = 0; i < 10; i++){
                            playerBat.setX(playerBat.getX() + 8);
                        }
                    }
                }
            }
        };

        pane.setOnKeyPressed(key);

        Label score = new Label();
        score.setLayoutX(200);
        pane.getChildren().add(score);

        KeyFrame k = new KeyFrame(Duration.millis(speed), e ->{
            moveBall(score);
        });
        t = new Timeline(k);
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();


    }

    void moveBall(Label score) {
        int intersectedNumber = 0;
        if (ball.isStopped) {
            return;
        }
        if (playerBat.isGameOver){
            t.stop();
            return;
        }
        if (bricks.size() == 0){
            if (playerBat.isGameOver)
                return;
            playerBat.isGameOver = true;
            Label gameOver = new Label();
            gameOver.setText("You Got It Wineeeeeeeeeer !");
            gameOver.setLayoutY(400);
            gameOver.setFont(new Font(68));
            Label scoreShow = new Label();
            scoreShow.setText("Score = " + ((playerBat.timeScore-= n*speed/1000 * 20)+ playerBat.brickScore));
            scoreShow.setLayoutX(290);
            scoreShow.setLayoutY(500);
            scoreShow.setFont(new Font(30));
            Button again = new Button();
            again.setText("Do It Again");
            again.setLayoutX(350);
            again.setLayoutY(700);
            again.setTextFill(Color.DARKOLIVEGREEN);
            again.setOnMouseClicked(e -> {
                start();
            });
            pane.getChildren().addAll(gameOver, scoreShow, again);

        }
        n++;
        boolean did = false, intersected = false;
        if (ball.intersects(playerBat.getBoundsInLocal()) || playerBat.intersects(ball.getBoundsInLocal())) {
            intersected = true;
            did = true;
            Ball.ySpeed = -Ball.ySpeed;
        }
        else if (ball.getCenterX() + ball.getRadius() >= 800){
            intersected = true;
            did = true;
            Ball.xSpeed = -Ball.xSpeed;
//            t.setDelay(Duration.millis(1000));
        }
        else if (ball.getCenterX() - ball.getRadius() <= 0){
            intersected = true;
            did = true;
            Ball.xSpeed = -Ball.xSpeed;
        }
        else if (ball.getCenterY() + ball.getRadius() >= 900){
            if (playerBat.isGameOver)
                return;
            playerBat.isGameOver = true;
            Label gameOver = new Label();
            gameOver.setText("Game Over Loooooooser !");
            gameOver.setLayoutY(400);
            gameOver.setFont(new Font(68));
            Label scoreShow = new Label();
            scoreShow.setText("Score = " + ((playerBat.timeScore-= n*speed/1000 * 20)+ playerBat.brickScore));
            scoreShow.setLayoutX(290);
            scoreShow.setLayoutY(500);
            scoreShow.setFont(new Font(30));
            Button again = new Button();
            again.setText("Try Again");
            again.setLayoutX(350);
            again.setLayoutY(700);
            again.setTextFill(Color.DARKOLIVEGREEN);
            again.setOnMouseClicked(e -> {
                start();
            });
            pane.getChildren().addAll(gameOver, scoreShow, again);
        }
        if (ball.getCenterY() + ball.getRadius() <= 0){
            intersected = true;
            did = true;
            Ball.ySpeed = - Ball.ySpeed;
        }
        else {
            for (int i = 0; i < bricks.size(); i++){
                if (ball.getBoundsInLocal().intersects(bricks.get(i).getBoundsInLocal()) || bricks.get(i).getBoundsInLocal().intersects(ball.getBoundsInLocal())){
                    intersectedNumber = i;
//                    System.out.println("&&&");
//                    System.out.println("cx: " + ball.getCenterX() +" cy: "+ball.getCenterY() + "   Rad:" + ball.getRadius());
//                    System.out.println("rx: " + bricks.get(i).getX() + "    ry:" + bricks.get(i).getY());
                    intersected = true;
                    if (ball.getCenterX() + ball.getRadius() - bricks.get(i).getX() >= 0 && ball.getCenterX() + ball.getRadius() - bricks.get(i).getX() <= Brick.brickWidth && ball.getCenterY() - bricks.get(i).getY() >= 0 && ball.getCenterY() - bricks.get(i).getY() <= Brick.brickHeight) {
                        if (ball.getCenterX() + ball.getRadius() - bricks.get(i).getX() != 0 || ball.getCenterY() - bricks.get(i).getY() != 0){

                            Ball.xSpeed = -Ball.xSpeed;
//                            System.out.println("#1");
                            did = true;
                            bricks.get(i).necessaryTimes--;
                            if (bricks.get(i).necessaryTimes == 0) {
                                playerBat.brickScore += bricks.get(i).score;
                                pane.getChildren().remove(bricks.get(i));
                                bricks.remove(bricks.get(i));
                                break;
                            } else if (bricks.get(i).necessaryTimes != 0 && ball.isRadioActive) {
                                playerBat.brickScore += bricks.get(i).score;
                                pane.getChildren().remove(bricks.get(i));
                                bricks.remove(bricks.get(i));
                                break;
                            } else if (bricks.get(i).necessaryTimes > 0 && ball.isFired) {
                                playerBat.brickScore += bricks.get(i).score;
                                pane.getChildren().remove(bricks.get(i));
                                bricks.remove(bricks.get(i));
                                break;
                            }
                        }
                    }




                    if (!did && ((ball.getCenterX() - ball.getRadius() - bricks.get(i).getX()) >= 0) && ((ball.getCenterX() - ball.getRadius() - bricks.get(i).getX()) <= Brick.brickWidth) && ((ball.getCenterY() - bricks.get(i).getY()) >= 0) && ((ball.getCenterY() - bricks.get(i).getY()) <= Brick.brickHeight)) {
                        if (ball.getCenterX() - ball.getRadius() - bricks.get(i).getX() != 0 || ball.getCenterY() - bricks.get(i).getY() != 0){

                            Ball.xSpeed = -Ball.xSpeed;
//                            System.out.println("#2");
                            did = true;
                            bricks.get(i).necessaryTimes--;
                            if (bricks.get(i).necessaryTimes == 0) {
                                playerBat.brickScore += bricks.get(i).score;
                                pane.getChildren().remove(bricks.get(i));
                                bricks.remove(bricks.get(i));
                                break;
                            } else if (bricks.get(i).necessaryTimes != 0 && ball.isRadioActive) {
                                playerBat.brickScore += bricks.get(i).score;
                                pane.getChildren().remove(bricks.get(i));
                                bricks.remove(bricks.get(i));
                                break;
                            } else if (bricks.get(i).necessaryTimes > 0 && ball.isFired) {
                                playerBat.brickScore += bricks.get(i).score;
                                pane.getChildren().remove(bricks.get(i));
                                bricks.remove(bricks.get(i));
                                break;
                            }
                        }
                    }




                    if (!did && ball.getCenterY() + ball.getRadius() - bricks.get(i).getY() >= 0 && ball.getCenterY() + ball.getRadius() - bricks.get(i).getY() <= Brick.brickHeight && ball.getCenterX() - bricks.get(i).getX() >= 0 && ball.getCenterX() - bricks.get(i).getX() <= Brick.brickWidth) {
                        if (ball.getCenterY() + ball.getRadius() - bricks.get(i).getY() != 0 || ball.getCenterX() - bricks.get(i).getX() != 0)
                        Ball.ySpeed = -Ball.ySpeed;
                        did = true;
//                        System.out.println("#3");
                        bricks.get(i).necessaryTimes--;
                        if (bricks.get(i).necessaryTimes == 0) {
                            playerBat.brickScore += bricks.get(i).score;
                            pane.getChildren().remove(bricks.get(i));
                            bricks.remove(bricks.get(i));
                            break;
                        } else if (bricks.get(i).necessaryTimes != 0 && ball.isRadioActive) {
                            playerBat.brickScore += bricks.get(i).score;
                            pane.getChildren().remove(bricks.get(i));
                            bricks.remove(bricks.get(i));
                            break;
                        } else if (bricks.get(i).necessaryTimes > 0 && ball.isFired) {
                            playerBat.brickScore += bricks.get(i).score;
                            pane.getChildren().remove(bricks.get(i));
                            bricks.remove(bricks.get(i));
                            break;
                        }
                    }





                    if (!did && ball.getCenterY() - ball.getRadius() - bricks.get(i).getY() >= 0 && ball.getCenterY() - ball.getRadius() - bricks.get(i).getY() <= Brick.brickHeight && ball.getCenterX() - bricks.get(i).getX() >= 0 && ball.getCenterX() - bricks.get(i).getX() <= Brick.brickWidth) {
                        if (ball.getCenterY() - ball.getRadius() - bricks.get(i).getY() != 0 || ball.getCenterX() - bricks.get(i).getX() != 0)
                        {

                            Ball.ySpeed = -Ball.ySpeed;
//                            System.out.println("#4");
//                        Scanner scanner = new Scanner(System.in);
//                        scanner.next();
                            did = true;
                            bricks.get(i).necessaryTimes--;
                            if (bricks.get(i).necessaryTimes == 0) {
                                playerBat.brickScore += bricks.get(i).score;
                                pane.getChildren().remove(bricks.get(i));
                                bricks.remove(bricks.get(i));
                                break;
                            } else if (bricks.get(i).necessaryTimes != 0 && ball.isRadioActive) {
                                playerBat.brickScore += bricks.get(i).score;
                                pane.getChildren().remove(bricks.get(i));
                                bricks.remove(bricks.get(i));
                                break;
                            } else if (bricks.get(i).necessaryTimes > 0 && ball.isFired) {
                                playerBat.brickScore += bricks.get(i).score;
                                pane.getChildren().remove(bricks.get(i));
                                bricks.remove(bricks.get(i));
                                break;
                            }
                        }
                    }

                }
            }

            if (!did && intersected){
                Ball.xSpeed = -Ball.xSpeed;
                Ball.ySpeed = -Ball.ySpeed;
                bricks.get(intersectedNumber).necessaryTimes--;
                if (bricks.get(intersectedNumber).necessaryTimes == 0) {
                    playerBat.brickScore += bricks.get(intersectedNumber).score;
                    pane.getChildren().remove(bricks.get(intersectedNumber));
                    bricks.remove(bricks.get(intersectedNumber));
                } else if (bricks.get(intersectedNumber).necessaryTimes != 0 && ball.isRadioActive) {
                    playerBat.brickScore += bricks.get(intersectedNumber).score;
                    pane.getChildren().remove(bricks.get(intersectedNumber));
                    bricks.remove(bricks.get(intersectedNumber));
                } else if (bricks.get(intersectedNumber).necessaryTimes > 0 && ball.isFired) {
                    playerBat.brickScore += bricks.get(intersectedNumber).score;
                    pane.getChildren().remove(bricks.get(intersectedNumber));
                    bricks.remove(bricks.get(intersectedNumber));
                }
            }
        }


//        if (!did && intersected){
//            Ball.ySpeed = -Ball.ySpeed;
//            Ball.xSpeed = -Ball.xSpeed;
//        }

        did = false;
        ball.setCenterX(ball.getCenterX() + Ball.xSpeed);
        ball.setCenterY(ball.getCenterY() + Ball.ySpeed);
        score.setText("Brick Score = " + playerBat.brickScore +" Time : " + n*speed/1000 +"secs");

        for (int i = 0; i < powerUps.size(); i++){
            powerUps.get(i).move();
            powerUps.get(i).stop(n, speed, this);
            if (powerUps.get(i).intersects(playerBat.getBoundsInLocal())){
                powerUps.get(i).play(n, speed, this);
            }
        }

        if ((n*speed)%20000 >= 0 && (n*speed)%20000 < speed){
            powerUp();
        }
    }


    @FXML
    void exit(){
        Platform.exit();
    }

    void pauseTheGame(){
        if (ball.isStopped){
            ball.isStopped = false;
        }
        else {
            ball.isStopped = true;
        }
    }

    void powerUp(){
        Random rnd = new Random();

        double x = 20 + 760 * rnd.nextDouble();
        double y = 20 + 400 * rnd.nextDouble();
        int velocity = (int) (900 - y)/4;
        int type = rnd.nextInt(6);

        if (type == 0){
            PowerUp powerUp = new BallSpeedDoublePowerUp(x, y, velocity/speed);
            pane.getChildren().add(powerUp);
            powerUps.add(powerUp);
        }
        else if (type == 1){
            PowerUp powerUp = new BallSpeedHalfPowerUp(x, y, velocity/speed);
            pane.getChildren().add(powerUp);
            powerUps.add(powerUp);

        }
        else if (type == 2){

            PowerUp powerUp = new LengthPowerUp(x, y, velocity/speed);
            pane.getChildren().add(powerUp);
            powerUps.add(powerUp);
        }
        else if (type == 3){

            PowerUp powerUp = new BallTakhayoliPowerUp(x, y, velocity/speed);
            pane.getChildren().add(powerUp);
            powerUps.add(powerUp);
        }
        else if (type == 4){

            PowerUp powerUp = new FireBallPowerUp(x, y, velocity/speed);
            pane.getChildren().add(powerUp);
            powerUps.add(powerUp);
        }
    }
}
