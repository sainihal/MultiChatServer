package chatserver.helpers;

import chatserver.data.ServerRegistry;
import chatserver.model.ClientContext;
import com.wavemaker.utils.exceptions.AppIOException;
import com.wavemaker.utils.messages.ChatMessage;
import com.wavemaker.utils.messages.Message;
import com.wavemaker.utils.messages.NoClientExists;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sainihala on 17/7/16.
 */
public class ClientWriterHelper {
    private static Logger logger = Logger.getLogger(ClientReaderHelper.class.getName());


    public static void writeToReceiver(Message message, ClientContext clientContext) {
        ServerRegistry serverRegistry = clientContext.getServerContext().getServerRegistry();
        String name = clientContext.getClientData().getName();
        if (clientContext.isClosed()) {
            return;
        }
        try {
            if (message.getType() == Message.MessageType.CHAT ) {
                ChatMessage chatMessage = (ChatMessage) message;
                logger.log(Level.INFO, "destination is " + chatMessage.getDestination());
                if (serverRegistry.exists(chatMessage.getDestination())) {
                    serverRegistry.getData(chatMessage.getDestination()).getObjectOutputStream().writeObject(message);
                } else {
                    message = new NoClientExists();
                    serverRegistry.getData(name).getObjectOutputStream().writeObject(message);
                }
                logger.log(Level.INFO, " message  written to client " + message.toString());
            }
            else {
                serverRegistry.getData(name).getObjectOutputStream().writeObject(message);
            }
        }catch (IOException  ioe){
            clientContext.setClosed(true);
            throw new AppIOException("in worsker of client "+name+"   " ,ioe);
        }

    }
}