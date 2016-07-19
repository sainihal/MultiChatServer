package chatserver.helpers;

import chatserver.model.ClientContext;
import com.wavemaker.utils.exceptions.AppClassNotFoundException;
import com.wavemaker.utils.exceptions.AppIOException;
import com.wavemaker.utils.messages.Message;
import com.wavemaker.utils.messages.ServerExiting;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sainihala on 17/7/16.
 */
public class ClientReaderHelper {

    private static Logger logger = Logger.getLogger(ClientReaderHelper.class.getName());

    public static  Message readFromSender(ClientContext clientContext) {
        Message message;
        try {
            while (true) {
                if (clientContext.getClientData().getSocket().getInputStream().available() != 0) { //todo
                    break;
                }
                if (clientContext.getServerContext().isClosed()) {
                    message = new ServerExiting();
                    return message;
                }
            }
            try {
                message = (Message) clientContext.getClientData().getObjectInputStream().readObject();
            } catch (ClassNotFoundException classNotFound) {
                clientContext.setClosed(true);
                throw new AppClassNotFoundException("in client " + clientContext.getClientData().getName(), classNotFound);
            }
            logger.log(Level.INFO, "Server: reading from client......." + clientContext.getClientData().getName());

            switch (message.getType()) {
                case CHAT: {
                    break;
                }
                case QUIT: {
                    clientContext.setClosed(true);
                    break;
                }
            }
        } catch (IOException ioe) {
            clientContext.setClosed(true);
            throw new AppIOException("in reaeding from client " + clientContext.getClientData().getName(), ioe);
        }
        logger.log(Level.INFO, message.toString());
        return message;
    }
}
