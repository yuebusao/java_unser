package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class mysql {
    public static void main(String[] args) {
        try{
            String driver = "com.mysql.jdbc.Driver";
            String DB_URL = "jdbc:mysql://127.0.0.1:3306/test?autoDeserialize=true&queryInterceptors=com.mysql.cj.jdbc.interceptors.ServerStatusDiffInterceptor&user=calc";//8.x使用
            //String DB_URL = "jdbc:mysql://127.0.0.1:3306/test?detectCustomCollations=true&autoDeserialize=true&user=yso_JRE8u20_calc";//5.x使用
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(DB_URL);
        }catch (Exception e){

        }

    }

}
