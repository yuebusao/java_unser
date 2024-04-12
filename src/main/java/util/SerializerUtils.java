package util;

import util.customOutputStream.NativeCustomObjectOutputStream;

import java.io.*;
import java.util.Base64;

public class SerializerUtils {
    private final Object object;
    public SerializerUtils(Object object) {
        this.object = object;
    }

    public byte[] call() throws Exception {
        return serialize(object);
    }

    public static byte[] serialize(final Object obj) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        serialize(obj, out);
        return out.toByteArray();
    }

    public static byte[] serializeByOverLongUTF(final Object obj) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        serializeByOverLongUTF(obj, out);
        return out.toByteArray();
    }

    public static String serializeToFile(final Object obj) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("test.ser");
        serialize(obj,fileOutputStream);
        fileOutputStream.close();
        return "test.ser";
    }
    public static String serializeToFileByOverLongUTF(final Object obj) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("test.ser");
        serializeByOverLongUTF(obj,fileOutputStream);
        fileOutputStream.close();
        return "test.ser";
    }

    public static String serializeToFile(final Object obj, final String filename) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        serialize(obj,fileOutputStream);
        fileOutputStream.close();
        return filename;
    }
    public static String serializeBase64(final Object obj) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        serialize(obj, out);

        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    public static void unserialize(byte[] payload) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(payload));
        ois.readObject();
    }

    public static void serialize(final Object obj, final OutputStream out) throws IOException {
        final ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
    }

    public static void serializeByOverLongUTF(final Object obj, final OutputStream out) throws IOException {
        final ObjectOutputStream objOut = new NativeCustomObjectOutputStream(out);
        objOut.writeObject(obj);
    }
}
