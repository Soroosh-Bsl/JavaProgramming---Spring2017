package sample;

import javafx.scene.image.Image;

import java.io.File;
import java.io.Serializable;

/**
 * Created by smjfas on 5/27/17.
 */
public class ChatMessage implements Serializable{
    private String message;
    private String sender;
    private String reciever;
    private File file;

    public ChatMessage(String message, String sender, String reciever, File file) {
        this.message = message;
        this.sender = sender;
        this.reciever = reciever;
        this.file = file;
    }

    public String getMessage() {
        return message;
    }


    public String getSender() {
        return sender;
    }

    public File getFile() {
        return file;
    }
}

