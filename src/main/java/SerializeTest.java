import util.SerializerUtils;

import java.io.IOException;

public class SerializeTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        SerializerUtils.unserialize(SerializerUtils.serializeByOverLongUTF(new RCE()));
        Runtime runtime = Runtime.getRuntime();
        String[] echo = new String[]{"echo 123",">test"};
        runtime.exec(echo);
    }
}
