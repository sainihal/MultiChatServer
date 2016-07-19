package chatserver.server;


import chatserver.data.ServerRegistryImpl;
import chatserver.model.Context;
import chatserver.model.ServerContext;
import chatserver.worker.ClientWorker;
import com.wavemaker.utils.exceptions.AppIOException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sainihala on 24/6/16.
 */
public class ChatServer implements Runnable {

    private static final Logger lOGGER = Logger.getLogger(ServerApp.class.getName());

    private List<Thread> threadsList = new ArrayList<Thread>();
    private ServerSocket serverSocket;
    private ServerContext serverContext = new ServerContext(new ServerRegistryImpl());
    private int port;
    private Context context;

    public ChatServer(int port, Context context){
        this.port = port;
        this.context = context;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            context.setClosed(true);    //todo ask is the correct approach
            throw new AppIOException("In  ServerSocket creation ", e);
        }
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                Thread serverThread = new Thread(new ClientWorker(socket,serverContext));
                threadsList.add(serverThread);
                serverThread.start();
            }
        } catch (IOException e) {
            closeClients();
            lOGGER.log(Level.SEVERE, "In server ", e);
        }
    }

    public void closeClients() {
        serverContext.setClosed(true);
        context.setClosed(true);

        for (Thread thread : threadsList) {
            thread.interrupt();
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            lOGGER.log(Level.SEVERE, "In  server socket closing", e);
        }
    }
}



