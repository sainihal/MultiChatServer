package chatserver.server;


import chatserver.model.Context;
import com.wavemaker.utils.exceptions.AppIOException;
import com.wavemaker.utils.properties.Constants;
import com.wavemaker.utils.properties.PropertyLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sainihala on 23/6/16.
 */
public class ServerApp {
    private static final Logger logger = Logger.getLogger(ServerApp.class.getName());

    public ServerApp(){}

    public static void main(String args[]) {
        int port = (args.length == 0) ? PropertyLoader.getPort() : Integer.parseInt(args[0]);
        Context context = new Context();
        String status = "run";
        ChatServer chatServer = new ChatServer(port, context);
        BufferedReader br  = new BufferedReader(new InputStreamReader(System.in));
        Thread thread = new Thread(chatServer);

        thread.start();
        logger.log(Level.INFO,"enter "+Constants.EXIT_KEY+ " to close the server");

        try {
            outerloop:
            while (!status.equals(Constants.EXIT_KEY) && !context.isClosed()) {
                while(!br.ready()){
                    if(context.isClosed()) {
                        break outerloop;
                    }
                }
                status = br.readLine();
            }
        } catch (IOException e) {
            throw new AppIOException("In Chat server invoker ",e);
        }

        if (status.equals(Constants.EXIT_KEY)) {
            chatServer.closeClients();
        }
    }
}
