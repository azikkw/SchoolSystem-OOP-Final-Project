package interfaces;

import java.io.IOException;
import attributes.Research;

/**
 */
public interface Researchable
{
    void doResearch(Research research) throws IOException;
}