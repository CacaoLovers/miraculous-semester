package exceptions;

import java.io.IOException;

public class SocketConnectionError extends RuntimeException {
    public SocketConnectionError(String errorMessage){
        super(errorMessage);
    }
}
