package chatserver.worker;

/**
 * Created by sainihala on 23/6/16.
 */


import chatserver.helpers.ClientReaderHelper;
import chatserver.helpers.ClientWriterHelper;
import chatserver.model.ClientContext;
import chatserver.model.ClientData;
import chatserver.model.ServerContext;
import com.wavemaker.utils.exceptions.AppClassNotFoundException;
import com.wavemaker.utils.exceptions.AppIOException;
import com.wavemaker.utils.messages.Message;
import com.wavemaker.utils.messages.RegisterMessage;
import com.wavemaker.utils.messages.RegistrationFailed;
import com.wavemaker.utils.messages.RegistrationSuccess;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sainihala on 23/6/16.
 */
public class ClientWorker implements Runnable {

    private static final Logger logger = Logger.getLogger(ClientWorker.class.getName());
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String name;
    private ClientContext clientContext = new ClientContext();

    public ClientWorker(Socket socket, ServerContext serverContext) {
        this.socket = socket;
        this.clientContext.setServerContext(serverContext);

        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ioe) {
            closeStreams();
            throw new AppIOException("In creating streams", ioe);
        }
        clientInit();
    }

    public void run() {
        startProcess();
    }


    private void startProcess() {

        try {
            while (!clientContext.isClosed() && !clientContext.getServerContext().isClosed()) {
                Message message = ClientReaderHelper.readFromSender(clientContext);
                if (clientContext.isClosed()) {
                    clientContext.getServerContext().getServerRegistry().deRegister(name);
                    break;
                }
                ClientWriterHelper.writeToReceiver(message, clientContext);
            }
        } finally {
            logger.log(Level.INFO, "closing streams");
            closeStreams();
        }
    }

    private void clientInit() {
        Message message;

        try {
            try {
                message = (Message) objectInputStream.readObject();
            } catch (ClassNotFoundException classNotFound) {
                throw new AppClassNotFoundException("ClassNotFoundException in  client", classNotFound);
            }

            this.name = ((RegisterMessage) message).getName();
            if (clientContext.getServerContext().getServerRegistry().exists(name)) {
                logger.log(Level.INFO, "client name already registered");
                message = new RegistrationFailed("client name already registered");
                clientContext.setClosed(true);
            } else {
                ClientData clientData = new ClientData(name, socket, objectInputStream, objectOutputStream);
                clientContext.setClientData(clientData);
                clientContext.getServerContext().getServerRegistry().register(name, clientData);
                logger.log(Level.INFO, "new  client registering with name " + name);
                message = new RegistrationSuccess(name);
            }
            objectOutputStream.writeObject(message);
        } catch (IOException ioException) {
            closeStreams();
            throw new AppIOException("In client streams ", ioException);
        }
    }

    private void closeStreams() {
        logger.log(Level.INFO, " closing server steams........");
        try {
            if (objectOutputStream != null) {
                objectOutputStream.close();
            }
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "In closing output stream...  ", ioe.getStackTrace());

        }
        try {
            if (objectInputStream != null) {
                objectInputStream.close();
            }
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "In closing intput stream...  ", ioe.getStackTrace());
        }
    }
}


