package sample;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    String name;
    int port = 1900;
    Socket client;
    String ip;

    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;

    public Client(int port,String name, String ip) {
        this.port = port;
        this.name = name;
        this.ip = ip;
    }

    public void run(){
        connect2Server();
        getIOStreams();
    }

    private void connect2Server() {
        try {
            client = new Socket(ip, port);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private void getIOStreams() {
        try {
            if (client != null){
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                objectInputStream = new ObjectInputStream(client.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendData(String string, File file){
        ChatMessage message = new ChatMessage(string, this.name, "Server", file);

        try {
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}