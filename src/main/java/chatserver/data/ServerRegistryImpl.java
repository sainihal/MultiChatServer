package chatserver.data;

import chatserver.model.ClientData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sainihala on 24/6/16.
 */
public class ServerRegistryImpl  implements ServerRegistry {
    public Map<String, ClientData> map = new ConcurrentHashMap<String, ClientData>();

    public  void register(String name, ClientData clientData) {
        map.put(name,clientData);
    }
    public void deRegister(String name) {
        map.remove(name);
    }
    public ClientData getData(String name) {
        return map.get(name);
    }
    public boolean exists(String name ){
        return (map.get(name) != null);
    }


}
