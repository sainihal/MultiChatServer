package chatserver.model;

import chatserver.data.ServerRegistry;

/**
 * Created by sainihala on 18/7/16.
 */
public class ServerContext {
    private ServerRegistry serverRegistry;
    private boolean closed;

    public ServerContext(ServerRegistry serverRegistry){
        this.serverRegistry = serverRegistry;
    }

    public ServerRegistry getServerRegistry() {
        return serverRegistry;
    }

    public void setServerRegistry(ServerRegistry serverRegistry) {
        this.serverRegistry = serverRegistry;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
