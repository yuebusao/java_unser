import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import gadget.memshell.TomcatShellInject2;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.catalina.loader.ParallelWebappClassLoader;
import util.GadgetUtils;
import util.ReflectionUtils;
import util.SerializerUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Base64;

public class ArrayTest {
    public static void main(String[] args) throws Exception {
        Class test = Class.forName("Test");
        Object a =test.getConstructor(String.class).newInstance("a");

        Method m = test.getDeclaredMethod("DiaoWo");
        m.setAccessible(true);
        m.invoke(a);

        Method m1 = test.getDeclaredMethod("DiaoDiaoWo",String.class);
        m1.setAccessible(true);
        m1.invoke(a,"ws");

        String bytecodeBase64="MQ==";
        byte[] bytecode;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();


        Class clsBase64 = ClassLoader.getSystemClassLoader().loadClass("java.util.Base64");
        Constructor base64c= clsBase64.getDeclaredConstructor();
        base64c.setAccessible(true);
        Object base64i = base64c.newInstance();

        Object decoder = clsBase64.getDeclaredMethod("getDecoder").invoke(base64i); //获得Decoder实例
        bytecode = (byte[]) decoder.getClass().getMethod("decode",String.class).invoke(decoder,bytecodeBase64);
        System.out.println(bytecode);

//        Object base64 = base64clz.getDeclaredConstructors(String.class);
//        Method c = Base64clz.getMethod("getDecoder").invoke();
    }
}
