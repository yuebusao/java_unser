import javassist.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ArrayTest {
    public static void main(String[] args) throws Exception {
//        Class test = Class.forName("Test");
//        Object a =test.getConstructor(String.class).newInstance("a");
//
//        Method m = test.getDeclaredMethod("DiaoWo");
//        m.setAccessible(true);
//        m.invoke(a);
//
//        Method m1 = test.getDeclaredMethod("DiaoDiaoWo",String.class);
//        m1.setAccessible(true);
//        m1.invoke(a,"ws");
//
//        String bytecodeBase64="MQ==";
//        byte[] bytecode;
//
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//
//
//        Class clsBase64 = ClassLoader.getSystemClassLoader().loadClass("java.util.Base64");
//        Constructor base64c= clsBase64.getDeclaredConstructor();
//        base64c.setAccessible(true);
//        Object base64i = base64c.newInstance();
//
//        Object decoder = clsBase64.getDeclaredMethod("getDecoder").invoke(base64i); //获得Decoder实例
//        bytecode = (byte[]) decoder.getClass().getMethod("decode",String.class).invoke(decoder,bytecodeBase64);
//        System.out.println(bytecode);

//        Object base64 = base64clz.getDeclaredConstructors(String.class);
//        Method c = Base64clz.getMethod("getDecoder").invoke();
        test();

    }

        public static void test() throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            Test test=new Test("java assist?");
            // TODO: 获取ClassPool
            ClassPool classPool = ClassPool.getDefault();
            CtClass ctClass = classPool.get("Test");
            // TODO: 获取sayHelloFinal方法
            CtMethod ctMethod = ctClass.getDeclaredMethod("DiaoWo");
            // TODO: 方法前后进行增强
            ctMethod.insertBefore("{ System.out.println(\"start\");}");
            ctMethod.insertAfter("{ System.out.println(\"end\"); }");
            // TODO: CtClass对应的字节码加载到JVM里
            Class c = ctClass.toClass();

            //反射生成hook后的类
//            Test test = (Test) c.getDeclaredConstructor(String.class).newInstance("java assist");
            Method diaowo = c.getDeclaredMethod("DiaoWo");
            diaowo.setAccessible(true);
            diaowo.invoke(test);
        }

    }
