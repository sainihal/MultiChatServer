package chatserver.model;

/**
 * Created by sainihala on 17/7/16.
 */
public class Context {
    private boolean context;
    public void setClosed(boolean value){
        context = value;
    }
    public boolean isClosed(){
        return context;
    }

}

