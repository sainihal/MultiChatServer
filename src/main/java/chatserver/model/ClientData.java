package chatserver.model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by sainihala on 18/7/16.
 */
public class ClientData {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String name;

    public ClientData(String name,Socket socket, ObjectInputStream objectInputStream,
                      ObjectOutputStream objectOutputStream) {
        this.name = name;
        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
    }

    public Socket getSocket() {

        return socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public void setObjectInputStream(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }
}
