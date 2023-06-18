package interfaces;

import java.io.IOException;

import users.User;

/**
 */
public interface Requestable {
    /**
     * @throws IOException 
     */
    public boolean seeRequest(User u, String requestType, String requestMess) throws IOException;
}

