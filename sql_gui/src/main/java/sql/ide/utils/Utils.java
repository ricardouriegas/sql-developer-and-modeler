package sql.ide.utils;

/**
 * This class contains utility methods that are used throughout the application.
  */
public class Utils {
    public void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
