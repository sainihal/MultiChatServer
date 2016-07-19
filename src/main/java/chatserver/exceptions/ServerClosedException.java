package chatserver.exceptions;

/**
 * Created by sainihala on 16/7/16.
 */
public class ServerClosedException extends RuntimeException {
    public ServerClosedException(String serverClosed){
        super(serverClosed);
    }
}
