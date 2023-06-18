package interfaces;

import java.io.IOException;
import users.User;

/**
 */
public interface Viewable
{
    /**
     * @throws IOException 
     */
    public static void viewAttestation() throws IOException {
	}

    /**
     */
    public static void viewJournal() {
	}

    /**
     * @param <T>
     */
    public static void viewSchedule(User u) {
    }
}

