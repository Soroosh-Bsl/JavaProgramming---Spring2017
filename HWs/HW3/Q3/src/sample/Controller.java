package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;

import java.io.*;


public class Controller {
    Client client;
    Server server;
    Media hit;
    MediaPlayer mediaPlayer;
    int allImages, imageNum, allSounds, soundNum;


        @FXML
        private TextField area;

        @FXML
        private TextField ip;

        @FXML
        private TextField message;

        @FXML
        private Button send;

        @FXML
        private Button connect;

        @FXML
        private TextField name;


        @FXML
        private ImageView imageViewer;

        @FXML
        private AnchorPane pane;

        @FXML
        void connect(ActionEvent event) {
            Client client = new Client(1900, name.getText(), ip.getText());
            this.client = client;
            client.run();

            new Thread(){
                @Override
                public void run(){
                    while (true){
                        try {
                            ChatMessage message = (ChatMessage) (client.objectInputStream.readObject());
//                            print(message);

                            Platform.runLater(new Thread(){
                                @Override
                                public void run() {
                                    area.setAlignment(Pos.BASELINE_LEFT);
                                    area.appendText("\r\n" + message.getSender() +" : " + message.getMessage());
                                    if (message.getFile() != null){
                                        if (message.getFile().getName().substring(message.getFile().getName().lastIndexOf(".")).equalsIgnoreCase(".jpg") ||
                                                message.getFile().getName().substring(message.getFile().getName().lastIndexOf(".")).equalsIgnoreCase(".png") ||
                                                message.getFile().getName().substring(message.getFile().getName().lastIndexOf(".")).equalsIgnoreCase(".gif") ||
                                                message.getFile().getName().substring(message.getFile().getName().lastIndexOf(".")).equalsIgnoreCase(".jpeg"))
                                            imageViewer.setImage(new Image("file:"+message.getFile().getPath()));
                                        else if (message.getFile().getName().substring(message.getFile().getName().lastIndexOf(".")).equalsIgnoreCase(".mp3") ||
                                                message.getFile().getName().substring(message.getFile().getName().lastIndexOf(".")).equalsIgnoreCase(".wav")){
//                                            AudioStream audioStream = new AudioStream();

                                            hit = new Media(message.getFile().toURI().toString());
                                            mediaPlayer = new MediaPlayer(hit);
//                                            mediaPlayer.play();
                                        }
                                    }
                                }
                            });
                        } catch (ClassNotFoundException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }

        @FXML
        void listen(ActionEvent event) {
            Server server = new Server(12110, name.getText(), ip.getText());
            this.server = server;
            server.run();

            new Thread(){
                @Override
                public void run(){
                    while (true){
                        try {
                            ChatMessage message = (ChatMessage) (server.objectInputStream.readObject());
                            Platform.runLater(new Thread(){
                                @Override
                                public void run() {
                                    area.setAlignment(Pos.BASELINE_LEFT);
                                    area.appendText("\r\n" + message.getSender()+" : "  + message.getMessage());
                                    if (message.getFile() != null){
                                        if (message.getFile().getName().substring(message.getFile().getName().lastIndexOf(".")).equalsIgnoreCase(".JPG") ||
                                                message.getFile().getName().substring(message.getFile().getName().lastIndexOf(".")).equalsIgnoreCase(".png") ||
                                                message.getFile().getName().substring(message.getFile().getName().lastIndexOf(".")).equalsIgnoreCase(".gif") ||
                                                message.getFile().getName().substring(message.getFile().getName().lastIndexOf(".")).equalsIgnoreCase(".jpeg")){
                                            imageViewer.setImage(new Image("file:"+message.getFile().getPath()));
                                        }
                                    else if (message.getFile().getName().substring(message.getFile().getName().lastIndexOf(".")).equalsIgnoreCase(".mp3") ||
                                                message.getFile().getName().substring(message.getFile().getName().lastIndexOf(".")).equalsIgnoreCase(".wav")){

                                            hit = new Media(message.getFile().toURI().toString());
                                            mediaPlayer = new MediaPlayer(hit);
//                                            mediaPlayer.play();
                                        }
                                    }

                                }
                            });
                        } catch (ClassNotFoundException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }

        @FXML
        void send(ActionEvent event){
            String text = message.getText();
            if (text.contains(":)"))
                text = text.replace(":)", "\uD83D\uDE43");
            if (text.contains(":heart!"))
                text = text.replace(":heart!", "\uD83D\uDE0D");
            if (text.contains(":shakh!"))
                text = text.replace(":shakh!", "\uD83D\uDE0E");
            if (text.contains(":kiss!"))
                text = text.replace(":kiss!", "\uD83D\uDE18");


                if (client != null){
                    if (message.getText() != null && message.getText().length() > 0){
                        client.sendData(text, null);
                        area.setAlignment(Pos.BASELINE_LEFT);
                        area.appendText("\r\n"+ client.name +" : " + text);
                        message.setText("");
                    }

                }

                else if (server != null){
                        if (message.getText() != null && message.getText().length() > 0){
                            server.sendData(text, null);
                            area.setAlignment(Pos.BASELINE_LEFT);
                            area.appendText("\r\n" + server.name +" : " + text);
                            message.setText("");
                        }
                }
        }

        @FXML
        void browse(){
            FileChooser fileChooser = new FileChooser();
            File file = (fileChooser.showOpenDialog(pane.getScene().getWindow()));
            sendImage(file);
        }

        void sendImage(File file){
            if (client!=null){
                client.sendData(file.getPath().substring(file.getPath().lastIndexOf(".") + 1) + "File Received", file);
            }
            else if (server!=null){
                server.sendData(file.getPath().substring(file.getPath().lastIndexOf(".") + 1) + "File Received", file);
            }
        }

        @FXML
        void play(){
            if (mediaPlayer != null){
                mediaPlayer.play();
            }
        }

        @FXML
        void stop(){
            if (mediaPlayer != null){
                mediaPlayer.stop();
            }
        }
}
