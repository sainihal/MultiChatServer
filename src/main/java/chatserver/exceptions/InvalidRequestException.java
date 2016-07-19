package chatserver.exceptions;

/**
 * Created by sainihala on 13/7/16.
 */
public class InvalidRequestException extends  RuntimeException {

    public InvalidRequestException(String invalidRequest){
        super(invalidRequest);
    }


}
