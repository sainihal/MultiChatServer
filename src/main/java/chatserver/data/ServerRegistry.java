package chatserver.data;

import chatserver.model.ClientData;

/**
 * Created by sainihala on 17/7/16.
 */
public interface ServerRegistry {
     void register(String name, ClientData clientData);
     void deRegister(String name);
     boolean exists(String name);
     ClientData getData(String name);
}
