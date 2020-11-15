package sample;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    boolean isServerSet = false;
    int port;
    String name;
    String ip;
    ServerSocket serverSocket;
    Socket socket;

    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;

    public Server(int port, String name, String ip) {
        this.port = port;
        this.name = name;
        this.ip = ip;
    }

    public void run(){
        setUpServer();
        waitForClient();
        getIOStreams();
    }

    private void setUpServer(){
        try {
            serverSocket = new ServerSocket(1900);
            isServerSet = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void waitForClient() {
        try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void getIOStreams() {
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendData(String string, File file){
        ChatMessage message = new ChatMessage(string, this.name, "Client", file);
        try {
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

