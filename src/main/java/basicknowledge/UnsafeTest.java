package basicknowledge;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UnsafeTest {
    private int offset = 1;
    private static String cmd = "whoami";

    private String SECRET = "666";

    public int getOffset() {
        return offset;
    }

    public String getCmd() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(cmd).getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}