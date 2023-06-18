package interfaces;

import java.io.IOException;
import attributes.Request;

/**
 *
 */
public interface Sendable
{
    public void sendRequest(Request request) throws IOException;
}